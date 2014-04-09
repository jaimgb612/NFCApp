package com.example.nfcapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nfcapp.AboutActivity;
import com.example.nfcapp.R;

public class DisplayRoomStatusActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_room_status);
		//setupActionBar();
		
		@SuppressWarnings("unused")
		Intent receivedIntent = getIntent();
		displayRoomStatus();
	}
	
	public void displayRoomStatus() {
		TextView dataField = (TextView) findViewById(R.id.displayRoomStatusField);
		dataField.setText("Room Occupancy Table");
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	/*private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_room_status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
			
		case R.id.action_about:
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
		}
		
		return super.onOptionsItemSelected(item);
	}


}
