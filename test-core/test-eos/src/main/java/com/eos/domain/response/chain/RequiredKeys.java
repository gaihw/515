package com.eos.domain.response.chain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequiredKeys {

    private List<String> requiredKeys;

    public RequiredKeys(){

    }

    public List<String> getRequiredKeys() {
        return requiredKeys;
    }

    @JsonProperty ("required_keys")
    public void setRequiredKeys(List<String> requiredKeys) {
        this.requiredKeys = requiredKeys;
    }
}
