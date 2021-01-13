package com.eos.domain.common.transaction;


import com.eos.chain.PackedTransaction;

import java.util.List;

public class SignedPackedTransaction extends PackedTransaction {

    private List<String> signatures;

    public SignedPackedTransaction(){

    }
    @Override
    public List<String> getSignatures() {
        return signatures;
    }
    @Override
    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }
}
