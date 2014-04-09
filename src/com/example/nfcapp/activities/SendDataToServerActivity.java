package com.example.nfcapp.activities;

import com.example.nfcapp.AboutActivity;
import com.example.nfcapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SendDataToServerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_data_to_server);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		Intent receivedIntent = getIntent();
		String text = (String) receivedIntent.getStringExtra(MainActivity.SEND_DATA_TO_SERVER);
		TextView dataField = (TextView) findViewById(R.id.displayDataField);
		dataField.setText(text);
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
		getMenuInflater().inflate(R.menu.send_data_to_server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
