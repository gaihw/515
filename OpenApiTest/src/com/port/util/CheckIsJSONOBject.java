package com.port.util;

import net.sf.json.JSONObject;

public class CheckIsJSONOBject {
	public static boolean checkIsJSONObject(String getResponseBodyAsString) {
		JSONObject response = JSONObject.fromObject(getResponseBodyAsString);
		if(response.equals("")||response.equals(null)||response.isEmpty()) {
			System.out.println("接口返回数据为空，或者不是json格式，请查看！<"+response+">");
			return false;
		}else {
			return true;
		}
	}
}
