package com.taotao.portal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.PortalItem;
import com.taotao.portal.service.ItemService;

/**
 * 向rest服务获取商品信息Service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	@Value("${REST_ITEM_BASE_URL}")
	private String REST_ITEM_BASE_URL;

	@Value("${REST_ITEM_DESC_URL}")
	private String REST_ITEM_DESC_URL;

	@Value("${REST_ITEM_PARAM_URL}")
	private String REST_ITEM_PARAM_URL;

	/**
	 * 根据 商品id，向rest服务请求商品基本信息
	 */
	@Override
	public TbItem getItemById(Long itemId) {

		// 理论上得到一个 TaotaoResult json数据
		String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_BASE_URL + itemId);
		if (StringUtils.isNotBlank(json)) {
			// 转换为 java 对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, PortalItem.class);
			// 获取对象
			PortalItem item = (PortalItem) taotaoResult.getData();
			return item;
		}

		return null;
	}

	/**
	 * 根据 商品id，向rest服务请求商品描述信息
	 */
	@Override
	public String getItemDescById(Long itemId) {

		// 理论上得到一个 TaotaoResult json数据
		String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_DESC_URL + itemId);
		if (StringUtils.isNotBlank(json)) {
			// 转换为 java 对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemDesc.class);
			// 获取对象
			TbItemDesc itemDesc = (TbItemDesc) taotaoResult.getData();
			String desc = itemDesc.getItemDesc();
			return desc;
		}
		return null;
	}

	/**
	 * 根据itemId ， 通过 rest 发布的服务申请数据
	 */
	@Override
	public String getItemParamById(Long itemId) {

		String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_PARAM_URL + itemId);

		if (StringUtils.isNotBlank(json)) {
			// 将json数据转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
			TbItemParamItem paramItem = (TbItemParamItem) taotaoResult.getData();

			// 得到参数列表 json数据
			String data = paramItem.getParamData();

			// 将参数列表json数据转换成 map
			List<Map> mapList = JsonUtils.jsonToList(data, Map.class);

			// 拼接html
			StringBuffer sb = new StringBuffer();

			sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
			sb.append("	<tbody>\n");
			for (Map map : mapList) {
				sb.append("		<tr>\n");
				sb.append("			<th class=\"tdTitle\" colspan=\"2\">" + map.get("group") + "</th>\n");
				sb.append("		</tr>\n");
				// 取规格项
				List<Map> mapList2 = (List<Map>) map.get("params");
				for (Map map2 : mapList2) {
					sb.append("		<tr>\n");
					sb.append("			<td class=\"tdTitle\">" + map2.get("k") + "</td>\n");
					sb.append("			<td>" + map2.get("v") + "</td>\n");
					sb.append("		</tr>\n");
				}
			}
			sb.append("	</tbody>\n");
			sb.append("</table>");

			return sb.toString();
		}

		return null;
	}

}
