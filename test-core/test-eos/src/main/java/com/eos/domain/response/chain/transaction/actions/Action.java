
package com.eos.domain.response.chain.transaction.actions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

    "account_action_seq",
    "block_num",
    "block_time",
    "action_trace"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {

    @JsonProperty("account_action_seq")
    private Integer accountActionSeq;
    @JsonProperty("block_num")
    private Integer blockNum;
    @JsonProperty("block_time")
    private String blockTime;
    @JsonProperty("action_trace")
    private ActionTrace actionTrace;

}
