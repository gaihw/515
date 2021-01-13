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
public class OmniTransaction {
    private String txid;
    private BigDecimal fee;
    private String sendingaddress;
    private String referenceaddress;
    private Boolean ismine;
    private Integer version;
    private Integer type_int;
    private String type;
    private Long propertyid;
    private Boolean divisible;
    private BigDecimal amount;
    private Boolean valid;
    private String blockhash;
    private Long blocktime;
    private Integer positioninblock;
    private Integer block;
    private Integer confirmations;
}
