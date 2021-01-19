package com.test;

public class EnumTest {
	public static void main(String[] args) {
//		HttpStatusEnum[] e = HttpStatusEnum.values();
//		System.out.println(HttpStatusEnum.of(424));
//		for (int i = 0; i < e.length; i++) {
//			HttpStatusEnum httpStatusEnum = e[i];
//			System.out.println(httpStatusEnum.getMessage());
//			System.out.println(httpStatusEnum.getCode());
//		}
    	getEnumCode(HttpStatusEnum.PAYLOAD_TOO_LARGE);//413   Bulk request
		System.out.println(HttpStatusEnum.of(200));//OK
		System.out.println(HttpStatusEnum.of(200).getMessage());//success
	}
    public static void getEnumCode(HttpStatusEnum e) {
    	System.out.println(e.getCode());
    	System.out.println(e.getMessage());
    }
}
