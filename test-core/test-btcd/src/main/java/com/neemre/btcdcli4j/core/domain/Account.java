package com.neemre.btcdcli4j.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neemre.btcdcli4j.core.common.Defaults;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account extends Entity {

	@JsonProperty("involvesWatchonly")
	private Boolean involvesWatchOnly;
	private String account;
	@Setter(AccessLevel.NONE)
	private BigDecimal amount;
	private Integer confirmations;


	public Account(Boolean involvesWatchOnly, String account, BigDecimal amount, 
			Integer confirmations) {
		setInvolvesWatchOnly(involvesWatchOnly);
		setAccount(account);
		setAmount(amount);
		setConfirmations(confirmations);
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
	}
}