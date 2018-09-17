package com.taotao.sso.service.impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.component.JedisClient;
import com.taotao.sso.service.LoginService;

/**
 * 用户登录Service
 * @author 陈汇奇
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private TbUserMapper mapper;

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Value("${REDIS_USER_CART_KEY}")
	private String REDIS_USER_CART_KEY;
	
	/**
	 * 登录，及将token存放到redis中
	 */
	@Override
	public TaotaoResult login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		
		//验证用户名
		TbUserExample example  = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//执行查找
		List<TbUser> list = mapper.selectByExample(example);
		
		if(list == null || list.isEmpty()){
			return TaotaoResult.build(400, "用户名或密码错误！");
		}
		
		TbUser user = list.get(0);
		
		//验证密码
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
			return TaotaoResult.build(400, "用户名或密码错误！");
		}
		
		//生成token
		String token = UUID.randomUUID().toString();
		
		//将token放入缓存中，模拟session , 并且设置过期时间
		//key为： REDIS_SESSION:{token}
		//value为：user去掉密码的json数据
		user.setPassword(null);
		jedisClient.set(REDIS_SESSION_KEY + ":"+token, JsonUtils.objectToJson(user));
		
		//设置redis缓存中过期时间
		jedisClient.expire(REDIS_SESSION_KEY + ":"+token, SESSION_EXPIRE);
		
		//设置购物车缓存数据
		//key: REDIS_USER_CART:{username}   例如：REDIS_USER_CART:472972984:购物车json列表
		//value:购物车信息列表
		jedisClient.set(REDIS_USER_CART_KEY+":"+username,"");
		
		
		//设置cookie
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		
		return TaotaoResult.ok(token);
	}

	/**
	 * 通过  token 获取用户信息
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		
		//如果存在：存放的为用户信息
		String json = jedisClient.get(REDIS_SESSION_KEY + ":"+token);
		
		if(StringUtils.isBlank(json)){
			//如果信息为空，说明用户session过期
			return TaotaoResult.build(400, "用户session已过期");
		}
		
		//如果信息存在
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		
		//说明用户处于活跃状态，重新设置过期时间
		jedisClient.expire(REDIS_SESSION_KEY + ":"+token, SESSION_EXPIRE);
		
		return TaotaoResult.ok(user);
	}

}
