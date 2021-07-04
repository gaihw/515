package com.zmj.demo;


import com.zmj.demo.enums.MessageEnum;

public class Test {
    public static void main(String[] args) {
        System.out.println(MessageEnum.ERROR_PLATFORM.getDesc());
        System.out.println(MessageEnum.ERROR_PLATFORM.getType());
    }
}
