
package com.eos.domain.response.chain.push;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "account",
    "name",
    "authorization",
    "data",
    "hex_data"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Act_ {

    @JsonProperty("account")
    private String account;
    @JsonProperty("name")
    private String name;
    @JsonProperty("authorization")
    private List<Authorization_> authorization = null;
    @JsonProperty("data")
    private Data_ data;
    @JsonProperty("hex_data")
    private String hexData;

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("authorization")
    public List<Authorization_> getAuthorization() {
        return authorization;
    }

    @JsonProperty("authorization")
    public void setAuthorization(List<Authorization_> authorization) {
        this.authorization = authorization;
    }

    @JsonProperty("data")
    public Data_ getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data_ data) {
        this.data = data;
    }

    @JsonProperty("hex_data")
    public String getHexData() {
        return hexData;
    }

    @JsonProperty("hex_data")
    public void setHexData(String hexData) {
        this.hexData = hexData;
    }

}
