package com.example.nfcapp.client;

import java.net.*;
import java.io.*;

public class UtilConnection {
        
	    private String serverIpAddr = "";
		private int port = 6228;
		private Socket client;
		private InputStream inputServer;
		private OutputStream outputServer;
		private boolean serverUnavailableFlag = false;
		
		public boolean getServerAvailabilityStatus()
		{
			return serverUnavailableFlag;
		}
		public Socket CreateSocket()
		{
			try
			{
				client = new Socket();
				client.connect(new InetSocketAddress(serverIpAddr,port),4000);
			}
			catch(IOException e)
		    {
				serverUnavailableFlag = true;
		        e.printStackTrace();
		     }
			return client;
		}
		 
		public void openInputStream()
		{
			try
			{
				inputServer = client.getInputStream();
			}
			catch(IOException e)
		    {
		        e.printStackTrace();
		    }
		}
		
		public String readFromSocket()
		{
			String read_data = null;
			try
			{
				DataInputStream in = new DataInputStream(inputServer);
				read_data = in.readUTF();
			}
			catch(IOException e)
		    {
		        e.printStackTrace();
		    }
			return read_data;
		}
		
		public void openOutputStream()
		{
			try
			{
				outputServer = client.getOutputStream();
			}
	        catch(IOException e)
			{
			     e.printStackTrace();
			}
		}
		
		public void writeToSocket(String data)
		{
			try
			{
				DataOutputStream out = new DataOutputStream(outputServer);
				out.writeUTF(data);
			}
	        catch(IOException e)
			{
			     e.printStackTrace();
			}
		}
	
		public void closeSocket()
		{
		  try
		  {
			client.close();
		  } 
		  catch (IOException e)
		  {
			e.printStackTrace();
		  }
		}
		

	}


