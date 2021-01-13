
package com.eos.domain.response.chain.transaction;

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
    "signatures",
    "compression",
    "packed_context_free_data",
    "packed_trx"
})
public class Trx_ {

    @JsonProperty("signatures")
    private List<String> signatures = null;
    @JsonProperty("compression")
    private String compression;
    @JsonProperty("packed_context_free_data")
    private String packedContextFreeData;
    @JsonProperty("packed_trx")
    private String packedTrx;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("signatures")
    public List<String> getSignatures() {
        return signatures;
    }

    @JsonProperty("signatures")
    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    @JsonProperty("compression")
    public String getCompression() {
        return compression;
    }

    @JsonProperty("compression")
    public void setCompression(String compression) {
        this.compression = compression;
    }

    @JsonProperty("packed_context_free_data")
    public String getPackedContextFreeData() {
        return packedContextFreeData;
    }

    @JsonProperty("packed_context_free_data")
    public void setPackedContextFreeData(String packedContextFreeData) {
        this.packedContextFreeData = packedContextFreeData;
    }

    @JsonProperty("packed_trx")
    public String getPackedTrx() {
        return packedTrx;
    }

    @JsonProperty("packed_trx")
    public void setPackedTrx(String packedTrx) {
        this.packedTrx = packedTrx;
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
