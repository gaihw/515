package com.zmj.demo.domain.tool;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SmsEmailcodeDomain {
    private int id;

    private String mobile;

    private String email;

    private String templateParams;

    private int status;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
}

