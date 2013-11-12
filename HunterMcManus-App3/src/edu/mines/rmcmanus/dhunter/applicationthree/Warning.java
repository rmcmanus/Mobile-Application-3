/**
* Description:  This class defines a custom dialog that will be shown when a user
 * presses the add button on the team selection screen.  This dialog has a listener
 * for if the user presses the submit button, and if it does, then it passes information
 * to the activity.
 * 
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationthree;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Warning extends Dialog implements
android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes, no;
	public boolean proceed = false;

	public Warning(Activity a) {
		super(a);
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);
		yes = (Button) findViewById(R.id.yeswarning);
		yes.setOnClickListener(this);
		no = (Button) findViewById(R.id.nowarning);
		no.setOnClickListener(this);

	}

	/**
	 * The is the listener for the button on the dialog box.  If the button is pressed
	 * then it will set a team name, and set the boolean to check if a name has been
	 * set to true.  The dialog will then be dismissed.
	 * 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yeswarning:
			proceed = true;
			break;
		default:
			break;
		}
		dismiss();
	}
}
