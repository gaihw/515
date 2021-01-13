
package com.test.ada.domain.block;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "blocks"
})
@lombok.Data
public class Data {

    @JsonProperty("blocks")
    public List<Block> blocks = null;

}
