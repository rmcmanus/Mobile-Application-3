/**
 * Description: This activity is used to add a new Assignment to a course.  The functionality
 * for this submission has not yet been implemented.  In the next submission the assignment
 * will be inserted into the database.
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
		
		courseID = getIntent().getStringExtra(CourseDetailActivity.EXTRA_COURSE_ID);
		if (courseID == null) {
			courseID = getIntent().getStringExtra(CourseListActivity.EXTRA_COURSE_ID);
		}
		
		setContentView(R.layout.activity_add_assignment);
		
		Calendar c = Calendar.getInstance();
		dateDue = (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
		
		//Populate the spinner with priorities high, medium, low
		Spinner prioritySpinner = (Spinner) findViewById(R.id.assignmentPrioritySpinner);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.priorityArray, R.layout.spinner_layout);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prioritySpinner.setAdapter(arrayAdapter);
		
		//Populate the spinner with assignment types that were set when the course was added
		
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
		
		((CalendarView) findViewById(R.id.calendarView1)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				dateDue = (month + 1) + "/" + dayOfMonth + "/" + year;
			}
		});
	}
	
	public void addAssignment(View view) {
		if (assignmentName.getText().toString().equals("")) {
			Toast.makeText(this, getString(R.string.assignmentNameError), Toast.LENGTH_SHORT).show();
		}
		else {
			if (pointValue.getText().toString().equals("")) {
				Toast.makeText(this, getString(R.string.assignmentPointsError), Toast.LENGTH_SHORT).show();
			}
			else {
				ParseObject newAssignment = new ParseObject("Assignments");
				newAssignment.put("user", ParseUser.getCurrentUser());
				newAssignment.put("assignmentName", assignmentName.getText().toString());
				newAssignment.put("pointValue", pointValue.getText().toString());
				newAssignment.put("assignmentPriority", assignmentPriority);
				newAssignment.put("assignmentType", assignmentType);
				newAssignment.put("dateDue", dateDue);
				newAssignment.put("course", courseID);
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
