package me.anon.growjournal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
		setTitle("Setup");

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
							((MainApplication)getApplication()).initialise();

							Intent main = new Intent(SetupActivity.this, MainActivity.class);
							main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(main);
							finish();

							Toast.makeText(SetupActivity.this, "Repository successfully cloned", Toast.LENGTH_LONG).show();
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
