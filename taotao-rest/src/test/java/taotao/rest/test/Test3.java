package taotao.rest.test;

import java.util.Date;

import com.taotao.common.pojo.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public class Test3 {

	public void test(){
		TaotaoResult result ;
		TbItem item = new TbItem();
		item.setBarcode("1");
		item.setCid(123456l);
		item.setCreated(new Date());
		item.setId(123l);
		item.setImage("image");
		try {
			int i = Integer.parseInt("1");
			result = TaotaoResult.ok(item);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		System.out.println(result);
	}
	
}
