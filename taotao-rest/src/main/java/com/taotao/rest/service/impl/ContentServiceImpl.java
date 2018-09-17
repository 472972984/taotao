package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ContentService;

/**
 * 发布的tb_content 内容Service
 * @author 陈汇奇
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_CONTENT_KEY}")
	private String REDIS_CONTENT_KEY;
	
	/**
	 * 返回的列表内容是涛taotao首页的广告位信息
	 */
	@Override
	public List<TbContent> getContentList(Long cid) {
		
		//执行业务逻辑之前先查看缓存中是否存在数据
		try {
			String json = jedisClient.hget(REDIS_CONTENT_KEY,cid+"");
			if(!StringUtils.isBlank(json)){
				//如果不为空就得到结果返回，不执行查询数据库
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		//执行完查询，将结果放入到缓存中
		//为了不影响正常的业务逻辑。采用 try catch 
		try {
			//为了规范key可以使用hash
			//定义一个保存内容的key，hash中每个项就是cid
			//value是list，需要把list转换成json数据。
			//将查询的结果放入到缓存中
			jedisClient.hset(REDIS_CONTENT_KEY, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

	@Override
	public TaotaoResult syncContent(Long cid) {
		jedisClient.hdel(REDIS_CONTENT_KEY, cid+"");
		return TaotaoResult.ok();
	}

}






