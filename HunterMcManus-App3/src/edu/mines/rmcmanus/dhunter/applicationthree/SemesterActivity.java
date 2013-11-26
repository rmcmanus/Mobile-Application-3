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
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SemesterActivity extends Activity {
	public Intent intent;
	public boolean isGuest;
	public LinearLayout pbl;
	public String[] semesterArray;
	public List<ParseObject> semestersList;
	public ArrayList<Semester> semesterArrayList;
	public ListView semesterListView;
	public String semesterID;
	public final static String EXTRA_SEMESTER_ID = "edu.mines.rmcmanus.dhunter.applicationthree.SEMESTERID";
	public DeleteWarning deleteWarning;
	public int deleteIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);
		// Show the Up button in the action bar.
		setupActionBar();

		//Gets weather the user signed in as a guest or not
		isGuest = getIntent().getBooleanExtra(MainActivity.EXTRA_GUEST, false);

		intent = new Intent(this, CourseListActivity.class);
		getList();
		deleteWarning = new DeleteWarning(this);
	}

	/**
	 * This function starts the next activity based on which semester was clicked on in 
	 * the list
	 */
	public void pickCourse() {
		intent.putExtra(EXTRA_SEMESTER_ID, semesterID);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		getList();
	}

	public void getList() {
		pbl = (LinearLayout) findViewById(R.id.progressView);
		pbl.setVisibility(View.VISIBLE);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Semester");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.orderByAscending("semester_year");
		query.addAscendingOrder("semester_type");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> semesterList, ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + semesterList.size() + " scores");
					semestersList = semesterList;
					semesterArray = new String[semesterList.size()];
					semesterArrayList = new ArrayList<Semester>();
					String semesterType;
					String semesterYear;
					String objectId;
					if (semesterList.size() == 0) {
						semesterArray = new String[1];
						semesterArray[0] = getString(R.string.noSemester);
					}
					for (int i = 0; i < semesterList.size(); ++i) {
						semesterType = semesterList.get(i).getString("semester_type");
						semesterYear = semesterList.get(i).getString("semester_year");
						objectId = semesterList.get(i).getObjectId();
						semesterArrayList.add(new Semester(semesterType, semesterYear, objectId));
						semesterArray[i] = semesterType + " " + semesterYear;
					}
					ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.custom_list, semesterArray);
					semesterListView = (ListView) findViewById(R.id.semesterlist);
					semesterListView.setAdapter(arrayAdapter);
					if (semesterList.size() != 0) {
						semesterListView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
								semesterID = semesterArrayList.get(position).objectId;
								pickCourse();
							}
						});
						semesterListView.setLongClickable(true);
						semesterListView.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
								semesterID = semesterArrayList.get(position).objectId;
								deleteIndex = position;
								deleteWarning.show();
								deleteWarning.setOnDismissListener(new OnDismissListener() {
									
									@Override
									public void onDismiss(DialogInterface dialog) {
										if (deleteWarning.proceed) {
											semestersList.get(deleteIndex).deleteInBackground(new DeleteCallback() {
												@Override
												public void done(ParseException e) {
													if (e == null) {
														getList();
													}
												}
											});
										}	
									}
								});
//								ParseObject.createWithoutData("Semester", semesterID).deleteInBackground(new DeleteCallback() {
//									
//									@Override
//									public void done(ParseException e) {
//										if (e == null) {
//											getList();
//										}
//									}
//								});
								return true;
							}
						});
					}
					pbl.setVisibility(View.INVISIBLE);
				} else {
					Log.d("score", "Error: " + e.getMessage());
					if (e.getCode() == ParseException.CONNECTION_FAILED) {
						Toast.makeText(getBaseContext(), getString(R.string.connectivityError), Toast.LENGTH_LONG).show();
					}
				}
			}
		});	
	}

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
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
