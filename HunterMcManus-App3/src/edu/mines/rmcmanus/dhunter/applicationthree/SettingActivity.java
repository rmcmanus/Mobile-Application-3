/**
 * Description: This activity is used to set up settings for the application. 
 * The user can set school name and what type of terms scheme they are on.  The
 * functionality has not yet been implemented. This information will be inserted in
 * the database
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class SettingActivity extends Activity {

	public EditText changeEmail;
	public Button button;
	public TextView label;
	public String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		changeEmail = (EditText) findViewById(R.id.changeEdit);
		button = (Button) findViewById(R.id.submitbuttonsettings);
		label = (TextView) findViewById(R.id.changeEmailLabel);
		if (ParseUser.getCurrentUser().isAuthenticated()) {
			changeEmail.setText(ParseUser.getCurrentUser().getString("email"));
		} else {
			changeEmail.setVisibility(View.INVISIBLE);
			button.setVisibility(View.INVISIBLE);
			label.setText(getString(R.string.changeEmailGuest));
		}
	}

	public void changeEmail(View v) {
		email = changeEmail.getText().toString();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		query.whereEqualTo("email", email);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					if (objects.size() == 0) {
						ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
						// Retrieve the object by id
						query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
							public void done(ParseObject user, ParseException e) {
								if (e == null) {
									// Now let's update it with some new data. In this case, only cheatMode and score
									// will get sent to the Parse Cloud. playerName hasn't changed.
									user.put("username", email);
									user.put("email", email);
									user.saveInBackground(new SaveCallback() {

										@Override
										public void done(ParseException e) {
											if (e == null) {
												Toast.makeText(getBaseContext(), getString(R.string.userUpdated), Toast.LENGTH_SHORT).show();
											} else {
												Log.d("update", "Error: " + e.getMessage() + " " + e.getCode());
												if (e.getCode() == ParseException.INVALID_EMAIL_ADDRESS) {
													Toast.makeText(getBaseContext(), getString(R.string.invaidemail), Toast.LENGTH_SHORT).show();
												}
												if (e.getCode() == ParseException.EMAIL_TAKEN || e.getCode() == ParseException.USERNAME_TAKEN) {
													Toast.makeText(getBaseContext(), getString(R.string.duplicateemail), Toast.LENGTH_SHORT).show();
												}
											}
										}
									});
								}
							}
						});
					}
					else {
						Toast.makeText(getBaseContext(), getString(R.string.duplicateemail), Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Log.d("update", "Error: " + e.getMessage());
				}
			}
		});
	}
	
	public void logout(View v) {
		if (ParseUser.getCurrentUser() != null) {
			ParseUser.logOut();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.setting, menu);
	//		return true;
	//	}

}
