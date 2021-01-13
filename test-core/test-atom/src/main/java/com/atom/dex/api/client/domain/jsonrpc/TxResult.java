package com.atom.dex.api.client.domain.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TxResult {
    private String data;
    private String log;
    private Integer code = 0;
    private List<Tag> tags;
    private List<Event> events;


    @Data
    public static class Tag {
        private String key;
        private String value;

    }

    @Data
    public static class Event {
        private List<Attribute> attributes;
        private String type;

    }

    @Data
    public static class Attribute {
        private String key;
        private String value;
    }


}
