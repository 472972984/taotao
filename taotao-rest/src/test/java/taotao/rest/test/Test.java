package taotao.rest.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.ItemCatResult;

public class Test {

	private static TbItemCatMapper itemCatMapper;
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		itemCatMapper = context.getBean(TbItemCatMapper.class);
		JedisClient client =  context.getBean(JedisClient.class);
		client.set("wo", "you");
		String result = client.get("wo");
		System.err.println(result);
	}

	public List getItemCatList(Long parentId) {

		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 一共有1000+条记录
		List<TbItemCat> list1 = itemCatMapper.selectByExample(example);

		List listResult = new ArrayList();

		for (TbItemCat tbItemCat : list1) {

			// 如果是父节点
			if (tbItemCat.getIsParent()) {

				CatNode catNode = new CatNode();
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");

				if (tbItemCat.getParentId() == (long) 0) {
					catNode.setName(
							"<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
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

	public void test3() {

		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		TbItemCatMapper itemCatMapper = context.getBean(TbItemCatMapper.class);

		TbItemCatExample example = new TbItemCatExample();

		// 一共有1000+条记录
		List<TbItemCat> list1 = itemCatMapper.selectByExample(example);

		Map<Long, List> map = new HashMap();

		ArrayList<TbItemCat> arrayList = new ArrayList();
		List<Integer> temp = new ArrayList();

		for (TbItemCat tbItemCat : list1) {
			if (tbItemCat.getParentId() == 0) {
				arrayList.add(tbItemCat);
				temp.add(list1.indexOf(tbItemCat));
			}
		}
		map.put((long) 0, arrayList);

		// 移除
		for (Integer index : temp) {
			list1.remove(index);
		}

		Map result = recursion(map, list1, (long) 0);

	}

	/**
	 * 
	 * @param map:
	 *            parentId 为 0 的集合对象
	 * @param list：
	 *            查出来的所有数据集合
	 * @param index
	 * @return
	 */
	private Map recursion(Map<Long, List> map, List<TbItemCat> list, Long key) {

		// 用来存放移除的元素index
		List<Integer> indexs = new ArrayList<Integer>();

		if (list.size() == 0) {
			// 递归停止
			return map;
		} else {

			// 找节点
			for (TbItemCat currentList : list) {
				Long parentId = currentList.getParentId();
				// 得到父节点存放的list
				List parentList = map.get(key);

				// 如果集合存放的是TbItemCat对象
				if (parentList.get(0) instanceof TbItemCat) {

					for (Object object : parentList) {

						TbItemCat tbItemCat = (TbItemCat) object;

						if (tbItemCat.getParentId() == parentId) {
							// 属于
							int indexOf = list.indexOf(currentList);
							indexs.add(indexOf);

						}
					}

				}
				// 从父节点中遍历， 查找自己所属

			}

		}

		return map;
	}

	public void test2() {

		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		TbItemCatMapper itemCatMapper = context.getBean(TbItemCatMapper.class);

		TbItemCatExample example = new TbItemCatExample();

		// 一共有1000+条记录
		List<TbItemCat> list1 = itemCatMapper.selectByExample(example);

		// 选出最上层根节点
		List highNode = new ArrayList();

		HashMap<Long, List> equalParentIdMap = new HashMap<Long, List>();

		getListOfParentId(list1, highNode, equalParentIdMap);

		// 遍历 equalParentIdMap ， 找属于自己的 ParentId

		System.out.println("");

	}

	private void getListOfParentId(List<TbItemCat> list1, List highNode, HashMap<Long, List> equalParentIdMap) {
		for (TbItemCat tbItemCat : list1) {

			// 找出最上层的根节点
			if (tbItemCat.getParentId() == 0) {
				highNode.add(tbItemCat);
				// list1.remove(tbItemCat);
			} else {
				// 如果不是最上层根节点,存入到HashMap中
				Long parentId = tbItemCat.getParentId();

				// 如果在 hashmap中存在,则直接取出 list 存入
				if (equalParentIdMap.containsKey(parentId)) {
					equalParentIdMap.get(parentId).add(tbItemCat);
				} else {
					// 如果不存在
					// 存放 parentId 相同的元素
					List equalParentId = new ArrayList();
					equalParentId.add(tbItemCat);
					equalParentIdMap.put(tbItemCat.getParentId(), equalParentId);
				}

			}

		}
	}

	public void test3(TbItemCat itemCat) {

		// 如果为true， 是根节点， 有叶子
		if (itemCat.getIsParent()) {
			// 继续递归

		} else {
			// 没有根节点，那就是叶子节点，把数据添加到 CatNode item列表 中

		}

	}

	public void test1() {
		CatNode node = new CatNode();
		List items = new ArrayList();
		items.add("items1");
		items.add("items2");
		items.add("items3");
		items.add("items4");
		node.setItems(items);

		node.setName("name");
		node.setUrl("url1");

		CatNode node2 = new CatNode();
		List items2 = new ArrayList();
		items2.add("items1");
		items2.add("items2");
		items2.add("items3");
		items2.add("items4");
		node2.setItems(items);

		node2.setName("name");
		node2.setUrl("url2");

		ItemCatResult catResult = new ItemCatResult();

		List data = new ArrayList();
		data.add(node);
		data.add(node2);
		catResult.setData(data);

		String json = JsonUtils.objectToJson(catResult);
		System.out.println(json);
	}

}
