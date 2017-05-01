package me.anon.growjournal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

import me.anon.growjournal.MainApplication;
import me.anon.growjournal.R;
import me.anon.growjournal.manager.FileManager;

import static java.util.Collections.singleton;
import static me.anon.growjournal.R.id.pull;

/**
 * // TODO: Add class description
 */
public class SetupActivity extends AppCompatActivity
{
	private EditText remoteUrl;
	private EditText remoteUsername;
	private EditText remotePassword;
	private EditText remoteBranch;
	private ProgressBar progress;

	private Git git;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setup_view);
		setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
		getSupportActionBar().setTitle("Setup");

		remoteUrl = (EditText)findViewById(R.id.remote_url);
		remoteUsername = (EditText)findViewById(R.id.remote_username);
		remotePassword = (EditText)findViewById(R.id.remote_password);
		remoteBranch = (EditText)findViewById(R.id.remote_branch);
		progress = (ProgressBar)findViewById(R.id.progress);

		findViewById(pull).setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				v.setEnabled(false);
				pull();
			}
		});
	}

	@Override public void onBackPressed()
	{
		new AlertDialog.Builder(this)
			.setTitle("Are you sure?")
			.setMessage("Cancel the import? A new empty repository will be created instead")
			.setPositiveButton("Exit", new DialogInterface.OnClickListener()
			{
				@Override public void onClick(DialogInterface dialog, int which)
				{
					PreferenceManager.getDefaultSharedPreferences(SetupActivity.this).edit()
						.putBoolean("setup", true)
						.apply();
					
					FileManager.getInstance().deleteRecursive(new File(getFilesDir() + "/site/"));
					((MainApplication)getApplication()).initialise();

					Intent main = new Intent(SetupActivity.this, MainActivity.class);
					main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(main);
					finish();
				}
			})
			.setNegativeButton("Cancel", null)
			.show();
	}

	private boolean integrityCheck()
	{
		File siteFolder = new File(getFilesDir() + "/site/");

		if (siteFolder.list().length == 0) return false;
		if (!new File(siteFolder, "_config.yml").exists()) return false;

		new File(siteFolder, "/_pages/").mkdirs();
		new File(siteFolder, "/_posts/").mkdirs();

		return true;
	}

	private void pull()
	{
		FileManager.getInstance().deleteRecursive(new File(getFilesDir() + "/site/"));
		progress.setVisibility(View.VISIBLE);

		new Thread(new Runnable()
		{
			@Override public void run()
			{
				try
				{
					Git git = Git.cloneRepository()
						.setURI(remoteUrl.getText().toString())
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(remoteUsername.getText().toString(), remotePassword.getText().toString()))
						.setBranch(remoteBranch.getText().toString())
						.setBranchesToClone(singleton("refs/heads/" + remoteBranch.getText().toString()))
  						.setBranch("refs/heads/" + remoteBranch.getText().toString())
						.setDirectory(new File(getFilesDir() + "/site/"))
						.call();

					runOnUiThread(new Runnable()
					{
						@Override public void run()
						{
							if (integrityCheck())
							{
								PreferenceManager.getDefaultSharedPreferences(SetupActivity.this).edit()
									.putString("git_url", remoteUrl.getText().toString())
									.putString("git_username", remoteUsername.getText().toString())
									.putString("git_password", remotePassword.getText().toString())
									.putBoolean("setup", true)
									.apply();

								((MainApplication)getApplication()).initialise();

								Intent main = new Intent(SetupActivity.this, MainActivity.class);
								main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
								startActivity(main);
								finish();

								Toast.makeText(SetupActivity.this, "Repository successfully cloned", Toast.LENGTH_LONG).show();
							}
							else
							{
								Toast.makeText(SetupActivity.this, "Your repository did not look valid", Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				catch (GitAPIException e)
				{
					e.printStackTrace();

					runOnUiThread(new Runnable()
					{
						@Override public void run()
						{
							findViewById(R.id.pull).setEnabled(true);
							progress.setVisibility(View.GONE);
							Toast.makeText(SetupActivity.this, "There was an error fetching your repository", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}
}
