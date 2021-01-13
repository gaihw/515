package com.zmj.service;

import com.zmj.domain.btcd.OmniChain;

public interface OmniService {
    /**
     * 创建交易
     */
    String transaction(OmniChain omniChain);
}
