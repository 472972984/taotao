package com.taotao.rest.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.util.JsonUtils;
import com.taotao.rest.pojo.ItemCatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 商品分类Controller
 * @author 陈汇奇
 *
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private  ItemCatService itemCatService;
	
	/*@RequestMapping("/list")
	@ResponseBody
	public ItemCatResult getItemCatList(String callback){
		ItemCatResult result = itemCatService.getItemCatList();
		return result;
	}
	
	
	@RequestMapping("/index")
	public void test(){
		System.out.println("来过了");
	}
	*/
	
	//设置请求响应头 application/json 
	@RequestMapping(value="/list", produces={MediaType.APPLICATION_JSON_VALUE+";charset=utf-8"})
	@ResponseBody
	public String getItemCatList(String callback){
		
		System.err.println("callback:  "+callback);
		
		ItemCatResult result = itemCatService.getItemCatList();

		if(callback == null){
			//如果没有请求参数。说明他可能直接申请一串json数据
			String json = JsonUtils.objectToJson(result);
			return json;
		}
		
		//如果有参数,拼接请求的 js 片段
		//如果字符串不为空，需要支持jsonp调用
		String json = JsonUtils.objectToJson(result);
		return callback +"("+json+");";
		
	}
	

}
