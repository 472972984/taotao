package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.service.StaticPageService;

/**
 * 发布freemarker自动生成静态html文件Controller
 * @author 陈汇奇
 *
 */
@Controller
public class StaticPageController {
	
	@Autowired
	private StaticPageService pageService;

	@RequestMapping("/gen/item/{itemId}")
	@ResponseBody
	public TaotaoResult genItemPage(@PathVariable Long itemId) {
		
		try {
			TaotaoResult taotaoResult = pageService.genItemHtml(itemId);
			return taotaoResult;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
}
