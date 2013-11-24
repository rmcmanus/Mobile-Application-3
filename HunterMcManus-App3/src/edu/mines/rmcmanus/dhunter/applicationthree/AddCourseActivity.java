/**
 * Description: This activity is used to add a new Course to a semester.  The functionality
 * for this submission has not yet been implemented.  In the next submission the course
 * will be inserted into the database.
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.ArrayList;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;

public class AddCourseActivity extends Activity {

	public String semesterID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_course);

		semesterID = getIntent().getStringExtra(CourseListActivity.EXTRA_SEMESTER_ID);
	}

	public void addCourse(View v) {
		//		Toast.makeText(this, semesterID, Toast.LENGTH_SHORT).show();
		EditText courseName = (EditText) findViewById(R.id.addCourseName);
		String location;
		String time;
		if (courseName.getText().toString().equals("")) {
			Toast.makeText(this, getString(R.string.courseNameError), Toast.LENGTH_SHORT).show();
		}
		else {
			final ArrayList<String> categories = new ArrayList<String>();
			final ArrayList<String> weights = new ArrayList<String>();
			location = ((EditText) findViewById(R.id.courseTime)).getText().toString();
			time = ((EditText) findViewById(R.id.courseLocation)).getText().toString();
			if (((EditText) findViewById(R.id.assignmentCategory1)).getText().toString().equals("")
					&& ((EditText) findViewById(R.id.assignmentCategory2)).getText().toString().equals("")
					&& ((EditText) findViewById(R.id.assignmentCategory3)).getText().toString().equals("")) {
				Toast.makeText(this, getString(R.string.courseTypeError), Toast.LENGTH_SHORT).show();
			}
			else {
				if (!((EditText) findViewById(R.id.assignmentCategory1)).getText().toString().equals("")) {
					categories.add(((EditText) findViewById(R.id.assignmentCategory1)).getText().toString());
					weights.add(((EditText) findViewById(R.id.assignmentWeight1)).getText().toString());
				}
				if (!((EditText) findViewById(R.id.assignmentCategory2)).getText().toString().equals("")) {
					categories.add(((EditText) findViewById(R.id.assignmentCategory2)).getText().toString());
					weights.add(((EditText) findViewById(R.id.assignmentWeight2)).getText().toString());
				}
				if (!((EditText) findViewById(R.id.assignmentCategory3)).getText().toString().equals("")) {
					categories.add(((EditText) findViewById(R.id.assignmentCategory3)).getText().toString());
					weights.add(((EditText) findViewById(R.id.assignmentWeight3)).getText().toString());
				}
				final ParseObject course = new ParseObject("Course");
				course.put("name", courseName.getText().toString());
				course.put("semester", semesterID);
				course.put("time", time);
				course.put("location", location);
				course.put("user", ParseUser.getCurrentUser());
				course.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						String courseID = course.getObjectId();
						for (int i = 0; i < categories.size(); ++i) {
							ParseObject category = new ParseObject("Category");
							category.put("course", courseID);
							category.put("category_name", categories.get(i));
							category.put("weight", weights.get(i));
							category.put("user", ParseUser.getCurrentUser());
							category.saveInBackground(new SaveCallback() {
								@Override
								public void done(ParseException e) {
									finish();
								}
							});
						}
					}
				});
			}
		}
	}

}
