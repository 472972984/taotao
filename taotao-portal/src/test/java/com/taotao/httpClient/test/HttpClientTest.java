package com.taotao.httpClient.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

	
//	@Test
	public void test() throws Exception {

		// 创建一个 httpClient 对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建 get 对象
		HttpGet get = new HttpGet("http://localhost:8080");

		// 执行请求 得到HttpResponse
		CloseableHttpResponse httpResponse = httpClient.execute(get);

		// 接收返回结果
		HttpEntity entity = httpResponse.getEntity();

		// 取响应结果内容
		String html = EntityUtils.toString(entity);
		System.err.println(html);

		// 关闭
		httpResponse.close();
		httpClient.close();

	}
	
	
//	@Test
	public void testPost() throws Exception{
		
		 
		//第一步：创建一个httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//第二步：创建一个HttpPost对象。需要指定一个url
		HttpPost post = new HttpPost("http://localhost:8080/content/save");
		//第三步：创建一个list模拟表单，list中每个元素是一个NameValuePair对象
		List<NameValuePair> formList = new ArrayList<NameValuePair>();
		
		formList.add(new BasicNameValuePair("categoryId", "89"));
		formList.add(new BasicNameValuePair("title", "title"));
		formList.add(new BasicNameValuePair("subTitle", "subTitle"));
		formList.add(new BasicNameValuePair("titleDesc", "titleDesc"));
		formList.add(new BasicNameValuePair("url", "url"));
		formList.add(new BasicNameValuePair("pic", "pic"));
		formList.add(new BasicNameValuePair("pic2", "pic2"));
		formList.add(new BasicNameValuePair("content", "content"));
		
		//第四步：需要把表单包装到Entity对象中。StringEntity
		StringEntity entity = new UrlEncodedFormEntity(formList);
		//加入表单值
		post.setEntity(entity);
		//第五步：执行请求。 执行之前需要将表单值加入到post请求中
		CloseableHttpResponse response = httpClient.execute(post);
		//第六步：接收返回结果
		HttpEntity result = response.getEntity();
		String resultString = EntityUtils.toString(result);
		System.err.println(resultString);
		//第七步：关闭流。
		response.close();
		httpClient.close();
	}
	
	

}
