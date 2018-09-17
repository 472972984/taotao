package com.taotao.search.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.ItemService;

/**
 * 发布商品信息controller
 * @author 陈汇奇
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/**
	 * 发布商品信息添加至solr服务
	 * @return
	 */
	@RequestMapping("/importall")
	@ResponseBody
	public TaotaoResult ImportAll(){
		
		try {
			TaotaoResult result = itemService.importItems();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
}
