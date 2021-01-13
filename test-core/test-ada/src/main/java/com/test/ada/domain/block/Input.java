
package com.test.ada.domain.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "address",
    "sourceTxIndex",
    "sourceTxHash",
    "value"
})
public class Input {

    @JsonProperty("address")
    public String address;
    @JsonProperty("sourceTxIndex")
    public Integer sourceTxIndex;
    @JsonProperty("sourceTxHash")
    public String sourceTxHash;
    @JsonProperty("value")
    public String value;

}
