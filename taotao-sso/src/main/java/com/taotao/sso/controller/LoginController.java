package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.service.LoginService;

/**
 * 用户登录Controller
 * 
 * @author 陈汇奇
 *
 */
@Controller
@RequestMapping("/user")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			TaotaoResult result = loginService.login(username, password, request, response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token , String callback){
		
		try {
			TaotaoResult result = loginService.getUserByToken(token);
			
			//支持jsonp操作
			if(StringUtils.isNotBlank(callback)){
				//传入json数据
				MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
				jacksonValue.setJsonpFunction(callback);
				return jacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
	
	
	
	
	
}
