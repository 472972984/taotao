package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchItem;
import com.taotao.search.pojo.SearchResult;

/**
 * solr查询DAO
 * 
 * @author 陈汇奇
 *
 */
@Repository
public class SearchDaoImpl implements SearchDao {

	@Autowired
	private SolrServer solrServer;

	@Override
	public SearchResult search(SolrQuery query) throws Exception {

		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		List<SearchItem> searchItemList = new ArrayList<SearchItem>();

		for (SolrDocument solrDocument : solrDocumentList) {

			SearchItem item = new SearchItem();

			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setId((String) solrDocument.get("id"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((Long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));

			// 取高亮显示
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			
			item.setTitle(itemTitle);
			
			searchItemList.add(item);
		}

		SearchResult result = new SearchResult();
		result.setItemList(searchItemList);
		result.setRecordCount(solrDocumentList.getNumFound());

		return result;
	}

}
