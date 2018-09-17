package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbItem;
import com.taotao.portal.service.ItemService;

/**
 * 商品信息Controller
 * @author 陈汇奇
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService	itemService;
	
	@RequestMapping("/item/{itemId}")
	public String getItemById(@PathVariable Long itemId,Model model){
		
		TbItem item = itemService.getItemById(itemId);
		model.addAttribute("item", item);
		return "item";
	}
	
	@RequestMapping(value="/item/desc/{itemId}",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemDescById(@PathVariable Long itemId){
		String desc = itemService.getItemDescById(itemId);
		return desc;
	}
	
	@RequestMapping(value="/item/param/{itemId}",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemParamById(@PathVariable Long itemId){
		String html = itemService.getItemParamById(itemId);
		return html;
	}
	
	
}
