package com.test;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
 
 
public class SocketClientEngine{
 
 
	public static void main(String[] args) {
		try {
			WebClientEnum.CLIENT.initClient(new MsgWebSocketClient("ws://106.14.203.54:8087?Name=opt1&Password=opt1"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}