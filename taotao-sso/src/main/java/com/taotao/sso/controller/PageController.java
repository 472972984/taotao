package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 单点登录系统页面展示  Controller  
 * @author 陈汇奇
 *
 */
@Controller
public class PageController {
	
	@RequestMapping("/page/login")
	public String showLogin(String redirect,Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}

}
