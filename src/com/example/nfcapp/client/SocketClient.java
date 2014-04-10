package com.example.nfcapp.client;

import java.net.*;
import java.io.*;
 
public class SocketClient{

	//create socket 
	
	
	public void sendDataToServer()
	{
		String data = null;
		UtilConnection objUtil = new UtilConnection();
		objUtil.CreateSocket();
		
		//openStream();
		
		objUtil.openOutputStream();
		objUtil.openInputStream();
		
		//send data to server
		//receive data from server
		objUtil.writeToSocket(data);
		
		//close
		objUtil.closeSocket();
		
		
	}
	
	

}
