package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.portal.service.LogoutService;

/**
 * 用户安全退出 Service
 * @author 陈汇奇
 *
 */
@Service
public class LogoutServiceImpl implements LogoutService {
	
	@Value("${SSO_BASE_URL}")
	private String SSO_BASE_URL;

	@Value("${SSO_USER_LOGOUT_URL}")
	private String SSO_USER_LOGOUT_URL;
	
	/**
	 * 根据token，调用sso系统安全退出
	 */
	@Override
	public Object logout(String token) {
		
		//调用httpClient删除redis缓存中的token
		//http://localhost:8084/user/logout/972592c9-eeab-4082-b5f7-e6904d00fb17
		String json = HttpClientUtil.doGet(SSO_BASE_URL+SSO_USER_LOGOUT_URL+token);
		
		return json;
	}

}
