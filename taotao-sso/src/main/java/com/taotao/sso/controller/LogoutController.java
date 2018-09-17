package com.taotao.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.service.LogoutService;

/**
 * 用户安全退出Controller
 * @author 陈汇奇
 *
 */
@Controller
@RequestMapping("/user")
public class LogoutController {
	
	@Autowired
	private LogoutService logoutService;
	
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token , String callback){
		
		try {
			TaotaoResult result = logoutService.logout(token);
			
			//支持jsonp操作
			if(StringUtils.isNotBlank(callback)){
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
