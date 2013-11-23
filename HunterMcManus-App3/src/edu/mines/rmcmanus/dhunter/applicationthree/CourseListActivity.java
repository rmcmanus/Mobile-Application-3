/**
 * Description: This activity is used to show all of the saved courses relating
 * to the current users selected semester. The functionality for this submission has not 
 * yet been implemented. The list is filled with dummy data.  This is the activity on
 * a hand held
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * An activity representing a list of Courses. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link CourseDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link CourseListFragment} and the item details (if present) is a
 * {@link CourseDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link CourseListFragment.Callbacks} interface to listen for item selections.
 */
public class CourseListActivity extends FragmentActivity implements
		CourseListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	public final static String EXTRA_SEMESTER_ID = "edu.mines.rmcmanus.dhunter.applicationthree.SEMESTERID";
	public final static String EXTRA_COURSE_ID = "edu.mines.rmcmanus.dhunter.applicationthree.COURSEID";
	public String semesterID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_list);
		
        semesterID = getIntent().getStringExtra(SemesterActivity.EXTRA_SEMESTER_ID);
		
		if (findViewById(R.id.course_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((CourseListFragment) getSupportFragmentManager().findFragmentById(
					R.id.course_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (mTwoPane)
			getMenuInflater().inflate(R.menu.two_pane_assignment, menu);
		else
			getMenuInflater().inflate(R.menu.course, menu);
		return true;
	}

	//Does an action based on the item selected in the Context menu
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
		case R.id.addAssignmentContext:
			Intent intent = new Intent(this, AddAssignmentActivity.class);
			startActivity(intent);
			return true;
		case R.id.addCourseContext:
			Intent addCourse = new Intent(this, AddCourseActivity.class);
			addCourse.putExtra(EXTRA_SEMESTER_ID, semesterID);
			startActivity(addCourse);
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

	/**
	 * Callback method from {@link CourseListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(CourseDetailFragment.ARG_ITEM_ID, id);
			CourseDetailFragment fragment = new CourseDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.course_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
//			TextView test = (TextView) findViewById(Integer.parseInt(id));
			Intent detailIntent = new Intent(this, CourseDetailActivity.class);
//			detailIntent.putExtra(EXTRA_COURSE_ID, test.getText().toString());
			detailIntent.putExtra(CourseDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
