package edu.mines.rmcmanus.dhunter.applicationthree;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends FragmentActivity {

	Intent intent;
	EditText first, last, email, password;
	EditText loginUsername, loginPassword, confirmPassword;
	boolean proceed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		first = (EditText) findViewById(R.id.firstname);
		last = (EditText) findViewById(R.id.lastname);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.passwordSignUp);
		confirmPassword = (EditText) findViewById(R.id.confirmPasswordSignUp);

		loginUsername = (EditText) findViewById(R.id.username);
		loginPassword = (EditText) findViewById(R.id.password);

		// Initialize our app to access specific folders on Parse.com
		// Application ID: aB0qJXP4e7xyBoZwTe4GrkNsgxgxXcCRUpZC091C
		// Client ID: f5g9Z9taJ3KPklhO6iRMOKxj8zVyINwYYJLrXkaS
		Parse.initialize(this, "aB0qJXP4e7xyBoZwTe4GrkNsgxgxXcCRUpZC091C", "f5g9Z9taJ3KPklhO6iRMOKxj8zVyINwYYJLrXkaS");
		ParseAnalytics.trackAppOpened(getIntent());

		// Check to see if a user is currently logged in
		// If true, moves directly to the semester activity
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			startSemesterIntent();
		}
	}

	public void login(View v) {
		String username = loginUsername.getText().toString();
		String password = loginPassword.getText().toString();
		Log.d("Parse Error", username);
		Log.d("Parse Error", password);

		ParseUser.logInInBackground(username, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					startSemesterIntent();
				} else {
					Log.d("Parse Error", e.toString());
					Log.d("Parse Error", "" + e.getCode());
					if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
						TextView invalid = (TextView) findViewById(R.id.errorlabel);
						invalid.setTextColor(Color.RED);
						invalid.setText(getString(R.string.invalidlogin));
					}
				}
			}
		});
	}

	public void guest(View v) {
		// Creates an automatic user in Parse if not logged in. Once logged in, data is saved
		// to that new or existing user
		ParseUser.enableAutomaticUser();
		ParseUser.getCurrentUser().increment("RunCount");
		ParseUser.getCurrentUser().saveInBackground();

		showWarningDialog();
	}

	public void signup(View v) {
		String firstName = first.getText().toString();
		String lastName = last.getText().toString();
		String userPassword = password.getText().toString();
		String confirm= confirmPassword.getText().toString();
		String userEmail = email.getText().toString();
		
		if (firstName.equals("") || lastName.equals("") || userPassword.equals("") || 
				confirm.equals("") || userEmail.equals("")) {
			TextView invalid = (TextView) findViewById(R.id.errorlabel);
			invalid.setTextColor(Color.RED);
			invalid.setText(getString(R.string.allrequiredfields));
			return;
		}

		if (userPassword.equals(confirm)) {
			ParseUser newUser = new ParseUser();

			newUser.setUsername(userEmail);
			newUser.put("first", firstName);
			newUser.put("last", lastName);
			newUser.setPassword(userPassword);
			newUser.setEmail(userEmail);

			newUser.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						//Log.d("Parse Message", "Let's start an intent!");
						startSemesterIntent();
					} else {
						Log.d("Parse Error", e.toString());
						if (e.getCode() == ParseException.INVALID_EMAIL_ADDRESS) {
							TextView invalid = (TextView) findViewById(R.id.errorlabel);
							invalid.setTextColor(Color.RED);
							invalid.setText(getString(R.string.invaidemail));
						}
					}
				}
			});
		} else {
			TextView invalid = (TextView) findViewById(R.id.errorlabel);
			invalid.setTextColor(Color.RED);
			invalid.setText(getString(R.string.nonmatchingpasswords));
		}

	}

	private void showWarningDialog() {
		final Warning warningDialog = new Warning(this);
		warningDialog.show();
		warningDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (warningDialog.proceed) {
					startSemesterIntent();
				}
			}

		});
	}

	public void startSemesterIntent() {
		intent = new Intent(this, SemesterActivity.class);
		startActivity(intent);
		finish();
	}

}
