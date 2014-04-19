package com.example.nfcapp.utilClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClass {
        
	    private final String serverIpAddr = "54.215.211.167";
		private final int port = 9776;
		private Socket clientSocket = null;
		private BufferedReader in = null;
		private PrintWriter out = null;
		
		public int createSocket() {
			clientSocket = new Socket();
			try {
				clientSocket.connect(new InetSocketAddress(serverIpAddr, port), 4000);
				return 0;
			}
			catch(IOException e) {
				return -1;
				//e.printStackTrace();
		    }
		}
		
		public void openInputStream() {
			try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
		        read_data = null;
		    }
			return read_data;
		}
		
		public void openOutputStream() {
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
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
			  clientSocket.close();
		  } 
		  catch (IOException e) {
			e.printStackTrace();
		  }
		}
}