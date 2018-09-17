package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

/**
 * 实现商品添加购物车 Controller
 * @author 陈汇奇
 *
 */
@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;

	/**
	 * 添加购物车
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, Integer num, 
			HttpServletRequest request , HttpServletResponse response){
		
		TaotaoResult result = cartService.addCart(itemId, num, request, response);
		return "cart-success";
		
	}
	
	/**
	 * 显示购物车信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String getCartItems(HttpServletRequest request, Model model){
		List<CartItem> list = cartService.getCartItems(request);
		model.addAttribute("cartList", list);
		return "cart";
	}
	
	
	/**
	 * 更新购物车数量
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateCartItemNum(@PathVariable long itemId , @PathVariable Integer num,
			HttpServletRequest request , HttpServletResponse response){
		
		TaotaoResult result = cartService.updateCartItem(itemId, num, request, response);
		return result;
		
	}
	
	//   http://localhost:8082/cart/delete/153568586904273.html
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable long itemId, HttpServletRequest request, HttpServletResponse response){
		
		TaotaoResult result = cartService.deleteCartItem(itemId, request, response);
		return "redirect:/cart/cart.html";
		
	}
	
	
	
	
	
	
	
}
