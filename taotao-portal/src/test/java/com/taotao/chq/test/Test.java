package com.taotao.chq.test;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.portal.pojo.CartItem;

public class Test {

	public static void main(String[] args) throws Exception {

		// 对cookie进行转码
//		String s = "%5B%7B%22id%22%3A153568586904273%2C%22title%22%3A%22%E9%99%88%E6%B1%87%E5%A5%87%E6%89%8B%E6%9C%BA%E7%95%85%E9%94%80%E4%B8%AD%22%2C%22price%22%3A888800%2C%22num%22%3A1%2C%22image%22%3A%22http%3A%2F%2F192.168.171.129%2Fgroup1%2FM00%2F00%2F00%2FwKirgVuFhOeAdy2qAAJCKKoGJsQ375.jpg%22%7D%5D";
//
//		String str = URLDecoder.decode(s, "UTF-8");
//		System.out.println(str);
		
//		String s="%5B%7B%22id%22%3A153568586904273%2C%22title%22%3A%22%3F%3F%3F%3F%3F%3F%3F%3F%22%2C%22price%22%3A888800%2C%22num%22%3A2%2C%22image%22%3A%22http%3A%2F%2F192.168.171.129%2Fgroup1%2FM00%2F00%2F00%2FwKirgVuFhOeAdy2qAAJCKKoGJsQ375.jpg%22%7D%5D";
//		
//		String str = URLDecoder.decode(s, "UTF-8");
//		System.out.println(str);

		// 获取本地其他浏览器cookie
		// Connection conn = Jsoup.connect("https://blog.csdn.net");
		// conn.method(Method.GET);
		// conn.followRedirects(false);
		// Response response = conn.execute();
		// System.out.println(response.cookies());
		// {uuid_tt_dd=10_30599088290-1536452857097-443114,
		// dc_session_id=10_1536452857097.309589}
		/*
		 * HttpClient client = new DefaultHttpClient(); GetMethod get = new
		 * GetMethod("http://www.17sct.com/city.php?ename=CHANGZHOU");
		 * get.setFollowRedirects(false); client.executeMethod(get);
		 * System.out.println(Arrays.toString(client.getState().getCookies()));
		 * //[PHPSESSID=cn74fv516879pv26h5lbaf9gd0, 99a0_city=269] //立即过期的不显示出来
		 */
		
//		TaotaoResult result = TaotaoResult.ok();
//		System.out.println(result.getData());
		
//		String json = "[{\"id\": 153568586904273,\"title\": \"????????\",\"price\": 888800,\"num\": 1,\"image\": \"http://192.168.171.129/group1/M00/00/00/wKirgVuFhOeAdy2qAAJCKKoGJsQ375.jpg\"},{\"id\": 153568586904273,\"title\": \"????????\",\"price\": 888800,\"num\": 1,\"image\": \"http://192.168.171.129/group1/M00/00/00/wKirgVuFhOeAdy2qAAJCKKoGJsQ375.jpg\"}]";
//		List<CartItem> list = JsonUtils.jsonToList(json, CartItem.class);
//		System.out.println("");
		
		List<CartItem> cartItems = new ArrayList<CartItem>();
		CartItem item = new CartItem();
		item.setTitle("我来了");
		item.setId(123456789l);
		item.setImage("image");
		item.setNum(2);
		item.setPrice(12345600l);
		cartItems.add(item);
		
		String json = JsonUtils.objectToJson(cartItems);
		
		Map<String, String> param = new HashMap<String, String>();
		//设置参数
		param.put("username", "472972984");
		param.put("cartListJson", JsonUtils.objectToJson(cartItems));
		
//		HttpClientUtil.doPost("http://localhost:8081/rest/cart/addcart", param);
		HttpClientUtil.doPostDealErrorCode("http://localhost:8081/rest/cart/addcart", param);
		
	}

}
