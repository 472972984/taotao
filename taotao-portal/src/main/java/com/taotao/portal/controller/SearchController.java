package com.taotao.portal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 发布商品查询 Controller
 * @author 陈汇奇
 *
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q")String keyword , 
			@RequestParam(defaultValue="1")int page, @RequestParam(defaultValue="60")int rows,Model model){

		//解决get请求乱码
		try {
			keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			keyword = "";
			e.printStackTrace();
		}
		SearchResult searchResult = searchService.search(keyword, page, rows);
		
		//将信息发送到页面上
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		
		return "search";
	}

}
