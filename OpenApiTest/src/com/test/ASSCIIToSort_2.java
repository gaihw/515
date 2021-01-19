package com.test;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONObject;





public class ASSCIIToSort_2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str ="{\"aazfff\":0,\"ad\":1,\"aac\":2,\"d\":3}";
		TreeMap<String,Integer> treemap = new TreeMap<String, Integer>();
		JSONObject jsonb = JSONObject.fromObject(str);
		Set<String> set = jsonb.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			treemap.put(key, (Integer) jsonb.get(key));
		}
		Set<String> set1 = treemap.keySet();
		Iterator it1 = set.iterator();
		while(it1.hasNext()) {
			String key1 = (String) it1.next();
			System.out.println(key1+"="+treemap.get(key1));
		}
	}

}
