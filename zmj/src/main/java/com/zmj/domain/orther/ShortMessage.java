package com.zmj.domain.orther;

import lombok.Data;

@Data
public class ShortMessage {
    private Long id;
    private String mobile;
    private String keywords;
    private String sendTime;
}
