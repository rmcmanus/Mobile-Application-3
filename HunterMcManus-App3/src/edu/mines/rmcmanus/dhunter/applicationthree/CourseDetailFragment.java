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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a single Course detail screen. This fragment is
 * either contained in a {@link CourseListActivity} in two-pane mode (on
 * tablets) or a {@link CourseDetailActivity} on hand sets.
 */
public class CourseDetailFragment extends Fragment {
	
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
		
		String[] dummyValues = new String[] {"Assignment 1", "Assignment 2", "Assignment 3"};
		assignmentList = new ArrayList<String>();
		
		for (String s : dummyValues)
			assignmentList.add(s);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_course_detail, container, false);
		
		ListView list = (ListView) rootView.findViewById(R.id.assignmentsListView);
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, assignmentList);
		list.setAdapter(arrayAdapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//		    	  Toast.makeText(getActivity(), "This functionality is not available yet", Toast.LENGTH_SHORT).show();
		    	 showAssignment();
		      }

		});

		return rootView;
	}
	
	public void showAssignment() {
		 Intent intent = new Intent(getActivity(), ShowAssignmentActivity.class);
		 startActivity(intent);
	}
}
