/**
 * Description: This activity is used to add a new Assignment to a course.  The functionality
 * for this submission has not yet been implemented.  In the next submission the assignment
 * will be inserted into the database.
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.Activity;
public class AddAssignmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		//Populate the spinner with priorities high, medium, low
		Spinner prioritySpinner = (Spinner) findViewById(R.id.assignmentPrioritySpinner);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.priorityArray, android.R.layout.simple_spinner_dropdown_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prioritySpinner.setAdapter(arrayAdapter);
		
		//Populate the spinner with assignment types.  For the final submission it will
		//be populated based on information stored about the course in the database
		Spinner typeSpinner = (Spinner) findViewById(R.id.assignmentTypeSpinner);
		ArrayAdapter<CharSequence> arrayAdapter1 = ArrayAdapter.createFromResource(this, R.array.testTypesArray, android.R.layout.simple_spinner_dropdown_item);
		arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(arrayAdapter1);
	}
}
