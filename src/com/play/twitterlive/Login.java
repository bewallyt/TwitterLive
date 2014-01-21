package com.play.twitterlive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Login extends Activity {

	public static long userID;

	static String TWITTER_CONSUMER_KEY = "WONUBTciNbYF9IysVL1bw";
	static String TWITTER_CONSUMER_SECRET = "pTjcUTi7OBckXbmsSXHtNKwIKKLeqTWL1PCKcvKAwTk";

	public static SharedPreferences mSharedPreferences;

	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	private static RequestToken requestToken;
	private static Twitter twitter;

	Button btnLoginTwitter, btnLogoutTwitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Might be here
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);

		btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				logoutFromTwitter();
			}
		});

		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!isTwitterLoggedInAlready()) {
					loginToTwitter();
				} else {
					Intent startMainActivity = new Intent(
							getApplicationContext(), MainActivity.class);
					startActivity(startMainActivity);
				}

			}
		});

		twitLive();

	}

	private void twitLive() {
		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
				// oAuth verifier
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					// Get the access token
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					userID = accessToken.getUserId();

					// Shared Preferences
					SharedPreferences.Editor e = mSharedPreferences.edit();

					// After getting access token, access token secret
					// store them in application preferences
					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
					// Store login status - true
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

					System.out.print("mSharedPreferences contains token:");
					System.out.println(mSharedPreferences
							.contains(PREF_KEY_OAUTH_TOKEN));
					System.out
							.print("mSharedPreferences contains secret token:");
					System.out.println(mSharedPreferences
							.contains(PREF_KEY_OAUTH_SECRET));

					System.out.println("This is the onCreate");

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		} else {
			Intent startMainActivity = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(startMainActivity);
			finish();
		}
	}

	// need to move shit off the main thread, keep these two lines for testing
	// on 4.0 devices
	// StrictMode.ThreadPolicy policy = new
	// StrictMode.ThreadPolicy.Builder().permitAll().build();
	// StrictMode.setThreadPolicy(policy);

	private void loginToTwitter() {
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			// user already logged into twitter
			Toast.makeText(getApplicationContext(),
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initialize() {
		setContentView(R.layout.login);
		btnLogoutTwitter = (Button) findViewById(R.id.button2);
		btnLoginTwitter = (Button) findViewById(R.id.button);
	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isTwitterLoggedInAlready()) {
			Intent startMainActivity = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(startMainActivity);
		}
	}

	private void logoutFromTwitter() {

		Toast.makeText(getApplicationContext(),
				"Already logged out of Twitter", Toast.LENGTH_LONG).show();
		// Clear the shared preferences
		SharedPreferences.Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

}
