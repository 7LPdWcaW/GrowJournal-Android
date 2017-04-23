package me.anon.growjournal.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import org.eclipse.jgit.api.Git;
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

					copyFile(in, out);

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

	/**
	 * Copies a file from src to dest
	 * @param src The source input stream
	 * @param dest The dest output stream
	 * @throws IOException
	 */
	private void copyFile(InputStream src, OutputStream dest) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read;

		while ((read = src.read(buffer)) != -1)
		{
			dest.write(buffer, 0, read);
		}
	}

	/**
	 * Commits all files on the working branch
	 */
	public void commitChanges()
	{
		try
		{
			if (git.status().call().hasUncommittedChanges() || !git.status().call().isClean())
			{
				git.add()
					.addFilepattern(".")
					.call();

				new Thread(new Runnable()
				{
					@Override public void run()
					{
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
						String username = prefs.getString("committer_name", "");
						String email = prefs.getString("committer_email", "");

						try
						{
							git.commit()
								.setAuthor(username, email)
								.setMessage("Commit " + new Date().toString())
								.call();
						}
						catch (GitAPIException e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
		catch (GitAPIException e)
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
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
					String remotePath = prefs.getString("git_url", "");
					String username = prefs.getString("git_username", "");
					String password = prefs.getString("git_password", "");

					git.push()
						.setRemote(remotePath)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
						.setPushAll()
						.setForce(true)
						.call();

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
