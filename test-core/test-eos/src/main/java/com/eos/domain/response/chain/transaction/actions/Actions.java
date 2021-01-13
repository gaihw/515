
package com.eos.domain.response.chain.transaction.actions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "actions",
    "last_irreversible_block"
})
public class Actions {

    @JsonProperty("actions")
    public List<Action> actions = null;
    @JsonProperty("last_irreversible_block")
    public Integer lastIrreversibleBlock;

}
