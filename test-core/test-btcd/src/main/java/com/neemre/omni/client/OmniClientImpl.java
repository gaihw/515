package com.neemre.omni.client;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.Commands;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.neemre.btcdcli4j.core.client.ClientConfigurator;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClient;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClientImpl;
import com.neemre.btcdcli4j.core.util.CollectionUtils;
import com.neemre.omni.client.domain.OmniBalance;
import com.neemre.omni.client.domain.OmniProperty;
import com.neemre.omni.client.domain.OmniTransaction;
import com.neemre.omni.client.domain.Prevtx;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

@Slf4j
public class OmniClientImpl extends BtcdClientImpl implements OmniClient {

    private JsonRpcClient rpcClient;
    private ClientConfigurator configurator;

    public OmniClientImpl(CloseableHttpClient httpProvider, Properties nodeConfig)
            throws BitcoindException, CommunicationException {
        super(httpProvider, nodeConfig);
        initialize();
        rpcClient = new JsonRpcClientImpl(configurator.checkHttpProvider(httpProvider),
                configurator.checkNodeConfig(nodeConfig));
        // configurator.checkNodeVersion(getInfo().getVersion());
        // configurator.checkNodeHealth((BtcBlockBean)getBlock(getBestBlockHash(), true));
    }


    public OmniClientImpl(String rpcHost, Integer rpcPort, String rpcUser, String rpcPassword)
            throws BitcoindException, CommunicationException {
        this((String) null, rpcHost, rpcPort, rpcUser, rpcPassword);
    }


    public OmniClientImpl(String rpcProtocol, String rpcHost, Integer rpcPort, String rpcUser,
                          String rpcPassword) throws BitcoindException, CommunicationException {
        this(rpcProtocol, rpcHost, rpcPort, rpcUser, rpcPassword, null);
    }


    public OmniClientImpl(String rpcProtocol, String rpcHost, Integer rpcPort, String rpcUser,
                          String rpcPassword, String httpAuthScheme) throws BitcoindException,
            CommunicationException {
        this(null, rpcProtocol, rpcHost, rpcPort, rpcUser, rpcPassword, httpAuthScheme);
    }

    public OmniClientImpl(CloseableHttpClient httpProvider, String rpcProtocol, String rpcHost,
                          Integer rpcPort, String rpcUser, String rpcPassword, String httpAuthScheme)
            throws BitcoindException, CommunicationException {
        this(httpProvider, new ClientConfigurator().toProperties(rpcProtocol, rpcHost, rpcPort,
                rpcUser, rpcPassword, httpAuthScheme));
    }

    @Override
    public List<String> getOmniTransactionInBlock(int blockCount)
            throws BitcoindException, CommunicationException {
        String omniTransactionsJson = rpcClient.execute(
                Commands.GET_OMNI_LISTTRANSACTIONSINBLOCK.getName(), blockCount);
        List<String> omniTransactions = rpcClient.getMapper()
                .mapToList(omniTransactionsJson, String.class);
        return omniTransactions;
    }

    @Override
    public OmniTransaction getOmniTransactionInfo(String txid)
            throws BitcoindException, CommunicationException {
        String omniTransactionsInfoJson = rpcClient.execute(
                Commands.GET_OMNI_TRANSACTION.getName(), txid);
        OmniTransaction omniTransactionInfo = rpcClient.getMapper()
                .mapToEntity(omniTransactionsInfoJson, OmniTransaction.class);
        return omniTransactionInfo;
    }

    @Override
    public String createOmniPayloadForSimpleSend(long propertyId, BigDecimal amount)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(propertyId, amount.toPlainString());
        String payloadJson = rpcClient.execute(Commands.CREATE_OMNI_PAYLOAD_SIMPLESEND.getName(),
                params);
        String payload = rpcClient.getParser()
                .parseString(payloadJson);
        return payload;
    }

    @Override
    public String createOmniRawTxForOpreturn(String rawTx, String payloagHex)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(rawTx, payloagHex);
        String rawTxOpreturnJson = rpcClient.execute(Commands.CREATE_OMNI_RAWTX_OPRETURN.getName(),
                params);
        String rawTxOpreturn = rpcClient.getParser()
                .parseString(rawTxOpreturnJson);
        return rawTxOpreturn;
    }

    @Override
    public String createOmniRawTxForReference(String rawTx, String address)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(rawTx, address);
        String rawTxReferenceJson = rpcClient.execute(
                Commands.CREATE_OMNI_RAWTX_REFERENCE.getName(),
                params);
        String rawTxReference = rpcClient.getParser()
                .parseString(rawTxReferenceJson);
        return rawTxReference;
    }


    @Override
    public String createOmniRawTxForChange(String rawTx, List<Prevtx> prevtxs, String address,
                                           BigDecimal fee) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(rawTx, prevtxs, address, fee);
        String rawTxChangeJson = rpcClient.execute(Commands.CREATE_OMNI_RAWTX_CHANGE.getName(),
                params);
        String rawTxChange = rpcClient.getParser()
                .parseString(rawTxChangeJson);
        return rawTxChange;
    }

    @Override
    public OmniBalance getOmniBalance(String address, long propertyid)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(address, propertyid);
        String omniBalanceJson = rpcClient.execute(Commands.GET_OMNI_BALANCE.getName(),
                params);
        OmniBalance omniBalance = rpcClient.getMapper()
                .mapToEntity(omniBalanceJson, OmniBalance.class);
        return omniBalance;
    }

    @Override
    public OmniProperty getOmniProperty(String propertyId) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(propertyId);
        String omniPropertyJson = rpcClient.execute(Commands.GET_OMNI_BALANCE.getName(), params);
        OmniProperty omniProperty = rpcClient.getMapper()
                .mapToEntity(omniPropertyJson, OmniProperty.class);
        return omniProperty;
    }


    private void initialize() {
        configurator = new ClientConfigurator();
    }
}