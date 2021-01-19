package com.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.ObjectUtils;
import org.java_websocket.WebSocket.READYSTATE;
 
 
public enum WebClientEnum {
 
	CLIENT;
	
	private static MsgWebSocketClient socketClient = null;
	
	public static void initClient(MsgWebSocketClient client) {
		socketClient = client;
		if(ObjectUtils.notEqual(null, socketClient)) {
			socketClient.connect();
			System.out.println(socketClient.getURI());
//			while (!socketClient.getReadyState().equals(READYSTATE.OPEN)) {
//				  System.out.println("还没有建立连接...");
//			}
			socketClient.send("rdb_valgetsnapshot,1001,d0.f10.pv,d1.dbl01.pv,d2.i03.pv,d3.l01.pv,d4.k01.pv");
		}
		boolean flag = true;
		int i=1000;
		while(flag) {
			socketClient.send("测试websocket。。。"+(i--));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i == 0) {
				flag = false;
			}
		}
	}
	
}
