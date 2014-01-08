package com.play.twitterlive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RecentTweets extends Activity {

	ListView tweets;
	TextView tweet1, date1, tweet2, date2, tweet3, date3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.recent_tweets_manual);

		//ArrayList<Employee> rTweets = new ArrayList<Employee>();
		

		int mark = MainActivity.clickNumber * 7;
		//int limit = mark + 7;
		System.out.println("clicknumber: " + MainActivity.clickNumber);
		System.out.println("mark: " + mark);
//		int counter = 0;
//		while (counter < 6) {
//			rTweets.add(new Employee(MainActivity.Results.get(++mark),
//					MainActivity.Results.get(++mark)));
//			System.out.println("Rtweets size " + rTweets.size());
//			counter = counter + 2;
//		}
//		
//		for(int i = 0; i < rTweets.size(); i++){
//		System.out.println(rTweets.get(i).getName());
//		System.out.println(rTweets.get(i).getTitle());
//		}
		
		initialize();
		
		set(mark);
		
		

//		Employee[] employees = new Employee[rTweets.size()];
//		employees = rTweets.toArray(employees);
//
//		tweets = (ListView) findViewById(R.id.lvTweets);
//		tweets.setAdapter(new EmployeeArrayAdapter(this, employees));

	}
	
	private void initialize(){
		tweet1 = (TextView) findViewById(R.id.textView1);
		date1 = (TextView) findViewById(R.id.textView2);
		tweet2 = (TextView) findViewById(R.id.textView3);
		date2 = (TextView) findViewById(R.id.textView4);
		tweet3 = (TextView) findViewById(R.id.textView5);
		date3 = (TextView) findViewById(R.id.textView6);
	}
	
	private void set(int mark){
		tweet1.setText(MainActivity.Results.get(++mark));
		date1.setText(MainActivity.Results.get(++mark));
		tweet2.setText(MainActivity.Results.get(++mark));
		date2.setText(MainActivity.Results.get(++mark));
		tweet3.setText(MainActivity.Results.get(++mark));
		date3.setText(MainActivity.Results.get(++mark));
	}
	
	

}
