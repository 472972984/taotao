package com.taotao.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;

/**
 * 发布生成订单Controller
 * 
 * @author 陈汇奇
 *
 */
@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 其他项目访问的url：http://localhost:8085/order/create
	 * @param orderInfo 接收的json数据
	 * @return
	 */
	@RequestMapping(value="/order/create" ,method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createOrder(@RequestBody OrderInfo orderInfo) {
		try {
			TaotaoResult result = orderService.createOrder(orderInfo);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
