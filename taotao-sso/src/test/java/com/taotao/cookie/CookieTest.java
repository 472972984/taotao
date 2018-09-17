package com.taotao.cookie;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;

public class CookieTest{

	public static void main(String[] args) {
		
		TaotaoResult result = TaotaoResult.ok("我来了");
		String json = JsonUtils.objectToJson(result);
		System.out.println(json);
		
		TaotaoResult format = TaotaoResult.format(json);
		System.out.println(format);
		
	}
}
