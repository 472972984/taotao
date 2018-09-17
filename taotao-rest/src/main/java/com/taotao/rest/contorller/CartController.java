package com.taotao.rest.contorller;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.rest.service.CartService;

/**
 * 发布缓存购物车信息服务 Controller
 * @author 陈汇奇
 *
 */
@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	/**
	 * 设置缓存中的购物车信息
	 * 其他项目调用此服务的url格式：http://localhost:8081/rest/cart/addcart  method = post
	 * @param cartListJson: 购物车信息json字符串
	 * @param user：用户名
	 * @return
	 */
	@RequestMapping(value ="/cart/addcart",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult setCartList(String cartListJson,String username){
		//存在中文乱码问题："title":"????????"
//		try {
//			cartListJson = new String(cartListJson.getBytes(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		//创建服务层参数
		TbUser user = new TbUser();
		user.setUsername(username);
		//调用服务层方法
		
		TaotaoResult result = cartService.setCartList(cartListJson, user);
		return result;
	}
	
	
	
	
	/**
	 * 查询缓存中对应用户名的购物车信息
	 * 其他项目调用此服务的url格式：http://localhost:8081/rest/cart/472972984
	 * @param username
	 * @return
	 */
	@RequestMapping("/cart/{username}")
	@ResponseBody
	public TaotaoResult getCartList(@PathVariable String username){
		if(StringUtils.isNotBlank(username)){
			//创建服务层参数
			TbUser user = new TbUser();
			user.setUsername(username);
			//调用服务
			TaotaoResult result = cartService.getCartList(user);
			return result;
		}
		
		return TaotaoResult.ok();
		
	}

}
