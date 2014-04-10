package com.example.nfcapp.client;

import java.net.*;
import java.io.*;

public class UtilConnection {
        
	    private String serverIpAddr = "54.215.211.167";
		private int port = 2959;
		private Socket client = null;
		private BufferedReader in = null;
		private PrintWriter out = null; 
		private boolean serverUnavailableFlag = false;
		
		public boolean getServerAvailabilityStatus() {
			return serverUnavailableFlag;
		}
		
		public void createSocket() {
			client = new Socket();
			try {
				client.connect(new InetSocketAddress(serverIpAddr,port),4000);
			}
			catch(IOException e) {
				serverUnavailableFlag = true;
		        //e.printStackTrace();
		    }
		}
		 
		public void openInputStream() {
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			}
			catch(IOException e) {
		        e.printStackTrace();
		    }
		}
		
		public String readFromSocket() {
			String read_data = null;
			try {
				read_data = in.readLine();
			}
			catch(IOException e) {
		        e.printStackTrace();
		    }
			return read_data;
		}
		
		public void openOutputStream() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
			}
	        catch(IOException e) {
			     e.printStackTrace();
			}
		}
		
		public void writeToSocket(String data) {
			out.println(data);
		}
	
		public void closeSocket() {
		  try {
			client.close();
		  } 
		  catch (IOException e) {
			e.printStackTrace();
		  }
		}
	}


