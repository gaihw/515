package com.port.websocket.util;

import lombok.Data;

@Data
public class KlineEvent {

    private String type;
    private String product;
    private String business;
    private KlineDto data;

    @Data
    public static class KlineDto {
        private String id;
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;
        private String amount;
        private String type;
        private String time;
    }

}
