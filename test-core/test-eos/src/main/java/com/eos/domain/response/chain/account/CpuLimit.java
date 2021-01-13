
package com.eos.domain.response.chain.account;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "used",
    "available",
    "max"
})
public class CpuLimit {

    @JsonProperty("used")
    private Long used;
    @JsonProperty("available")
    private Long available;
    @JsonProperty("max")
    private Long max;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("used")
    public Long getUsed() {
        return used;
    }

    @JsonProperty("used")
    public void setUsed(Long used) {
        this.used = used;
    }

    @JsonProperty("available")
    public Long getAvailable() {
        return available;
    }

    @JsonProperty("available")
    public void setAvailable(Long available) {
        this.available = available;
    }

    @JsonProperty("max")
    public Long getMax() {
        return max;
    }

    @JsonProperty("max")
    public void setMax(Long max) {
        this.max = max;
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
