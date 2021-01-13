package com.neemre.omni.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmniProperty {

    private Long propertyid;
    private String name;
    private String category;
    private String subcategory;
    private String data;
    private String url;
    private boolean divisible;
    private String issuer;
    private String creationtxid;
    private boolean fixedissuance;
    private boolean managedissuance;
    private BigDecimal totaltokens;

}