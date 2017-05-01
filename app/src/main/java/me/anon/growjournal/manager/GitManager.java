package me.anon.growjournal.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import lombok.Getter;
import me.anon.growjournal.event.NewCommitEvent;
import me.anon.growjournal.helper.BusHelper;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * // TODO: Add class description
 */
public class GitManager
{
	private static GitManager instance = null;

	@Getter private File localRepo;
	@Getter private Git git;
	private Context context;

	public static GitManager getInstance()
	{
		if (instance == null)
		{
			synchronized (GitManager.class)
			{
				instance = new GitManager();
			}
		}

		return instance;
	}

	/**
	 * Initialises the manager and loads/creates the base repository
	 * @param context
	 */
	public void initialise(final Context context)
	{
		this.context = context.getApplicationContext();

		localRepo = new File(context.getFilesDir() + "/site/");

		if (!localRepo.exists())
		{
			createNewRepo();
			copyAssets("site", context);
		}

		if (git == null)
		{
			try
			{
				git = Git.init()
					.setDirectory(new File(localRepo.getAbsolutePath()))
					.call();
			}
			catch (GitAPIException e)
			{
				e.printStackTrace();
			}
		}

		if (git == null)
		{
			throw new RuntimeException("Failed to init git object");
		}
	}

	/**
	 * Creates a new bare repository
	 */
	protected void createNewRepo()
	{
		localRepo.mkdirs();

		try
		{
			Repository repo = FileRepositoryBuilder.create(new File(localRepo, ".git"));
			git = Git.wrap(repo);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Copies all files under assets/site into the target local repo path
	 * @param context
	 */
	private void copyAssets(String path, Context context)
	{
		try
		{
			AssetManager assetManager = context.getAssets();
			String[] files = assetManager.list(path);

			for (String filename : files)
			{
				if (assetManager.list(path + "/" + filename).length == 0)
				{
					InputStream in = assetManager.open(path + "/" + filename);
					OutputStream out = new FileOutputStream(new File(localRepo.getParent(), (path + "/" + filename).replaceAll("~", "_")));

					FileManager.getInstance().copyFile(in, out);

					in.close();
					out.flush();
					out.close();
				}
				else
				{
					new File(localRepo.getParent(), (path + "/" + filename).replaceAll("~", "_")).mkdirs();
					copyAssets(path + "/" + filename, context);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean hasChangesToPush()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		return prefs.getBoolean("commits", false) && !TextUtils.isEmpty(prefs.getString("git_url", ""));
	}

	/**
	 * Commits all files on the working branch
	 */
	public void commitChanges()
	{
		try
		{
			Status status = git.status().call();
			if ((status.hasUncommittedChanges() || !status.isClean())
			&& (status.getAdded().size() > 0 || status.getRemoved().size() > 0 || status.getModified().size() > 0 || status.getUntracked().size() > 0))
			{

				PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("commits", true).apply();

				git.add()
					.addFilepattern(".")
					.call();

				final Handler mainHandler = new Handler(context.getMainLooper());
				final Runnable callback = new Runnable()
				{
					@Override public void run()
					{
						BusHelper.getInstance().post(new NewCommitEvent());
					}
				};

				new Thread(new Runnable()
				{
					@Override public void run()
					{
						SharedPreferences prefs = getDefaultSharedPreferences(context);
						String username = prefs.getString("committer_name", "");
						String email = prefs.getString("committer_email", "");

						try
						{
							git.commit()
								.setAuthor(username, email)
								.setMessage("Commit " + new Date().toString())
								.call();

							mainHandler.post(callback);
						}
						catch (GitAPIException e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Pushes the current repo to its remote path
	 */
	public void pushChanges(final Runnable callback)
	{
		new Thread(new Runnable()
		{
			@Override public void run()
			{
				try
				{
					// make sure everything is committed
					commitChanges();

					SharedPreferences prefs = getDefaultSharedPreferences(context);
					String remotePath = prefs.getString("git_url", "");
					String username = prefs.getString("git_username", "");
					String password = prefs.getString("git_password", "");

					git.push()
						.setRemote(remotePath)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
						.setPushAll()
						.setForce(true)
						.call();

					PreferenceManager.getDefaultSharedPreferences(context).edit().remove("commits").apply();

					if (callback != null)
					{
						callback.run();
					}
				}
				catch (GitAPIException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
}
