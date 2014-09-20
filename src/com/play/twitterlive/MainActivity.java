package com.play.twitterlive;

import android.widget.EditText;
import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Global;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import twitter4j.PagableResponseList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity implements OnClickListener {

	static String TWITTER_CONSUMER_KEY = "WONUBTciNbYF9IysVL1bw";
	static String TWITTER_CONSUMER_SECRET = "pTjcUTi7OBckXbmsSXHtNKwIKKLeqTWL1PCKcvKAwTk";

	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	private static RequestToken requestToken;

	Button press, btnLogoutTwitter;
	TextView thewho, results;
	EditText targetSN;
	ConfigurationBuilder cb;
	TwitterFactory tf;
	Twitter twitter;

	ProgressBar myProgressBar;
	int myProgress = 0;

	ListView liveFeed;

	public static ArrayList<String> Results = new ArrayList<String>();
	public static int clickNumber;


	ArrayList<Drawable> images = new ArrayList<Drawable>();
	List<RowItem> rowItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		initialize();

		// ////////////// LIST v1

//		final StableArrayAdapter adapter = new StableArrayAdapter(this,
//				android.R.layout.simple_list_item_1, fruits);
//		liveFeed.setAdapter(adapter);
//
//		liveFeed.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// Toast.makeText(getApplicationContext(),
//				// "Click ListItem Number " + position, Toast.LENGTH_LONG)
//				// .show();
//				try {
//					@SuppressWarnings("rawtypes")
//					Class ourClass = Class
//							.forName("com.play.twitterlive.RecentTweets"); // Starts
//																			// when
//																			// clicked.
//					// Opens path to whatever list item was clicked
//					Intent ourIntent = new Intent(MainActivity.this, ourClass);
//					startActivity(ourIntent);
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace(); // For Debugging
//				}
//
//			}
//		});

		// /////////// END
		//////////// LIST 2
		
       
 
		// Always have this

		btnLogoutTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				logoutFromTwitter();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void initialize() {
		setContentView(R.layout.activity_main);
		press = (Button) findViewById(R.id.button1);
		thewho = (TextView) findViewById(R.id.tvWho);
		targetSN = (EditText) findViewById(R.id.etSN);
		results = (TextView) findViewById(R.id.etResults);
		myProgressBar = (ProgressBar) findViewById(R.id.progressbar_default);
		myProgressBar.setVisibility(100);
		press.setOnClickListener(this);
		btnLogoutTwitter = (Button) findViewById(R.id.button2);
		liveFeed = (ListView) findViewById(R.id.listView1);

	}

	public void authenticate() {

		String TWITTER_ACCESS_TOKEN = Login.mSharedPreferences.getString(
				PREF_KEY_OAUTH_TOKEN, "oauth_token");
		String TWITTER_ACCESS_TOKEN_SECRET = Login.mSharedPreferences
				.getString(PREF_KEY_OAUTH_SECRET, "oauth_token_secret");

		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
				.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
				.setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);

		tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	@SuppressWarnings("deprecation")
	public static Drawable drawableFromUrl(String url) throws IOException {
	    Bitmap x;

	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    connection.connect();
	    InputStream input = connection.getInputStream();

	    x = BitmapFactory.decodeStream(input);
	    return new BitmapDrawable(x);
	}

	public void getTweetTimes(long user, String username,
			ArrayList<String> Results) {

		try {
			Paging page1 = new Paging(1, 3);
			ArrayList<Status> timeline = new ArrayList<Status>(
					twitter.getUserTimeline(user, page1));
			if (timeline.size() > 2) {
				double time1 = timeline.get(0).getCreatedAt().getTime();
				double time2 = timeline.get(1).getCreatedAt().getTime();
				double time3 = timeline.get(2).getCreatedAt().getTime();

				double tweetsPERminute1 = 2.0 / ((time1 - time2) / 60000.0);
				double tweetsPERminute2 = 0;

				if (tweetsPERminute1 >= .3) {

					tweetsPERminute2 = 2.0 / ((time2 - time3) / 60000.0);

				}

				// Original Time: 1 minute
				// Original Time with caching on phone: 41 seconds
				// With If statement: 36 seconds
				// With If statement and paging: 35 seconds, 40

				System.out.println(username);
				// System.out.println(twitter.getUserTimeline(user, page1));

				// System.out.println(twitter.getUserTimeline(user).get(0).getText());
				// System.out.println(twitter.getUserTimeline(user).get(1).getText());
				// System.out.println(twitter.getUserTimeline(user).get(2).getText());

				// If within an hour...
				if ((tweetsPERminute1 >= .3) && (tweetsPERminute2 >= .3)) {
					System.out.println("Added" + username);
					
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					
					Date one = timeline.get(0).getCreatedAt();      
					String reportDateOne = df.format(one);
					Date two = timeline.get(1).getCreatedAt();      
					String reportDateTwo = df.format(two);
					Date three = timeline.get(2).getCreatedAt();      
					String reportDateThree = df.format(three);
					
					Results.add(username);
					Results.add(timeline.get(0).getText());
					Results.add(reportDateOne);
					Results.add(timeline.get(1).getText());
					Results.add(reportDateTwo);
					Results.add(timeline.get(2).getText());
					Results.add(reportDateThree);
					

					// Get the date today using Calendar object.

					
					// Get photo
					User user2 = twitter.showUser((int) user);
					String urlString = user2.getProfileImageURL();
					try {
						images.add(drawableFromUrl(urlString));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
						images.add(d);
						
						
						
					}
					
					if((images.size() * 7) < Results.size()){
						Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
						images.add(d);
					}
					
//					

				}
			}

			// 30 seconds

			else {

			}

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		}

	}

	public void generateFriendList(ArrayList<String> Results) {

		try {
			// PagableResponseList<User> friends;
			// int cursor = -1;
			// while ((cursor = ids.getNextCursor()) != 0) {
			// friends = twitter.getFriendsList(Login.userID, cursor);
			//
			// for (int i = 0; i < friends.size(); i++) {
			// getTweetTimes(friends.get(i).getId(), friends.get(i)
			// .getName(), Results);
			// }
			// }

			long lCursor = -1;
			IDs friendsIDs = twitter.getFriendsIDs(Login.userID, lCursor);
			System.out.println(twitter.showUser(Login.userID).getName());
			do {
				for (long i : friendsIDs.getIDs()) {
					// System.out.println("follower ID #" + i);
					// System.out.println(twitter.showUser(i).getName());

					getTweetTimes(i, twitter.showUser(i).getName(), Results);
				}
			} while (friendsIDs.hasNext());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class FetchTwitter extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			// String name = targetSN.getText().toString();
			// getTweetTimes(name);
			
			generateFriendList(Results);

			return Results;
		}

		protected void onPostExecute(ArrayList<String> Results) {

			myProgressBar.setVisibility(100);

//			for (int i = 0; i < Results.size(); i++) {
//				// Prints only one result
//				System.out.println(Results.get(i));
//			}
				// BEGIN LIST GENERATION
				
				 rowItems = new ArrayList<RowItem>();
				 int i = 0;
				 System.out.println("images.size() is " + images.size());
			        for (int j = 0; j < images.size(); j++) {
			        	System.out.println("i is: " + i);
			        	System.out.println("Results.size() is: " + Results.size());
			        	System.out.println("Results.get(i): " + Results.get(i));
			            RowItem item = new RowItem(images.get(j), Results.get(i));
			            rowItems.add(item);
			            i = i + 7;
			        }
			 
			        CustomListViewAdapter adapter2 = new CustomListViewAdapter(MainActivity.this,
			                R.layout.single_row, rowItems);
			        liveFeed.setAdapter(adapter2);
			        
					liveFeed.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// Toast.makeText(getApplicationContext(),
							// "Click ListItem Number " + position, Toast.LENGTH_LONG)
							// .show();
							clickNumber = position;
							try {
								@SuppressWarnings("rawtypes")
								Class ourClass = Class
										.forName("com.play.twitterlive.RecentTweets"); // Starts
																						// when
																						// clicked.
								// Opens path to whatever list item was clicked
								Intent ourIntent = new Intent(MainActivity.this, ourClass);
								startActivity(ourIntent);
							} catch (ClassNotFoundException e) {
								e.printStackTrace(); // For Debugging
							}

						}
					});

					/////// END LIST GENERATION

			

			if (Results.size() == 0)
				results.setText("No one is live tweeting");

			System.out.println(Results.size());
		}


	}

	@Override
	public void onClick(View arg0) {

		myProgressBar.setVisibility(0);
		authenticate();
		FetchTwitter runner = new FetchTwitter();
		runner.execute();
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		authenticate();
		super.onResume();
	}

	private void logoutFromTwitter() {
		// Clear the shared preferences
		SharedPreferences.Editor e = Login.mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();

		String TWITTER_ACCESS_TOKEN = Login.mSharedPreferences.getString(
				PREF_KEY_OAUTH_TOKEN, "oauth_token");
		String TWITTER_ACCESS_TOKEN_SECRET = Login.mSharedPreferences
				.getString(PREF_KEY_OAUTH_SECRET, "oauth_token_secret");
		Intent intent = new Intent(getApplicationContext(), Login.class);
		startActivity(intent);
		finish();
	}

}
