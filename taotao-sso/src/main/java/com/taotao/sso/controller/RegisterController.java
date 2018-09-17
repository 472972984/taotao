package com.taotao.sso.controller;

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
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.RegisterService;

/**
 * 发布数据校验Controller
 * 
 * @author 陈汇奇
 *
 */
@Controller
@RequestMapping("user")
public class RegisterController {

	@Autowired
	private RegisterService registerService;

	/**
	 * 接收三个参数。判断callback是否为空。 使用mappingJacksonValue支持jsonp操作
	 * 
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {

		try {
			// 调用服务
			TaotaoResult result = registerService.checkData(param, type);
			// 判断是否是jsonp操作
			if (StringUtils.isNotBlank(callback)) {
				// 将一个对象传入
				MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
				// 设置jsonp方法名称
				jacksonValue.setJsonpFunction("callback");
				return jacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/register" , method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		try {
			TaotaoResult result = registerService.register(user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
