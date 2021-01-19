package com.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.port.websocket.util.ApiCallback;
import com.port.websocket.util.ApiClientFactory;
import com.port.websocket.util.ApiSocketClient;
import com.port.websocket.util.KlineEvent;
import com.port.websocket.util.KlineIntervalEnum;
import com.port.websocket.util.SubMessage;

import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
/**
 * @Author
 * @Description //TODO $
 * @Date $ $
 **/

public class WebSocketTest {
	public static void main(String[] args) throws URISyntaxException, InterruptedException  {
		CountDownLatch latch = new CountDownLatch(1);
		ApiSocketClient socketClient = ApiClientFactory.newInstance().newWebSocketClient();
        socketClient.onKlineEvent("2001,2002", KlineIntervalEnum.ONE_MINUTE, new ApiCallback<KlineEvent>() {
            @Override
            public void onConnected(SubMessage message) {
                //                System.out.println("message = [" + JSON.toJSONString(message) + "]");
            	System.out.println(format(message));
            }

            @Override
            public void onResponse(KlineEvent response) {
                //                System.out.println("response = [" + JSON.toJSONString(response) + "]");
                System.out.println(format(response));
            }

            @Override
            public void onFailure(Throwable cause) {
                if (cause != null) {
                    cause.printStackTrace();
                }

                latch.countDown();
            }

            @Override
            public void onClosed(int code, String reason) {
                System.out.println("code = [" + code + "], reason = [" + reason + "]");
            }
        });

        latch.await();
	}
	private static String format(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }   
}
