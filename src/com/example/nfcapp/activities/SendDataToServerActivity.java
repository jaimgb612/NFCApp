package com.example.nfcapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nfcapp.R;
import com.example.nfcapp.client.UtilConnection;

public class SendDataToServerActivity extends Activity {
	private String text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_data_to_server);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		Intent receivedIntent = getIntent();
		text = (String) receivedIntent.getStringExtra(MainActivity.SEND_DATA_TO_SERVER);
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
	
	public String packetData()
	{
		StringBuilder data = new StringBuilder();
		data.append(MainActivity.USERNAME);
		data.append(";");
		data.append(text);
		return data.toString();
	}
	
	private class SendDataToServerTask extends AsyncTask<Void , Void, Void>
	{
		 protected Void doInBackground(Void... voids)
		 {
		        String packet = null;
				UtilConnection objUtil = new UtilConnection();
				objUtil.CreateSocket();
				
				objUtil.openOutputStream();
				//objUtil.openInputStream();
				
				packet = packetData();
				objUtil.writeToSocket(packet);
				
				//close
				objUtil.closeSocket();
			    return null;
		 }
		 protected void onProgressUpdate(Void... voids) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(Void... voids) {
	         //showDialog("Downloaded " + result + " bytes");
	     }
	}

}