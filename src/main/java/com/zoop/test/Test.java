package com.zoop.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Test {

	public static void main(String[]args) {
//		TestA a = new TestA();
//		a.m1();
//		a.m1();
//		TestA b = new TestA();
//		b.m1();
//		System.out.println(TestA.m);
//		String atr = " jj ";
//		System.out.println(atr.trim());
		Map<String, Object> m = new HashMap<String,Object>();
		m.put("id", "11");
		m.put("name",null);
		String str = JSON.toJSONString(m);
		System.out.println(str);
		
	}
	
}
