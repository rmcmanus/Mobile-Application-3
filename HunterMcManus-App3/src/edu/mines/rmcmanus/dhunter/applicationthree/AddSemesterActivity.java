/**
 * Description: This activity is used to add a new Semester.  The functionality
 * for this submission has not yet been implemented.  In the next submission the semester
 * will be inserted into the database.
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
public class AddSemesterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_semester);
	}
	
	public void addSemester(View v) {
		EditText otherField = (EditText) findViewById(R.id.othersemester);
		EditText yearField = (EditText) findViewById(R.id.semesteryear);
		if (otherField.getText().toString().equals("")) {
			if (yearField.getText().toString().equals("")) {
				Toast.makeText(this, getString(R.string.yearError), Toast.LENGTH_SHORT).show();
			} else {
				RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
				int selected = rg.getCheckedRadioButtonId();
				RadioButton selectedButton = (RadioButton) findViewById(selected);
				int radioId = rg.indexOfChild(selectedButton);
				RadioButton rb = (RadioButton) rg.getChildAt(radioId);
				String semesterType = rb.getText().toString();
				ParseObject semester = new ParseObject("Semester");
				semester.put("semester_type", semesterType);
				semester.put("semester_year", yearField.getText().toString());
				semester.put("user", ParseUser.getCurrentUser());
				semester.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						finish();
					}
				});
			}
		}
		else {
			if (yearField.getText().toString().equals("")) {
				Toast.makeText(this, getString(R.string.yearError), Toast.LENGTH_SHORT).show();
			} else {
				ParseObject semester = new ParseObject("Semester");
				semester.put("semester_type", otherField.getText().toString());
				semester.put("semester_year", yearField.getText().toString());
				semester.put("user", ParseUser.getCurrentUser());
				semester.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						finish();
					}
				});
			}
		}
	}
}
