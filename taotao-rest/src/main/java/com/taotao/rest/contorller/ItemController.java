package com.taotao.rest.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.rest.service.ItemService;

/**
 * 发布商品基本信息Controller
 * 
 * @author 陈汇奇
 *
 */
@Controller
@RequestMapping("item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 接收商品id,查询基本信息，包装返回
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/base/{itemId}")
	@ResponseBody
	public TaotaoResult getItemById(@PathVariable("itemId") Long itemId) {
		try {
			TbItem item = itemService.getItemById(itemId);
			return TaotaoResult.ok(item);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 接收商品id，查询 TbItemParamItem 商品参数信息 ，包装返回
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/param/{itemId}")
	@ResponseBody
	public TaotaoResult getItemParamById(@PathVariable Long itemId) {
		try {
			TbItemParamItem itemParamItem = itemService.getItemParamById(itemId);
			return TaotaoResult.ok(itemParamItem);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 *  接收商品id，查询 TbItemDesc 商品描述信息 ，包装返回
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/desc/{itemId}")
	@ResponseBody
	public TaotaoResult getItemDescById(@PathVariable Long itemId) {
		try {
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			return TaotaoResult.ok(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
