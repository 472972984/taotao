package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.OrderInfo;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;

/**
 * 显示订单信息 Controller
 * @author 陈汇奇
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order-cart")
	public String showOrderCart(Model model , HttpServletRequest request){
		List<CartItem> cartItems = cartService.getCartItems(request);
		model.addAttribute("cartList", cartItems);
		return "order-cart";
	}
	
	/**
	 * 前提：用户必须登录
	 * 	并且在request域中存放user对象:用户获取用户信息
	 * 功能需求：
	 * 	1）界面提交表单：用 OrderInfo 接收
	 *  2）补全信息
	 *  3）调用service层服务，向order服务提交创建订单请求
	 *  4）获取订单号，返回
	 * @return
	 */
	@RequestMapping(value = "/create")
	public String createOrder(HttpServletRequest request,
			Model model, OrderInfo orderInfo) {
		
		//获取request域中的用户对象
		TbUser user = (TbUser) request.getAttribute("user");
		
		//补全orderInfo信息
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		
		//调用服务
		String orderId = orderService.createOrder(orderInfo);
		
		//往success页面中传递数据
		model.addAttribute("orderId", orderId);
		model.addAttribute("payment", orderInfo.getPayment());
		
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		//返回到success页面
		return "success";
	}
	
	
}
