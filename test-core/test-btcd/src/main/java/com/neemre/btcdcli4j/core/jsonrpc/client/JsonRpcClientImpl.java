package com.neemre.btcdcli4j.core.jsonrpc.client;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.common.Defaults;
import com.neemre.btcdcli4j.core.common.Errors;
import com.neemre.btcdcli4j.core.http.HttpConstants;
import com.neemre.btcdcli4j.core.http.client.SimpleHttpClient;
import com.neemre.btcdcli4j.core.http.client.SimpleHttpClientImpl;
import com.neemre.btcdcli4j.core.jsonrpc.JsonMapper;
import com.neemre.btcdcli4j.core.jsonrpc.JsonPrimitiveParser;
import com.neemre.btcdcli4j.core.jsonrpc.JsonRpcLayerException;
import com.neemre.btcdcli4j.core.jsonrpc.domain.JsonRpcError;
import com.neemre.btcdcli4j.core.jsonrpc.domain.JsonRpcRequest;
import com.neemre.btcdcli4j.core.jsonrpc.domain.JsonRpcResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class JsonRpcClientImpl implements JsonRpcClient {
	
	private SimpleHttpClient httpClient;
	private JsonPrimitiveParser parser;
	private JsonMapper mapper;


	public JsonRpcClientImpl(CloseableHttpClient httpProvider, Properties nodeConfig) {
		httpClient = new SimpleHttpClientImpl(httpProvider, nodeConfig);
		parser = new JsonPrimitiveParser();
		mapper = new JsonMapper();
	}

	@Override
	public String execute(String method) throws BitcoindException, CommunicationException {
		return execute(method, null);
	}

	@Override
	public <T> String execute(String method, T param) throws BitcoindException, 
			CommunicationException {
		List<T> params = new ArrayList<T>();
		params.add(param);
		return execute(method, params);
	}

	@Override
	public <T> String execute(String method, List<T> params) throws BitcoindException, 
			CommunicationException {
		String requestUuid = getNewUuid();
		JsonRpcRequest<T> request = getNewRequest(method, params, requestUuid);
		String requestJson = mapper.mapToJson(request);
		String responseJson = httpClient.execute(HttpConstants.REQ_METHOD_POST, requestJson);
		JsonRpcResponse response = mapper.mapToEntity(responseJson, JsonRpcResponse.class);
		response = verifyResponse(request, response);
		response = checkResponse(response);
		return response.getResult();
	}

	@Override
	public JsonPrimitiveParser getParser() {
		return parser;
	}

	@Override
	public JsonMapper getMapper() {
		return mapper;
	}

	@Override
	public void close() {
		httpClient.close();
	}
	
	private <T> JsonRpcRequest<T> getNewRequest(String method, List<T> params, String id) {
		JsonRpcRequest<T> rpcRequest = new JsonRpcRequest<T>();
		rpcRequest.setJsonrpc(Defaults.JSON_RPC_VERSION);
		rpcRequest.setMethod(method);
		rpcRequest.setParams(params);
		rpcRequest.setId(id);
		return rpcRequest;
	}

	private JsonRpcResponse getNewResponse(String result, JsonRpcError error, String id) {
		JsonRpcResponse rpcResponse = new JsonRpcResponse();
		rpcResponse.setJsonrpc(Defaults.JSON_RPC_VERSION);
		rpcResponse.setResult(result);
		rpcResponse.setError(error);
		rpcResponse.setId(id);
		return rpcResponse;
	}

	private String getNewUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	private <T> JsonRpcResponse verifyResponse(JsonRpcRequest<T> request, JsonRpcResponse response) 
			throws JsonRpcLayerException {
		if (response == null) {
			throw new JsonRpcLayerException(Errors.RESPONSE_JSONRPC_NULL);
		}
		if (response.getId() == null) {
			throw new JsonRpcLayerException(Errors.RESPONSE_JSONRPC_NULL_ID);
		}
		if (!response.getId().equals(request.getId())) {
			throw new JsonRpcLayerException(Errors.RESPONSE_JSONRPC_UNEQUAL_IDS);
		}
		if ((response.getJsonrpc() != null) && (!response.getJsonrpc().equals(
				Defaults.JSON_RPC_VERSION))) {
		}
		return response;
	}

	private <T> JsonRpcResponse checkResponse(JsonRpcResponse response) throws BitcoindException {
		if (!(response.getError() == null)) {
			JsonRpcError bitcoindError = response.getError();
			throw new BitcoindException(bitcoindError.getCode(), String.format("Error #%s: %s", 
					bitcoindError.getCode(), bitcoindError.getMessage()));
		}
		return response;
	}
}