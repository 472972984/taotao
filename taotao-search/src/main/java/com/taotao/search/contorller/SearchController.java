package com.taotao.search.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 商品查询Controller
 * @author 陈汇奇
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/q")
	@ResponseBody
	// 发布的服务url：    /search/q?keyword=xx&page=x&rows=x
	public TaotaoResult search(@RequestParam(defaultValue="")String keyword, 
			@RequestParam(defaultValue="1")int page ,@RequestParam(defaultValue="30")int rows){
		
		try {
			//解决  keyword  get请求乱码
			byte[] bytes = keyword.getBytes("iso-8859-1");
			keyword = new String(bytes);
			SearchResult result = searchService.search(keyword, page, rows);
			return TaotaoResult.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	
	
	
	
}

