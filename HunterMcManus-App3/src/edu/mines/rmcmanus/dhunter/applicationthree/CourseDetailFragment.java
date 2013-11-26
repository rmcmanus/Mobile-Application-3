/**
 * Description: This activity is used to show all of the saved assignments relating
 * to the current users selected course. The functionality for this submission has not 
 * yet been implemented. The list is filled with dummy data.  This is the right fragment on
 * a hand held
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A fragment representing a single Course detail screen. This fragment is
 * either contained in a {@link CourseListActivity} in two-pane mode (on
 * tablets) or a {@link CourseDetailActivity} on hand sets.
 */
public class CourseDetailFragment extends Fragment {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	ArrayList<String> assignmentObjects;
	String courseID;
	ArrayList<String> assignmentList;
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CourseDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		courseID = getArguments().getString(CourseListActivity.EXTRA_COURSE_ID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_course_detail, container, false);

		expListView = (ExpandableListView) rootView.findViewById(R.id.assignmentsListView);
		getAssignments();

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		getAssignments();
	}

	public void getAssignments() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Assignments");
		query.whereEqualTo("course", courseID);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> assignmentList, ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + assignmentList.size() + " assignments");

					listDataChild = new HashMap<String, List<String>>();
					listDataHeader = new ArrayList<String>();
					List<String> assignmentContents;
					
					assignmentObjects = new ArrayList<String>();
					for (int i = 0; i < assignmentList.size(); ++i) {
						assignmentContents = new ArrayList<String>();
						listDataHeader.add(assignmentList.get(i).getString("assignmentName"));
						assignmentContents.add(getString(R.string.appendDueDate) + " " + assignmentList.get(i).getString("dateDue"));
						assignmentContents.add(getString(R.string.appendPointValue) + " " + assignmentList.get(i).getString("pointValue"));
						assignmentContents.add(getString(R.string.appendPriority) + " " + assignmentList.get(i).getString("assignmentPriority"));
						assignmentContents.add(getString(R.string.appendType) + " " + assignmentList.get(i).getString("assignmentType"));
						
						assignmentObjects.add(assignmentList.get(i).getObjectId());
						listDataChild.put(listDataHeader.get(i), assignmentContents);
					}

					listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
					expListView.setAdapter(listAdapter);
					expListView.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
							String assignmentID = assignmentObjects.get(position);
							Log.d("Object ID", courseID);
							ParseObject.createWithoutData("Assignments", assignmentID).deleteInBackground(new DeleteCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e == null) {
										getAssignments();
									}
								}
							});
							return true;
						}
					});
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});	
	}

	public void showAssignment() {
		Intent intent = new Intent(getActivity(), ShowAssignmentActivity.class);
		startActivity(intent);
	}
}
