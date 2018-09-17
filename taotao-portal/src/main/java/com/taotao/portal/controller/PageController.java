package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.portal.service.ContentService;

/**
 * 商城欢迎页面Controller
 * @author 陈汇奇
 *
 */
@Controller
public class PageController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/index")
	public String indexPage(Model model){
		//获取大广告位内容
		String json = contentService.getAd1List();
		//传递到页面
		model.addAttribute("ad1", json);
		return "index";
	}
}
