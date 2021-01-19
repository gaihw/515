package com.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class New{
	public static void main(String[] args) {
		String str = "event=SUB, product=2001, type=kline, msg=success, code=0, business=regular";
		String json = JSON.toJSONString(str, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		System.out.println(json);
	}
}
