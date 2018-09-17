package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.portal.service.LogoutService;

/**
 * 用户安全退出 Controller
 * @author 陈汇奇
 *
 */
@Controller
public class LogoutController {

	@Autowired
	private LogoutService logoutService;
	
	@RequestMapping("/user/logout")
	public String logout(String token){
		logoutService.logout(token);
		return "redirect:/";
	}
	
}
