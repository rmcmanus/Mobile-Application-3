package edu.mines.rmcmanus.dhunter.applicationthree;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class CourseActivity extends Activity {
	public Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		// Show the Up button in the action bar.
		setupActionBar();
		
		String[] courses = new String[2];
		courses[0] = "Course 1";
		courses[1] = "Course 2";
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,courses);
		ListView courseList = (ListView) findViewById(R.id.courselist);
		courseList.setAdapter(arrayAdapter);
		intent = new Intent(this, AssignmentActivity.class);
		courseList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//				songName = namesList[position];
//				Song song = new Song(songName);
//				chooseDifficulty(v);
				pickAssignment();
			}
		});
	}
	
	public void pickAssignment() {
		startActivity(intent);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.addCourseContext:
			Intent intent = new Intent(this, AddCourseActivity.class);
			startActivity(intent);
			return true;
		case R.id.setting:
			Intent setting = new Intent(this, SettingActivity.class);
			startActivity(setting);
			return true;
		case R.id.about:
			Intent about = new Intent(this, AboutActivity.class);
			startActivity(about);
			return true;
		case R.id.help:
			Intent help = new Intent(this, HelpActivity.class);
			startActivity(help);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}