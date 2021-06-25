package com.zmj.demo.service;

import com.zmj.demo.domain.BaseDomain;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BaseService {

    List<BaseDomain> getList();
}
