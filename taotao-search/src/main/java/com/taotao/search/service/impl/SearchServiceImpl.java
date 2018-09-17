package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 商品查询 service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {

		// 设置查询条件
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(queryString);

		// 设置分页
		solrQuery.setStart((page - 1) * rows);
		solrQuery.setRows(rows);

		// 设置默认查询域
		solrQuery.set("df", "item_title");

		// 设置高亮
		solrQuery.setHighlight(true);
		// 设置域的哪个为高亮
		solrQuery.addHighlightField("item_title");
		// 设置高亮前缀
		solrQuery.setHighlightSimplePre("<font class=\"skcolor_ljg\">");
		// 设置高亮后缀
		solrQuery.setHighlightSimplePost("</font>");

		// 执行查询
		SearchResult result = searchDao.search(solrQuery);

		// 设置总页数
		int pageCount = (result.getPageCount() / rows);
		if (result.getPageCount() % rows > 0) {
			pageCount++;
		}
		result.setPageCount(pageCount);
		// 设置当前页
		result.setCurPage(page);

		return result;
	}

}
