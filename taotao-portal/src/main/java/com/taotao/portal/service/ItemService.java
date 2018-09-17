package com.taotao.portal.service;

import com.taotao.pojo.TbItem;

public interface ItemService {
	
	TbItem getItemById(Long itemId);
	String getItemDescById(Long itemId); 
	String getItemParamById(Long itemId);
	
}
