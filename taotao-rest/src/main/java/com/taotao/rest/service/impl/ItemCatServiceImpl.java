package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.ItemCatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 商品种类 Service
 * @author 陈汇奇
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	/**
	 * taotao首页的左边框鼠标移入显示的内容信息
	 */
	@Override
	public ItemCatResult getItemCatList() {
		
		List list = this.getItemCatList(0l);
		ItemCatResult itemCatResult = new ItemCatResult();
		itemCatResult.setData(list);
		
		return itemCatResult;
	}

	private List getItemCatList(Long parentId) {

		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);

		List listResult = new ArrayList();
		
		int count = 0 ;

		for (TbItemCat tbItemCat : list) {
			
			if(count >= 14){
				break;
			}
			
			// 如果是父节点
			if (tbItemCat.getIsParent()) {

				CatNode catNode = new CatNode();
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");

				if (tbItemCat.getParentId() == (long) 0) {
					catNode.setName("<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
					count++;
				} else {
					catNode.setName(tbItemCat.getName());
				}

				catNode.setItems(getItemCatList(tbItemCat.getId()));
				listResult.add(catNode);

			} else {
				// 如果不是父节点
				String item = "/products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName();
				listResult.add(item);
			}
		}
		return listResult;

	}
	
	
	
	
	
}
