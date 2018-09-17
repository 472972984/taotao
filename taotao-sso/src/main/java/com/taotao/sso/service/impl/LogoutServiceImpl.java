package com.taotao.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.component.JedisClient;
import com.taotao.sso.service.LogoutService;

/**
 * 用户安全退出Service
 * @author 陈汇奇
 *
 */
@Service
public class LogoutServiceImpl implements LogoutService {
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;

	/**
	 * 删除redis缓存中的token
	 */
	@Override
	public TaotaoResult logout(String token) {
		//删除redis缓存中的token
		jedisClient.del(REDIS_SESSION_KEY + ":"+token);
		return TaotaoResult.ok();
	}

}
