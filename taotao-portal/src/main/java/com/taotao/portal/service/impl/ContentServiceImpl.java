package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AdNode;
import com.taotao.portal.service.ContentService;

/**
 * tbContent内容Service
 * @author 陈汇奇
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	
	@Value("${REST_BASE_URL}") //从配置文件中加载信息
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_URL}")
	private String REST_CONTENT_URL;
	@Value("${REST_CONTENT_AD1_CID}")
	private String REST_CONTENT_AD1_CID;

	/**
	 * 获得大广告位信息
	 * 返回json数据
	 */
	@Override
	public String getAd1List() {
		
		//硬编码 需要写入配置文件中
		String json = HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_URL+REST_CONTENT_AD1_CID);
		
		//将 json 数据取出 ,变为TaotaoResult对象
//		TaotaoResult result = JsonUtils.jsonToPojo(json, TaotaoResult.class);
		TaotaoResult taotaoResult = TaotaoResult.formatToList(json, TbContent.class);
		//取出data
		List<TbContent> contents =  (List<TbContent>) taotaoResult.getData();
		
		List<AdNode> adNodes = new ArrayList<AdNode>();
		//封装成 AdNode
		for (TbContent tbContent : contents) {
			
			AdNode node = new AdNode();
			
			node.setHeight(240);
			node.setWedth(670);
			
			node.setHeight2(550);
			node.setWedth2(670);
			
			//图片url
			node.setSrc(tbContent.getPic());
			node.setSrc2(tbContent.getPic2());
			node.setAlt(tbContent.getSubTitle());
			node.setHref(tbContent.getUrl());
			
			adNodes.add(node);
		}
			
		//转换成json
		String resultJson = JsonUtils.objectToJson(adNodes);
		
		return resultJson;
	}

}
