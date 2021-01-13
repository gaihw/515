package com.eos.utils.cypto.util;

public class RefValue<T> {
    public T data;

    public RefValue(){
        data = null;
    }

    public RefValue(T initialVal ){
        data = initialVal;
    }
}