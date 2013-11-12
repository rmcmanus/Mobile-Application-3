package edu.mines.rmcmanus.dhunter.applicationthree;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void login(View v) {
		Intent intent = new Intent(this, SemesterActivity.class);
		startActivity(intent);
	}
	
	public void guest(View v) {
		Intent intent = new Intent(this, SemesterActivity.class);
		startActivity(intent);
	}
	
	public void signup(View v) {
		Intent intent = new Intent(this, SemesterActivity.class);
		startActivity(intent);
	}

}
