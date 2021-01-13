package com.neemre.omni.client;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.omni.client.domain.OmniBalance;
import com.neemre.omni.client.domain.OmniProperty;
import com.neemre.omni.client.domain.OmniTransaction;
import com.neemre.omni.client.domain.Prevtx;

import java.math.BigDecimal;
import java.util.List;

public interface OmniClient extends BtcdClient {



    List<String> getOmniTransactionInBlock(int blockCount)
        throws BitcoindException, CommunicationException;

    OmniTransaction getOmniTransactionInfo(String txid)
        throws BitcoindException, CommunicationException;

    String createOmniPayloadForSimpleSend(long propertyId, BigDecimal amount)
        throws BitcoindException, CommunicationException;

    String createOmniRawTxForOpreturn(String rawTx, String payloagHex)
        throws BitcoindException, CommunicationException;

    String createOmniRawTxForReference(String rawTx, String address)
        throws BitcoindException, CommunicationException;

    String createOmniRawTxForChange(String rawTx, List<Prevtx> prevtxs, String address,
                                    BigDecimal fee) throws BitcoindException, CommunicationException;

    OmniBalance getOmniBalance(String address, long propertyid)
        throws BitcoindException, CommunicationException;

    OmniProperty getOmniProperty(String propertyId) throws BitcoindException, CommunicationException;
}
