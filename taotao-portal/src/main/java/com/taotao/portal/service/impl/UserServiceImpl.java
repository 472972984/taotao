package com.taotao.portal.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

/**
 * 强制用户登录Service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Value("${SSO_BASE_URL}")
	private String SSO_BASE_URL;

	@Value("${SSO_USER_TOKEN_SERVICE}")
	private String SSO_USER_TOKEN_SERVICE;

	
	/**
	 * 从cookie中取出token，向服务申请数据，判断用户是否登录
	 */
	@Override
	public TbUser getUserByToken(HttpServletRequest request, HttpServletResponse response) {

		try {
			// 获取Cookie
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");

			// cookie是否存在
			if(StringUtils.isBlank(token)){
				return null;
			}
			
			//如果存在， 根据token调用sso服务查询用户信息，是否过期
			String json = HttpClientUtil.doGet(SSO_BASE_URL+SSO_USER_TOKEN_SERVICE+token);
			
			TaotaoResult result = TaotaoResult.format(json);
			
			//如果不为200，说明session已过期
			if(result.getStatus() != 200){
				return null;
			}
			
			result = TaotaoResult.formatToPojo(json, TbUser.class);
			
			return (TbUser) result.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
