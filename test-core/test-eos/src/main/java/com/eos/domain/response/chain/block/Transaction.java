
package com.eos.domain.response.chain.block;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "cpu_usage_us",
    "net_usage_words",
    "trx"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    @JsonProperty("status")
    private String status;
    @JsonProperty("cpu_usage_us")
    private Long cpuUsageUs;
    @JsonProperty("net_usage_words")
    private Long netUsageWords;
    @JsonProperty("trx")
    private Trx trx;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("cpu_usage_us")
    public Long getCpuUsageUs() {
        return cpuUsageUs;
    }

    @JsonProperty("cpu_usage_us")
    public void setCpuUsageUs(Long cpuUsageUs) {
        this.cpuUsageUs = cpuUsageUs;
    }

    @JsonProperty("net_usage_words")
    public Long getNetUsageWords() {
        return netUsageWords;
    }

    @JsonProperty("net_usage_words")
    public void setNetUsageWords(Long netUsageWords) {
        this.netUsageWords = netUsageWords;
    }

    @JsonProperty("trx")
    public Trx getTrx() {
        return trx;
    }

    @JsonProperty("trx")
    public void setTrx(Trx trx) {
        this.trx = trx;
    }

}
