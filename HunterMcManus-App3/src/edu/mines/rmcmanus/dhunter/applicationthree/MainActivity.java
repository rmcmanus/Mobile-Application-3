package edu.mines.rmcmanus.dhunter.applicationthree;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends FragmentActivity {

	Intent intent;
	EditText first, last, email, password;
	boolean proceed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		first = (EditText) findViewById(R.id.firstname);
		last = (EditText) findViewById(R.id.lastname);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.passwordSignUp);

		// Initialize our app to access specific folders on Parse.com
		// Application ID: aB0qJXP4e7xyBoZwTe4GrkNsgxgxXcCRUpZC091C
		// Client ID: f5g9Z9taJ3KPklhO6iRMOKxj8zVyINwYYJLrXkaS
		Parse.initialize(this, "aB0qJXP4e7xyBoZwTe4GrkNsgxgxXcCRUpZC091C", "f5g9Z9taJ3KPklhO6iRMOKxj8zVyINwYYJLrXkaS");

		ParseAnalytics.trackAppOpened(getIntent());
	}

	public void login(View v) {
		Intent intent = new Intent(this, SemesterActivity.class);
		startActivity(intent);
	}

	public void guest(View v) {
		showWarningDialog();
	}

	public void signup(View v) {
		String firstName = first.getText().toString();
		String lastName = last.getText().toString();
		String userPassword = password.getText().toString();
		String userEmail = email.getText().toString();

		ParseUser newUser = new ParseUser();

		newUser.setUsername(firstName + " " + lastName);
		newUser.setPassword(userPassword);
		newUser.setEmail(userEmail);

		newUser.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Log.d("Parse Message", "Let's start an intent!");
					startSemesterIntent();
				} else {
					Log.d("Parse Error", e.toString());
				}
			}
		});
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
	}

}
