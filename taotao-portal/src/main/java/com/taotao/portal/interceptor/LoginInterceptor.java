package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

/**
 * 拦截用户，审查是否登录，登录放行。
 * @author 陈汇奇
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService	userService;
	
	//存在父子容器关系：父容器扫描的属性。子容器不能访问?   所以在springmvc中也扫描一下
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		TbUser user = userService.getUserByToken(request, response);
		
		if(user == null){
			//强制跳转到登录页面 , 并传递给服务器当前正在访问的页面，实现登陆之后跳转回来
			response.sendRedirect(SSO_LOGIN_URL+"?redirect="+request.getRequestURL());
			return false;
		}
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
