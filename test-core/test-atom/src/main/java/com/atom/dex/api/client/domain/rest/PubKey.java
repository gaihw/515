package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubKey {
    private String type;
    private String  value;
}
