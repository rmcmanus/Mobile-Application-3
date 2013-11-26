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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class AddCourseActivity extends Activity {

	public String semesterID;
	private ListView myList;
	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_course);

		semesterID = getIntent().getStringExtra(CourseListActivity.EXTRA_SEMESTER_ID);
		myList = (ListView) findViewById(R.id.MyList);
		myList.setItemsCanFocus(true);
		myAdapter = new MyAdapter();
		myList.setAdapter(myAdapter);

	}

	public void addAnotherView(View v) {
		myAdapter.addItem("");
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
			location = ((EditText) findViewById(R.id.courseTime)).getText().toString();
			time = ((EditText) findViewById(R.id.courseLocation)).getText().toString();
			int countOfEmpty = 0;
			for (int i = 0; i < myAdapter.getCount(); ++i) {
				if (!myAdapter.getText(i).equals("")) {
					countOfEmpty++;
					break;
				}
			}
			if (countOfEmpty == 0) {
				Toast.makeText(this, getString(R.string.courseTypeError), Toast.LENGTH_SHORT).show();
			}
			else {
				
				final ParseObject course = new ParseObject("Course");
				course.put("name", courseName.getText().toString());
				course.put("semester", semesterID);
				course.put("time", time);
				course.put("location", location);
				course.put("user", ParseUser.getCurrentUser());
				course.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						String courseID = course.getObjectId();
						for (int i = 0; i < myAdapter.getCount(); ++i) {
							if (!myAdapter.getText(i).equals("")) {
								ParseObject category = new ParseObject("Category");
								category.put("course", courseID);
								category.put("category_name", myAdapter.getText(i));
								category.put("user", ParseUser.getCurrentUser());
								category.saveInBackground(new SaveCallback() {
									@Override
									public void done(ParseException e) {
										finish();
									}
								});
							}
						}
					}
				});
			}
		}
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public ArrayList<ListItem> myItems = new ArrayList<ListItem>();

		public MyAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < 5; i++) {
				ListItem listItem = new ListItem();
				listItem.caption = "";
				myItems.add(listItem);
			}
			notifyDataSetChanged();
		}

		public void addItem(String test) {
			ListItem listItem = new ListItem();
			listItem.caption = "";
			myItems.add(listItem);
			notifyDataSetChanged();
		}

		public int getCount() {
			return myItems.size();
		}

		public String getText(int position) {
			return myItems.get(position).caption;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item, null);
				holder.caption = (EditText) convertView
						.findViewById(R.id.ItemCaption);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//Fill EditText with the value you have in data source
			holder.caption.setText(myItems.get(position).caption);
			holder.caption.setId(position);

			//we need to update adapter once we finish with editing
			holder.caption.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus){
						final int position = v.getId();
						final EditText Caption = (EditText) v;
						myItems.get(position).caption = Caption.getText().toString();
					}
				}
			});

			return convertView;
		}
	}

	class ViewHolder {
		EditText caption;
	}

	class ListItem {
		String caption;
	}

}
