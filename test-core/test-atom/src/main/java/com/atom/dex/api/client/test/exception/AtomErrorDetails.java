package com.atom.dex.api.client.test.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class AtomErrorDetails {

    private String message;

    private String file;

    private Integer lineNumber;

    private String method;

    private AtomErrorDetails() {

    }

}
