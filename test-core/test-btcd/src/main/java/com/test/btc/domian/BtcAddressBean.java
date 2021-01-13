package com.test.btc.domian;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BtcAddressBean {

    private int id;
    private String txTabIds;
    private String address;
    private String privkey;
    private String pubkey;
    private String scriptpubkey;
    private String redeemscript;

}
