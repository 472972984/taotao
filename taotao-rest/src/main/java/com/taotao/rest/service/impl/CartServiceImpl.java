package com.taotao.rest.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.CartService;

/**
 * 查询缓存中 购物车信息
 * 
 * @author 陈汇奇
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_USER_CART_KEY}")
	private String REDIS_USER_CART_KEY;

	@Override
	public TaotaoResult getCartList(TbUser user) {

		String json = jedisClient.get(REDIS_USER_CART_KEY + ":" + user.getUsername());

		if (StringUtils.isNotBlank(json)) {
			return TaotaoResult.ok(json);
		}
		// data为null
		return TaotaoResult.ok();
	}

	/**
	 * 设置用户购物车缓存
	 */
	@Override
	public TaotaoResult setCartList(String cartListJson, TbUser user) {

		jedisClient.set(REDIS_USER_CART_KEY + ":" + user.getUsername(), cartListJson);
		return TaotaoResult.ok();
		
	}

}
