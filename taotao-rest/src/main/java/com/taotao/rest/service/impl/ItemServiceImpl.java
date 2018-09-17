package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ItemService;

/**
 * 网页点击商品 查询商品信息 返回信息结果 Service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;

	@Value("${ITEM_BASE_INFO_KEY}")
	private String ITEM_BASE_INFO_KEY;

	@Value("${ITEM_EXPIRE_SECOND}")
	private int ITEM_EXPIRE_SECOND;

	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Value("${ITEM_PARAM_KEY}")
	private String ITEM_PARAM_KEY;
	
	@Autowired
	private TbItemDescMapper descMapper;
	
	@Value("${ITEM_DESC_KEY}")
	private String ITEM_DESC_KEY;
	
	/**
	 * 根据商品id，查询商品的基本信息
	 */
	@Override
	public TbItem getItemById(Long itemId) {

		// 查询之前先查看缓存中有没有数据
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY);

			// 如果数据不为空返回
			if (StringUtils.isNotBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 单表查询
		TbItem item = itemMapper.selectByPrimaryKey(itemId);

		try {
			// 查询到数据就放入到缓存中，由于商品数据比较多，需要把用户访问的数据添加到缓存中
			// 因为需要设置过期时间，所以不能选择用 hset ， 因为 expire 是根据 key 来 设置的。会将所有 的数据清空。
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY, JsonUtils.objectToJson(item));

			// 同时需要设置商品数据的过期时间：
			// 为什么需要设置过期时间：如果一直存放在数据库中，点击一个商品，先加入缓存，若这个商品在1个小时过去后，没在点击，便从缓存中删除，过期。
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY, ITEM_EXPIRE_SECOND);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * 根据商品id，返回 TbItemParamItem 商品参数信息
	 */
	@Override
	public TbItemParamItem getItemParamById(Long itemId) {

		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY);

			if (StringUtils.isNotBlank(json)) {

				TbItemParamItem itemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return itemParamItem;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 设置查询条件
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);

		// 执行查询
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);

		TbItemParamItem itemParamItem = null;
		if (list != null && list.size() > 0) {
			itemParamItem = list.get(0);

			// 添加缓存
			try {
				jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY,
						JsonUtils.objectToJson(itemParamItem));

				// 设置过期时间
				jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY, ITEM_EXPIRE_SECOND);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return itemParamItem;
		}
		return itemParamItem;

	}

	/**
	 * 根据商品id，查询商品的描述
	 */
	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY);
			//如果查询到的数据库不为空
			if(StringUtils.isNotBlank(json)){
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc itemDesc = descMapper.selectByPrimaryKey(itemId);
		
		//添加缓存
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY, JsonUtils.objectToJson(itemDesc));
			// 设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
