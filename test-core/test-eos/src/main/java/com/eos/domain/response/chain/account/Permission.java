
package com.eos.domain.response.chain.account;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "perm_name",
    "parent",
    "required_auth"
})
public class Permission {

    @JsonProperty("perm_name")
    private String permName;
    @JsonProperty("parent")
    private String parent;
    @JsonProperty("required_auth")
    private RequiredAuth requiredAuth;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("perm_name")
    public String getPermName() {
        return permName;
    }

    @JsonProperty("perm_name")
    public void setPermName(String permName) {
        this.permName = permName;
    }

    @JsonProperty("parent")
    public String getParent() {
        return parent;
    }

    @JsonProperty("parent")
    public void setParent(String parent) {
        this.parent = parent;
    }

    @JsonProperty("required_auth")
    public RequiredAuth getRequiredAuth() {
        return requiredAuth;
    }

    @JsonProperty("required_auth")
    public void setRequiredAuth(RequiredAuth requiredAuth) {
        this.requiredAuth = requiredAuth;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
