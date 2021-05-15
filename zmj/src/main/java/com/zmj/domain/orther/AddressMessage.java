package com.zmj.domain.orther;

import lombok.Data;

@Data
public class AddressMessage {
    private Long id;
    private Long userId;
    private int currencyId;
    private String innerId;
    private String address;
    private String paymentId;
    private String tag;
    private int trust;
    private String createdDate;
    private String modifyDate;
}
