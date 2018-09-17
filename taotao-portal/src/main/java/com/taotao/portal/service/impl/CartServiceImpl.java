package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.ItemService;
import com.taotao.portal.service.UserService;

/**
 * 添加购物车实现 Service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ItemService itemService;

	@Value("${COOKIE_MAX_AGE}")
	private Integer COOKIE_MAX_AGE;

	@Autowired
	private UserService userService;

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	@Value("${CART_CACHE_URL}")
	private String CART_CACHE_URL;

	@Value("${UPLOAD_CART_CACHE_URL}")
	private String UPLOAD_CART_CACHE_URL;

	/**
	 * 添加购物车信息至cookie中
	 */
	@Override
	public TaotaoResult addCart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {

		try {
			// 判断用户是否登录
			TbUser user = userService.getUserByToken(request, response);
			if (user != null) {
				// 用户已经登录
				// 查询缓存购物车信息
				String json = HttpClientUtil.doGet(REST_BASE_URL + CART_CACHE_URL + user.getUsername());

				// 转换成java对象
				TaotaoResult result = TaotaoResult.format(json);
				// 如果购物车信息不为空
				if (result.getData() != null) {
					// 转换成对象
					//result = TaotaoResult.formatToList(json, CartItem.class);
					//List<CartItem> itemListInCache = (List<CartItem>) result.getData();
					String jsonCartList = (String) result.getData();
					List<CartItem> itemListInCache = JsonUtils.jsonToList(jsonCartList, CartItem.class);
					
					boolean flag = false;
					// 判断购物车是否存在相同商品：
					for (CartItem cartItem : itemListInCache) {
						// 如果存在数量相加
						if (cartItem.getId().longValue() == itemId) {
							cartItem.setNum(cartItem.getNum() + num);
							flag = true;
							break;
						}
					}
					//如果不存在相同商品
					if(!flag){
						//添加至列表中
						itemListInCache = addToList(itemId, num, itemListInCache);
					}
					//将列表信息添加至cookie中
					CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemListInCache), COOKIE_MAX_AGE, true);
					//向服务层提交购物车信息提交缓存  调用此服务的url格式：http://localhost:8081/rest/cart/addcart  method = post
					uploadCart(itemListInCache, user.getUsername());
					return TaotaoResult.ok();
				} else {
					// 如果购物车信息为空。创建一个列表添加 , 将列表信息添加至cookie中
					List<CartItem> itemListNew = addToList(itemId, num, null);
					CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemListNew), COOKIE_MAX_AGE, true);
					//向服务层提交购物车信息提交缓存
					uploadCart(itemListNew, user.getUsername());
					return TaotaoResult.ok();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		//如果用户没有登录。将购物车信息存放至cookie中
		// 从cookie中获取购物车信息列表
		List<CartItem> itemList = getCartItemList(request);

		// 设置一个标记，购物车中不存在某个商品
		boolean haveFlg = false;

		// 遍历查看购物车中是否存在正在添加的商品
		for (CartItem cartItem : itemList) {
			// 如果存在
			if (cartItem.getId().longValue() == itemId) {
				// 实现商品数量更正
				cartItem.setNum(cartItem.getNum() + num);
				haveFlg = true;
				break;
			}
		}

		// 如果遍历之后还是不存在
		if(!haveFlg){
			addToList(itemId, num, itemList);
		}
		
		// 放入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), COOKIE_MAX_AGE, true);

		return TaotaoResult.ok();
	}

	/**
	 * 返回购物车列表
	 * 
	 * @param request
	 * @return
	 */
	private List<CartItem> getCartItemList(HttpServletRequest request) {

		try {
			// 获取cookie中购物车数据
			String json = CookieUtils.getCookieValue(request, "TT_CART", true);

			List<CartItem> cartList = JsonUtils.jsonToList(json, CartItem.class);

			if (cartList != null) {
				return cartList;
			} else {
				return new ArrayList<CartItem>();
			}

		} catch (Exception e) {
			return new ArrayList<CartItem>();
		}

	}

	/**
	 * 添加商品至列表中
	 * @param itemId 添加的商品id
	 * @param num  添加的商品数量
	 * @param itemList 商品列表
	 * @return
	 */
	private List<CartItem> addToList(long itemId, Integer num, List<CartItem> itemList) {
		// 如果传入的list为空
		if (itemList == null) {
			itemList = new ArrayList<CartItem>();
		}
		
		CartItem cartItem = new CartItem();

		// 调用现有ItemService获取用户信息
		TbItem item = itemService.getItemById(itemId);

		cartItem.setId(itemId);
		cartItem.setNum(num);
		cartItem.setPrice(item.getPrice());
		cartItem.setTitle(item.getTitle());

		// 设置一张图片
		if (StringUtils.isNotBlank(item.getImage())) {
			String[] images = item.getImage().split(",");
			cartItem.setImage(images[0]);
		}

		// 添加至表列中
		itemList.add(cartItem);
		return itemList;
		
	}
	
	/**
	 * 向rest服务层提交购物车信息 让其保存到缓存
	 */
	private void uploadCart(List<CartItem> cartItems , String username){
		//定义需要发送的参数
		Map<String, String> param = new HashMap<String, String>();
		//设置参数
		param.put("username", username);
		param.put("cartListJson", JsonUtils.objectToJson(cartItems));
//		HttpClientUtil.doPost(REST_BASE_URL+UPLOAD_CART_CACHE_URL, param);
		HttpClientUtil.doPostDealErrorCode(REST_BASE_URL+UPLOAD_CART_CACHE_URL, param);
	}
	
	
	@Override
	public List<CartItem> getCartItems(HttpServletRequest request) {
		List<CartItem> list = getCartItemList(request);
		return list;
	}

	/**
	 * 更新购物车数量信息
	 */
	@Override
	public TaotaoResult updateCartItem(long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {

		// 从cookie中获取购物车列表信息
		List<CartItem> itemList = getCartItemList(request);

		// 遍历查找该商品
		for (CartItem cartItem : itemList) {
			if (cartItem.getId() == itemId) {
				// 重新设置 num 属性
				cartItem.setNum(num);
				break;
			}
		}

		// 将列表信息重新写入cookie中
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), COOKIE_MAX_AGE, true);

		return TaotaoResult.ok();
	}

	/**
	 * 根据商品id，删除购物车信息
	 */
	@Override
	public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {

		// 获取cookie中购物车信息
		List<CartItem> itemList = getCartItemList(request);

		for (CartItem cartItem : itemList) {
			// 遍历查找该商品。从列表中移除
			if (cartItem.getId() == itemId) {
				// 移除
				itemList.remove(cartItem);
				break;
			}
		}

		// 将列表信息重新写入cookie中
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), COOKIE_MAX_AGE, true);
		return TaotaoResult.ok();

	}

}
