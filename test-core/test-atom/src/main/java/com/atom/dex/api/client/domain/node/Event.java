
package com.atom.dex.api.client.domain.node;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Event {

    @JsonProperty("type")
    public String type;
    @JsonProperty("attributes")
    public List<Attribute> attributes = null;

}
