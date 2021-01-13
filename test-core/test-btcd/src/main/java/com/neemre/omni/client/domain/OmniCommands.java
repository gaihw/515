package com.neemre.omni.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * An enumeration specifying the <i>bitcoind</i> JSON-RPC API commands currently supported by
 * btcd-cli4j.
 **/
@Getter
@ToString
@AllArgsConstructor
public enum OmniCommands {
    GET_TRANSACTION("omni_gettransaction", 1, 1),
    GET_LISTTRANSACTIONSINBLOCK("omni_listblocktransactions", 1, 1),
    CREATE_PAYLOAD_SIMPLESEND("omni_createpayload_simplesend", 2, 2),
    CREATE_RAWTX_OPRETURN("omni_createrawtx_opreturn", 2, 2),
    CREATE_RAWTX_REFERENCE("omni_createrawtx_reference", 2, 2),
    CREATE_RAWTX_CHANGE("omni_createrawtx_change", 4, 4),
    GET_BALANCE("omni_getbalance", 2, 2);
    private final String name;
    private final int minParams;
    private final int maxParams;
}