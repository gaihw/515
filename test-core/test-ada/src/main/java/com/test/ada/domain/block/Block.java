
package com.test.ada.domain.block;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hash",
    "number",
    "fees",
    "transactions"
})
public class Block {

    @JsonProperty("hash")
    public String hash;
    @JsonProperty("number")
    public Integer number;
    @JsonProperty("fees")
    public Integer fees;
    @JsonProperty("transactions")
    public List<Transaction> transactions = null;

}
