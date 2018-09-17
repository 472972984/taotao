package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.portal.pojo.OrderInfo;
import com.taotao.portal.service.OrderService;

/**
 * 订单服务 Service
 * @author 陈汇奇
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;

	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;
	
	@Override
	public String createOrder(OrderInfo orderInfo) {
		
		//返回值因改为TaotaoResult
		String json = HttpClientUtil.doPostJson(ORDER_BASE_URL+ORDER_CREATE_URL, JsonUtils.objectToJson(orderInfo));
		//转换成java对象
		TaotaoResult result = TaotaoResult.format(json);
		String orderId = result.getData().toString();
		
		return orderId;
	}
	
	

}
