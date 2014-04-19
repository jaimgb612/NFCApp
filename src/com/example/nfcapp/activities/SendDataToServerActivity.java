package com.example.nfcapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcapp.R;
import com.example.nfcapp.utilClasses.SocketClass;

public class SendDataToServerActivity extends Activity {
	private String text;
	public boolean serverUnavailableFlag = false;
	
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
		
		sendDataToServer();
		displayRoomStatus();
		finish();
	}
	
	public void sendDataToServer() {
		SendDataToServerTask newTask = new SendDataToServerTask();
		newTask.execute();
		
		if (serverUnavailableFlag == true) {
			Toast.makeText(this, "Server unavailable.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void displayRoomStatus() {
		Intent intentToDisplay = new Intent(this, DisplayRoomStatusActivity.class);
		startActivity(intentToDisplay);
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
	
	private class SendDataToServerTask extends AsyncTask<Void , Void, Void> {
		 
		public String createMessage() {
			StringBuilder data = new StringBuilder("MESSAGEA");
			data.append(" ");
			data.append(text);
			//data.append(" ");
			//data.append(MainActivity.USERNAME);
			System.out.println(data);
			return data.toString();
		}
		
		protected Void doInBackground(Void... args) {
		        String message = null;
				SocketClass clientSocket = new SocketClass();
				if (clientSocket.createSocket() == -1) {
					serverUnavailableFlag = true;
					return null;
				}
				
				clientSocket.openOutputStream();
				//objUtil.openInputStream();
				
				message = new String(createMessage());
				System.out.println(message);
				clientSocket.writeToSocket(message);
				
				//close
				clientSocket.closeSocket();
				clientSocket = null;
				message = null;
			    return null;
		 }

	     protected void onPostExecute(Void arg) {
	         //showDialog("Downloaded " + result + " bytes");
	    	 
	     }
	}
}