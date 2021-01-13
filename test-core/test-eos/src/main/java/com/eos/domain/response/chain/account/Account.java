
package com.eos.domain.response.chain.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "account_name",
    "head_block_num",
    "head_block_time",
    "privileged",
    "last_code_update",
    "created",
    "core_liquid_balance",
    "ram_quota",
    "net_weight",
    "cpu_weight",
    "net_limit",
    "cpu_limit",
    "ram_usage",
    "permissions",
    "total_resources",
    "self_delegated_bandwidth",
    "refund_request",
    "voter_info"
})
public class Account {

    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("head_block_num")
    private Integer headBlockNum;
    @JsonProperty("head_block_time")
    private String headBlockTime;
    @JsonProperty("privileged")
    private Boolean privileged;
    @JsonProperty("last_code_update")
    private String lastCodeUpdate;
    @JsonProperty("created")
    private String created;
    @JsonProperty("core_liquid_balance")
    private String coreLiquidBalance;
    @JsonProperty("ram_quota")
    private Integer ramQuota;
    @JsonProperty("net_weight")
    private Integer netWeight;
    @JsonProperty("cpu_weight")
    private Integer cpuWeight;
    @JsonProperty("net_limit")
    private NetLimit netLimit;
    @JsonProperty("cpu_limit")
    private CpuLimit cpuLimit;
    @JsonProperty("ram_usage")
    private Integer ramUsage;
    @JsonProperty("permissions")
    private List<Permission> permissions = null;
    @JsonProperty("total_resources")
    private Object totalResources;
    @JsonProperty("self_delegated_bandwidth")
    private Object selfDelegatedBandwidth;
    @JsonProperty("refund_request")
    private Object refundRequest;
    @JsonProperty("voter_info")
    private Object voterInfo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("account_name")
    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("account_name")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @JsonProperty("head_block_num")
    public Integer getHeadBlockNum() {
        return headBlockNum;
    }

    @JsonProperty("head_block_num")
    public void setHeadBlockNum(Integer headBlockNum) {
        this.headBlockNum = headBlockNum;
    }

    @JsonProperty("head_block_time")
    public String getHeadBlockTime() {
        return headBlockTime;
    }

    @JsonProperty("head_block_time")
    public void setHeadBlockTime(String headBlockTime) {
        this.headBlockTime = headBlockTime;
    }

    @JsonProperty("privileged")
    public Boolean getPrivileged() {
        return privileged;
    }

    @JsonProperty("privileged")
    public void setPrivileged(Boolean privileged) {
        this.privileged = privileged;
    }

    @JsonProperty("last_code_update")
    public String getLastCodeUpdate() {
        return lastCodeUpdate;
    }

    @JsonProperty("last_code_update")
    public void setLastCodeUpdate(String lastCodeUpdate) {
        this.lastCodeUpdate = lastCodeUpdate;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("core_liquid_balance")
    public String getCoreLiquidBalance() {
        return coreLiquidBalance;
    }

    @JsonProperty("core_liquid_balance")
    public void setCoreLiquidBalance(String coreLiquidBalance) {
        this.coreLiquidBalance = coreLiquidBalance;
    }

    @JsonProperty("ram_quota")
    public Integer getRamQuota() {
        return ramQuota;
    }

    @JsonProperty("ram_quota")
    public void setRamQuota(Integer ramQuota) {
        this.ramQuota = ramQuota;
    }

    @JsonProperty("net_weight")
    public Integer getNetWeight() {
        return netWeight;
    }

    @JsonProperty("net_weight")
    public void setNetWeight(Integer netWeight) {
        this.netWeight = netWeight;
    }

    @JsonProperty("cpu_weight")
    public Integer getCpuWeight() {
        return cpuWeight;
    }

    @JsonProperty("cpu_weight")
    public void setCpuWeight(Integer cpuWeight) {
        this.cpuWeight = cpuWeight;
    }

    @JsonProperty("net_limit")
    public NetLimit getNetLimit() {
        return netLimit;
    }

    @JsonProperty("net_limit")
    public void setNetLimit(NetLimit netLimit) {
        this.netLimit = netLimit;
    }

    @JsonProperty("cpu_limit")
    public CpuLimit getCpuLimit() {
        return cpuLimit;
    }

    @JsonProperty("cpu_limit")
    public void setCpuLimit(CpuLimit cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    @JsonProperty("ram_usage")
    public Integer getRamUsage() {
        return ramUsage;
    }

    @JsonProperty("ram_usage")
    public void setRamUsage(Integer ramUsage) {
        this.ramUsage = ramUsage;
    }

    @JsonProperty("permissions")
    public List<Permission> getPermissions() {
        return permissions;
    }

    @JsonProperty("permissions")
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @JsonProperty("total_resources")
    public Object getTotalResources() {
        return totalResources;
    }

    @JsonProperty("total_resources")
    public void setTotalResources(Object totalResources) {
        this.totalResources = totalResources;
    }

    @JsonProperty("self_delegated_bandwidth")
    public Object getSelfDelegatedBandwidth() {
        return selfDelegatedBandwidth;
    }

    @JsonProperty("self_delegated_bandwidth")
    public void setSelfDelegatedBandwidth(Object selfDelegatedBandwidth) {
        this.selfDelegatedBandwidth = selfDelegatedBandwidth;
    }

    @JsonProperty("refund_request")
    public Object getRefundRequest() {
        return refundRequest;
    }

    @JsonProperty("refund_request")
    public void setRefundRequest(Object refundRequest) {
        this.refundRequest = refundRequest;
    }

    @JsonProperty("voter_info")
    public Object getVoterInfo() {
        return voterInfo;
    }

    @JsonProperty("voter_info")
    public void setVoterInfo(Object voterInfo) {
        this.voterInfo = voterInfo;
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
