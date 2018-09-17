package com.taotao.portal.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.portal.service.ItemService;
import com.taotao.portal.service.StaticPageService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 发布freemarker服务自动生成静态html文件的service
 * @author 陈汇奇
 *
 */
@Service
public class StaticPageServiceImpl implements StaticPageService {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Value("${STATIC_PAGE_PATH}")
	private String STATIC_PAGE_PATH;
	
	@Override
	public TaotaoResult genItemHtml(Long itemId) throws Exception {
		
		//获取基本信息
		TbItem item = itemService.getItemById(itemId);
		//获取描述信息
		String itemDesc = itemService.getItemDescById(itemId);
		//获取参数列表
		String itemParam = itemService.getItemParamById(itemId);
		
		//获取configuration对象
		Configuration configuration = freeMarkerConfig.getConfiguration();
		//获取模板对象
		Template template = configuration.getTemplate("item.ftl");
		//设置参数
		Map root = new HashMap();
		
		root.put("item", item);
		root.put("itemDesc", itemDesc);
		root.put("itemParam", itemParam);

		//创建writer对象
		Writer out = new FileWriter(new File(STATIC_PAGE_PATH+itemId+".html"));
		
		//执行
		template.process(root, out);
		
		out.flush();
		out.close();
		
		return TaotaoResult.ok();
	}

}
