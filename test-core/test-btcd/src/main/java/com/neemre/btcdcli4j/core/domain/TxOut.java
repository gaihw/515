package com.neemre.btcdcli4j.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.neemre.btcdcli4j.core.common.Defaults;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = false )
@JsonInclude ( Include.NON_NULL )
@JsonIgnoreProperties ( ignoreUnknown = true )
public class TxOut extends Entity {

    @Setter( AccessLevel.NONE )
    private BigDecimal value;
    private String bestblock;
    private Integer confirmations;
    private PubKeyScript scriptPubKey;

    public TxOut(String bestblock, BigDecimal value, Integer confirmations, PubKeyScript scriptPubKey) {
        this.bestblock = bestblock;
        setValue(value);
        this.confirmations = confirmations;
        setScriptPubKey(scriptPubKey);
    }

    public void setValue(BigDecimal value) {
        this.value = value.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }


}