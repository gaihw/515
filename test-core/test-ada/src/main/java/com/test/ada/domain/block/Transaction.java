
package com.test.ada.domain.block;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "block",
    "blockIndex",
    "fee",
    "hash",
    "inputs",
    "outputs",
    "size",
    "totalOutput"
})
public class Transaction {

    @JsonProperty("block")
    public Block_ block;
    @JsonProperty("blockIndex")
    public Integer blockIndex;
    @JsonProperty("fee")
    public Integer fee;
    @JsonProperty("hash")
    public String hash;
    @JsonProperty("inputs")
    public List<Input> inputs = null;
    @JsonProperty("outputs")
    public List<Output> outputs = null;
    @JsonProperty("size")
    public Integer size;
    @JsonProperty("totalOutput")
    public String totalOutput;

}
