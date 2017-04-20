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

import lombok.Getter;

/**
 * // TODO: Add class description
 */
public class GitManager
{
	private static GitManager instance = null;

	@Getter private File localRepo;
	@Getter private Git git;

	public static GitManager getInstance()
	{
		if (instance == null)
		{
			synchronized (instance)
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
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		localRepo = new File(context.getFilesDir() + "/site/");

		if (!localRepo.exists())
		{
			createNewRepo();
			copyAssets("site/", context);
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
				if (assetManager.list(filename).length == 0)
				{
					InputStream in = assetManager.open(filename);

					File outFile = new File(localRepo, filename);
					OutputStream out = new FileOutputStream(outFile);

					copyFile(in, out);

					in.close();
					out.flush();
					out.close();
				}
				else
				{
					copyAssets(path + filename + "/", context);
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
	 * Pushes the current repo to its remote path
	 */
	public void pushChanges()
	{
		new Thread(new Runnable()
		{
			@Override public void run()
			{
				try
				{
					String remotePath = "";
					String username = "";
					String password = "";

					git.push()
						.setRemote(remotePath)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
						.setPushAll()
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
