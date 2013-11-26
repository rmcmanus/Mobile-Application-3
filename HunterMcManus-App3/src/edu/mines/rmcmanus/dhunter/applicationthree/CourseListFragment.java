/**
 * Description: This activity is used to show all of the saved courses relating
 * to the current users selected semester. The functionality for this submission has not 
 * yet been implemented. The list is filled with dummy data.  This is the left fragment on
 * a tablet
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * A list fragment representing a list of Courses. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link CourseDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class CourseListFragment extends ListFragment {

	public String[] courseArray;
	//	public ArrayList<Semester> courseArrayList;
	public ArrayList<Course> courseArrayList;
	public List<ParseObject> coursesList;
	public ListView courseListView;
	public String semesterID;
	public boolean noCourses = false;
	public DeleteWarning deleteWarning;
	public int deleteIndex;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String courseName, boolean hasCourses, String semesterID);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String courseName, boolean hasCourses, String semesterID) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CourseListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		Toast.makeText(getActivity(), getActivity().getIntent().getStringExtra(SemesterActivity.EXTRA_SEMESTER_ID), Toast.LENGTH_SHORT).show();
		//		String[] courses = new String[] {"Course 1", "Course 2", "Course 3"};
		//		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, android.R.id.text1, courses));
		semesterID = getActivity().getIntent().getStringExtra(SemesterActivity.EXTRA_SEMESTER_ID);
		getList();
		deleteWarning = new DeleteWarning(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		getList();
	}

	public void getList() {
		if (semesterID == null) {
			semesterID = getActivity().getIntent().getStringExtra(CourseDetailActivity.EXTRA_SEMESTER_ID);
		}
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
		query.whereEqualTo("semester", semesterID);
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> courseList, ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + courseList.size() + " courses");
					courseArray = new String[courseList.size()];
					courseArrayList = new ArrayList<Course>();
					coursesList = courseList;
					String courseName, objectID;
					if (courseList.size() == 0) {
						courseArray = new String[1];
						courseArray[0] = getString(R.string.noCourse);
						noCourses = true;
					}
					for (int i = 0; i < courseList.size(); ++i) {
						courseName = courseList.get(i).getString("name");
						objectID = courseList.get(i).getObjectId();
						courseArrayList.add(new Course(courseName, objectID));
						courseArray[i] = courseName;
					}
					ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_list2, courseArray);
					setListAdapter(arrayAdapter);
					getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
							String courseID = courseArrayList.get(position).objectID;
							Log.d("Object ID", courseID);
							deleteIndex = position;
							deleteWarning.show();
							deleteWarning.setOnDismissListener(new OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialog) {
									if (deleteWarning.proceed) {
										coursesList.get(deleteIndex).deleteInBackground(new DeleteCallback() {

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
							//							ParseObject.createWithoutData("Course", courseID).deleteInBackground(new DeleteCallback() {
							//								
							//								@Override
							//								public void done(ParseException e) {
							//									if (e == null) {
							//										getList();
							//									}
							//								}
							//							});
							return true;
						}
					});
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});	
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		String courseName = listView.getItemAtPosition(position).toString();
		mCallbacks.onItemSelected(courseName, noCourses, semesterID);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
