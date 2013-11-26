/**
 * Description: This activity is used to add a new Assignment to a course.  This activity
 * gets the id of the course from the Course Detail activity or Course List activity based
 * on if the user is on a tablet or a hand-set.  The activity checks to see that all the
 * proper fields were filled out and update the database.
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
public class AddAssignmentActivity extends Activity {
	
	EditText assignmentName, pointValue;
	String assignmentPriority, assignmentType;
	String dateDue = "";
	String courseID;
	public String categoryArray[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Gets the course ID depending on which intent it came from
		courseID = getIntent().getStringExtra(CourseDetailActivity.EXTRA_COURSE_ID);
		if (courseID == null) {
			courseID = getIntent().getStringExtra(CourseListActivity.EXTRA_COURSE_ID);
		}
		
		setContentView(R.layout.activity_add_assignment);
		
		Calendar c = Calendar.getInstance();
		//Sets a default date in case the user doesn't change it via the listener
		dateDue = (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
		
		//Populate the spinner with priorities high, medium, low
		Spinner prioritySpinner = (Spinner) findViewById(R.id.assignmentPrioritySpinner);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.priorityArray, R.layout.spinner_layout);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prioritySpinner.setAdapter(arrayAdapter);
		
		//Populate the spinner with assignment types that were set when the course was added
		
		//Queries the parse database for the Category column and grabs the categories based on
		//the user's id and the course id
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.whereEqualTo("course", courseID);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> categoryList, ParseException e) {
				if (e == null) {
		            Log.d("score", "Retrieved " + categoryList.size() + " categories");
		            categoryArray = new String[categoryList.size()];
		            for (int i = 0; i < categoryList.size(); ++i) {
		            	categoryArray[i] = categoryList.get(i).getString("category_name");
		            }
		            //Populates the spinner based on the results of the query
		            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_layout, categoryArray);
		            Spinner typeSpinner = (Spinner) findViewById(R.id.assignmentTypeSpinner);
		    		courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    		typeSpinner.setAdapter(courseAdapter);
		    		typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    			@Override
		    			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		    				assignmentType = (String) parent.getItemAtPosition(position); 
		    			}
		    			@Override
		    			public void onNothingSelected(AdapterView<?> arg0) {
		    			}
		    		});
				} else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
			}
		});	
		
		//Sets a listener for the spinner to grab the priority the user picked
		prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				assignmentPriority = (String) parent.getItemAtPosition(position); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		assignmentName = (EditText) findViewById(R.id.addAssignmentName);
		pointValue = (EditText) findViewById(R.id.addAssignmentPointValue);
		
		//Finds the date that the user selected in the calendar view
		((CalendarView) findViewById(R.id.calendarView1)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				dateDue = (month + 1) + "/" + dayOfMonth + "/" + year;
			}
		});
	}
	
	/**
	 * This function is called when the user presses the submit button on the AddAssignmentActivity.
	 * This function checks to make user that the user filled out all of the appropriate fields, and if
	 * they did then the database is updated.
	 * 
	 * @param view The button that was pressed on the activity
	 */
	public void addAssignment(View view) {
		//Checks to see that the user entered an assignment name
		if (assignmentName.getText().toString().equals("")) {
			Toast.makeText(this, getString(R.string.assignmentNameError), Toast.LENGTH_SHORT).show();
		}
		else {
			//Checks to see that the uesr entered a point value for the assignment
			if (pointValue.getText().toString().equals("")) {
				Toast.makeText(this, getString(R.string.assignmentPointsError), Toast.LENGTH_SHORT).show();
			}
			else {
				//Inserts into the database all the information relating to the assignment
				ParseObject newAssignment = new ParseObject("Assignments");
				newAssignment.put("user", ParseUser.getCurrentUser());
				newAssignment.put("assignmentName", assignmentName.getText().toString());
				newAssignment.put("pointValue", pointValue.getText().toString());
				newAssignment.put("assignmentPriority", assignmentPriority);
				newAssignment.put("assignmentType", assignmentType);
				newAssignment.put("dateDue", dateDue);
				newAssignment.put("course", courseID);
				//Finishes the activity once the information has been saved
				newAssignment.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						finish();
					}
				});
			}
		}
	}
}
