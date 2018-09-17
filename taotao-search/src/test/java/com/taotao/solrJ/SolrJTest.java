package com.taotao.solrJ;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SolrJTest {

	public void testSolrJ() throws Exception{
		//创建连接 SolrServer ：如果是单机版，创建 HttpSolrServer
		SolrServer solrServer = new HttpSolrServer("http://192.168.171.128:8080/solr");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//添加域
		document.addField("id", "solrTest01");
		document.addField("item_title", "测试商品");
		document.addField("item_sell_point", "卖点");
		//添加到索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	public void testSolrJQuery() throws Exception{
		//创建连接 SolrServer ：如果是单机版，创建 HttpSolrServer
		SolrServer solrServer = new HttpSolrServer("http://192.168.171.128:8080/solr");
		SolrQuery query = new SolrQuery();
		query.setQuery("id:1534215666377");
		
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));

		}
	}
	
	public void test(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		SolrServer solrServer = (SolrServer) applicationContext.getBean("httpSolrServer");
		System.out.println(solrServer);
		
	}
	
}
