package com.port.test.websocket;

import java.util.concurrent.CountDownLatch;

import org.testng.annotations.Test;

import com.port.util.Format;
import com.port.websocket.util.*;

public class WebSocketTest {
	@Test
    public void subKline() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ApiSocketClient socketClient = ApiClientFactory.newInstance().newWebSocketClient();
        socketClient.onKlineEvent("2001", KlineIntervalEnum.ONE_MINUTE, new ApiCallback<KlineEvent>() {
            @Override
            public void onConnected(SubMessage message) {
                //                System.out.println("message = [" + JSON.toJSONString(message) + "]");
                System.out.println(Format.format(message));
            }

            @Override
            public void onResponse(KlineEvent response) {
                //                System.out.println("response = [" + JSON.toJSONString(response) + "]");
                System.out.println(Format.format(response));
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
}
