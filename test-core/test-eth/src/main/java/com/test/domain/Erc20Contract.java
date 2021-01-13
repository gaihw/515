package com.test.domain;

import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.parity.Parity;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Erc20Contract extends Contract {

    protected Erc20Contract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
            }));
        List<EventValues> valueList = getEventValuesList(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    private static List<EventValues> getEventValuesList(Event event, TransactionReceipt transactionReceipt) {
        List<Log> logs = transactionReceipt.getLogs();
        List<EventValues> values = new ArrayList<>();
        for (Log log : logs) {
            EventValues eventValues = getEventValues(event, log);
            if (eventValues != null) {
                values.add(eventValues);
            }
        }
        return values;
    }

    private static EventValues getEventValues(Event event, Log log) {
        return staticExtractEventParameters(event, log);
    }

    public static RemoteCall<BigInteger> balanceOf(String _owner, Parity parity, String contractAddress) {
        Function function = new Function("balanceOf",
            Arrays.<Type>asList(new Address(_owner)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
            }));
        return executeRemoteCallSingleValue(function, BigInteger.class, parity, _owner, contractAddress);
    }

    private static <T> RemoteCall<T> executeRemoteCallSingleValue(
        Function function, Class<T> returnType, Parity parity, String address, String contractAddress) {
        return new RemoteCall<>(() -> executeCallSingleValue(function, returnType, parity, address, contractAddress));
    }

    private static <T extends Type, R> R executeCallSingleValue(
        Function function, Class<R> returnType, Parity parity, String address, String contractAddress) throws IOException {
        T result = executeCallSingleValue(function, parity, address, contractAddress);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        Object value = result.getValue();
        if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString();  // cast isn't necessary
        } else {
            throw new ContractCallException(
                "Unable to convert response: " + value
                + " to expected type: " + returnType.getSimpleName());
        }
    }

    private static <T extends Type> T executeCallSingleValue(
        Function function, Parity parity, String address, String contractAddress) throws IOException {
        List<Type> values = executeCall(function, parity, address, contractAddress);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }

    private static List<Type> executeCall(
        Function function, Parity parity, String address, String contractAddress) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = parity.ethCall(
            Transaction.createEthCallTransaction(
                address, contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST)
            .send();

        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }

    public static class TransferEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}
