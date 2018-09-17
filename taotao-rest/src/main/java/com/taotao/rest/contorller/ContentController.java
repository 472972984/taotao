package com.taotao.rest.contorller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.rest.service.ContentService;

/**
 * 发布商品tb_content服务的Controller
 * @author 陈汇奇
 *
 */
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	
	@RequestMapping("/content/{cid}")
	@ResponseBody
	public TaotaoResult getContentList(@PathVariable(value="cid") long cid) {
		
		try {
			List<TbContent> list = contentService.getContentList(cid);
			return TaotaoResult.ok(list);
		} catch (Exception e) {
			//捕获到异常
			e.printStackTrace();
			String StackTrace = ExceptionUtil.getStackTrace(e);
			return TaotaoResult.build(500, StackTrace);
		}
		
	}
	
	
	@RequestMapping("/sync/content/{cid}")
	@ResponseBody
	public TaotaoResult syncContent(@PathVariable("cid") Long cid){
		try {
			TaotaoResult result = contentService.syncContent(cid);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	
}



