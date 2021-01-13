
package com.atom.dex.api.client.domain.node;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attribute {

    @JsonProperty("key")
    public String key;
    @JsonProperty("value")
    public String value;

}
