/**
 * Description: This activity is used to show all of the saved semester relating
 * to the current user. The functionality for this submission has not 
 * yet been implemented. The list is filled with dummy data.  For the final submission
 * the data will be pulled from the database
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SemesterActivity extends Activity {
	public Intent intent;
	public boolean isGuest;
	public LinearLayout pbl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Gets weather the user signed in as a guest or not
		isGuest = getIntent().getBooleanExtra(MainActivity.EXTRA_GUEST, false);
		
		intent = new Intent(this, CourseListActivity.class);
		pbl = (LinearLayout) findViewById(R.id.progressView);
		pbl.setVisibility(View.VISIBLE);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Semester");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> semesterList, ParseException e) {
				if (e == null) {
		            Log.d("score", "Retrieved " + semesterList.size() + " scores");
		            String[] semesterArray = new String[semesterList.size()];
		            ArrayList<Semester> semesterArrayList = new ArrayList<Semester>();
		            String semesterType;
		            String semesterYear;
		            for (int i = 0; i < semesterList.size(); ++i) {
		            	semesterType = semesterList.get(i).getString("semester_type");
		            	semesterYear = semesterList.get(i).getString("semester_year");
		            	semesterArrayList.add(new Semester(semesterType, semesterYear));
		            	semesterArray[i] = semesterType + " " + semesterYear;
		            }
		            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, semesterArray);
		            ListView semesterListView = (ListView) findViewById(R.id.semesterlist);
		    		semesterListView.setAdapter(arrayAdapter);
		    		semesterListView.setOnItemClickListener(new OnItemClickListener() {
		    			@Override
		    			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    				pickCourse();
		    			}
		    		});
		    		pbl.setVisibility(View.INVISIBLE);
				} else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
			}
		});	
	}
	
	/**
	 * This function starts the next activity based on which semester was clicked on in 
	 * the list
	 */
	public void pickCourse() {
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
		//If the user is a guest don't show the logout button
		if (!isGuest) {
			getMenuInflater().inflate(R.menu.semester, menu);
		} else {
			getMenuInflater().inflate(R.menu.semester_nosignout, menu);
		}
		return true;
	}

	//Does actions based on which item was clicked in the context menu
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
		case R.id.addSemesterContext:
			Intent intent = new Intent(this, AddSemesterActivity.class);
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
		case R.id.action_user:
			if (!isGuest) {
				ParseUser.logOut();
				Intent login = new Intent(this, MainActivity.class);
				startActivity(login);
				finish();
			}
//			ParseUser currentUser = ParseUser.getCurrentUser();
						
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
