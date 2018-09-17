package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.component.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * 订单相关service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbOrderShippingMapper orderShippingMapper;

	@Autowired
	private JedisClient jedisClient;

	// #缓存中订单明细主键生成key
	@Value("${REDIS_ORDER_DETAIL_GEN_KEY}")
	private String REDIS_ORDER_DETAIL_GEN_KEY;

	// #订单号生成key
	@Value("${REDIS_ORDER_GEN_KEY}")
	private String REDIS_ORDER_GEN_KEY;

	// 订单号的初始值
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;

	
	/**
	 * 接收json提交的订单数据，插入对应的数据库表中，返回生成的订单号
	 */
	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		
		//使用jedis的incr操作生成订单id
		
		//插入order表
		//进入前判断一下缓存中是否存在
		String id = jedisClient.get(REDIS_ORDER_GEN_KEY);
		if(StringUtils.isBlank(id)){
			//如果不存在，则设置进去
			jedisClient.set(REDIS_ORDER_GEN_KEY, ORDER_ID_BEGIN);
		}
		
		Long orderId = jedisClient.incr(REDIS_ORDER_GEN_KEY);
		//补全信息插入到对应的表中
		orderInfo.setOrderId(orderId.toString());
		
		//  status 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//执行插入
		orderMapper.insert(orderInfo);
		
		//插入orderItem表
		
		//对每一个列表中的订单信息生成一个id
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//使用jedis生成
			Long orderItemId = jedisClient.incr(REDIS_ORDER_DETAIL_GEN_KEY);
			tbOrderItem.setId(orderItemId.toString());
			tbOrderItem.setOrderId(orderId.toString());
			//执行插入
			orderItemMapper.insert(tbOrderItem);
		}
		
		
		//插入购物表
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId.toString());
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		//执行插入操作
		orderShippingMapper.insert(orderShipping);
		
		//返回orderId订单号
		return TaotaoResult.ok(orderId);
	}

}
