
package com.eos.domain.response.chain.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "threshold",
    "keys",
    "accounts",
    "waits"
})
public class RequiredAuth {

    @JsonProperty("threshold")
    private Integer threshold;
    @JsonProperty("keys")
    private List<Key> keys = null;
    @JsonProperty("accounts")
    private List<Object> accounts = null;
    @JsonProperty("waits")
    private List<Object> waits = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("threshold")
    public Integer getThreshold() {
        return threshold;
    }

    @JsonProperty("threshold")
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @JsonProperty("keys")
    public List<Key> getKeys() {
        return keys;
    }

    @JsonProperty("keys")
    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    @JsonProperty("accounts")
    public List<Object> getAccounts() {
        return accounts;
    }

    @JsonProperty("accounts")
    public void setAccounts(List<Object> accounts) {
        this.accounts = accounts;
    }

    @JsonProperty("waits")
    public List<Object> getWaits() {
        return waits;
    }

    @JsonProperty("waits")
    public void setWaits(List<Object> waits) {
        this.waits = waits;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
