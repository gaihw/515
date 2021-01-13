
package com.eos.domain.response.chain.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "cpu_usage_us",
    "net_usage_words"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Receipt {

    @JsonProperty("status")
    private String status;
    @JsonProperty("cpu_usage_us")
    private Integer cpuUsageUs;
    @JsonProperty("net_usage_words")
    private Integer netUsageWords;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("cpu_usage_us")
    public Integer getCpuUsageUs() {
        return cpuUsageUs;
    }

    @JsonProperty("cpu_usage_us")
    public void setCpuUsageUs(Integer cpuUsageUs) {
        this.cpuUsageUs = cpuUsageUs;
    }

    @JsonProperty("net_usage_words")
    public Integer getNetUsageWords() {
        return netUsageWords;
    }

    @JsonProperty("net_usage_words")
    public void setNetUsageWords(Integer netUsageWords) {
        this.netUsageWords = netUsageWords;
    }

}
