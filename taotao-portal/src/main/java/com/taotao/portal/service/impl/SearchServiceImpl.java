package com.taotao.portal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 调用商品查询service
 * @author 陈汇奇
 *
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) {
		//封装请求参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("keyword", keyword);
		param.put("page", page+"");
		param.put("rows", rows+"");
		
		String json = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
		
		//将json数据转换成java对象
		TaotaoResult toPojo = TaotaoResult.formatToPojo(json, SearchResult.class);
		
		//得到SearchResult对象
		SearchResult result = (SearchResult) toPojo.getData();
		
		return result;
	}

}
