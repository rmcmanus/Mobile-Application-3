/**
 * Description: This activity is used to set up settings for the application. 
 * The user can set school name and what type of terms scheme they are on.  The
 * functionality has not yet been implemented. This information will be inserted in
 * the database
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.Activity;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		Spinner typeSpinner = (Spinner) findViewById(R.id.semesterTypeSpinner);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.termsArray, android.R.layout.simple_spinner_dropdown_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(arrayAdapter);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.setting, menu);
//		return true;
//	}

}
