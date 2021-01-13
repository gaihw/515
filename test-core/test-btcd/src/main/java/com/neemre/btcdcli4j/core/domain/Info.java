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
public class Info extends Entity {

	private Integer version;
	@JsonProperty("protocolversion")
	private Integer protocolVersion;
	@JsonProperty("walletversion")
	private Integer walletVersion;
	@Setter(AccessLevel.NONE)
	private BigDecimal balance;
	private Integer blocks;
	@JsonProperty("timeoffset")
	private Integer timeOffset;
	private Integer connections;
	private String proxy;
	@Setter(AccessLevel.NONE)
	private BigDecimal difficulty;
	private Boolean testnet;
	@JsonProperty("keypoololdest")
	private Long keypoolOldest;
	@JsonProperty("keypoolsize")
	private Integer keypoolSize;
	@JsonProperty("unlocked_until")
	private Long unlockedUntil;
	@Setter(AccessLevel.NONE)
	@JsonProperty("paytxfee")
	private BigDecimal payTxFee; 
	@Setter(AccessLevel.NONE)
	@JsonProperty("relayfee")
	private BigDecimal relayFee;
	private String errors;


	public Info(Integer version, Integer protocolVersion, Integer walletVersion, BigDecimal balance,
			Integer blocks, Integer timeOffset, Integer connections, String proxy, 
			BigDecimal difficulty, Boolean testnet, Long keypoolOldest, Integer keypoolSize,
			Long unlockedUntil, BigDecimal payTxFee, BigDecimal relayFee, String errors) {
		setVersion(version);
		setProtocolVersion(protocolVersion);
		setWalletVersion(walletVersion);
		setBalance(balance);
		setBlocks(blocks);
		setTimeOffset(timeOffset);
		setConnections(connections);
		setProxy(proxy);
		setDifficulty(difficulty);
		setTestnet(testnet);
		setKeypoolOldest(keypoolOldest);
		setKeypoolSize(keypoolSize);
		setUnlockedUntil(unlockedUntil);
		setPayTxFee(payTxFee);
		setRelayFee(relayFee);
		setErrors(errors);
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
	}

	public void setPayTxFee(BigDecimal payTxFee) {
		this.payTxFee = payTxFee.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
	}

	public void setRelayFee(BigDecimal relayFee) {
		this.relayFee = relayFee.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
	}
}