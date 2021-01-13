
package com.test.ada.domain.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "index",
    "address",
    "value"
})
public class Output {

    @JsonProperty("index")
    public Integer index;
    @JsonProperty("address")
    public String address;
    @JsonProperty("value")
    public String value;

}
