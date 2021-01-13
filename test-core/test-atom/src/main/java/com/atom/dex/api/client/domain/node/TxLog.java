
package com.atom.dex.api.client.domain.node;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TxLog {

    @JsonProperty("msg_index")
    public Integer msgIndex;
    @JsonProperty("success")
    public Boolean success;
    @JsonProperty("log")
    public String log;
    @JsonProperty("events")
    public List<Event> events = null;

}
