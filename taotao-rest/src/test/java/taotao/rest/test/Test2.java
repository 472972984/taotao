package taotao.rest.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.rest.contorller.ItemCatController;
import com.taotao.rest.service.ItemCatService;

public class Test2 {


	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/*.xml");
		TbItemCatMapper itemCatMapper = context.getBean(TbItemCatMapper.class);
		System.err.println(context);
		System.out.println(context.getBean(ItemCatController.class));
		System.err.println(context.getBean(ItemCatService.class));
	}
	
}
