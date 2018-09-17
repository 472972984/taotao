package com.taotao.freemarker.test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {

	public class Student{
		
		private String id ;
		private String name ;
		private String address ;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Student() {
			super();
		}
		public Student(String id, String name, String address) {
			super();
			this.id = id;
			this.name = name;
			this.address = address;
		}
		
	}
	
//	@Test
	public void test() throws Exception{
		
//		第一步：把freemarker的jar包添加到工程中
//		第二步：freemarker的运行不依赖web容器，可以在java工程中运行。创建一个测试方法进行测试。
//		第三步：创建一个Configration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		
//		第四步：告诉config对象模板文件存放的路径。
		configuration.setDirectoryForTemplateLoading(new File("D:\\MyEclipse workspace\\taotao-portal\\src\\main\\webapp\\WEB-INF\\ftl"));
		
//		第五步：设置config的默认字符集。一般是utf-8
		configuration.setDefaultEncoding("UTF-8");
		
//		第六步：从config对象中获得模板对象。需要制定一个模板文件的名字。
		Template template = configuration.getTemplate("first.ftl");
		
//		第七步：创建模板需要的数据集。可以是一个map对象也可以是一个pojo，把模板需要的数据都放入数据集。
		Map map = new HashMap();
		map.put("hello", "hello freemarker");
		map.put("student", new Student("1", "张三", "北京"));
		
		List<Student> students = new ArrayList<Student>();
		students.add(new Student("1", "张三", "北京"));
		students.add(new Student("2", "张三2", "北京3"));
		students.add(new Student("3", "张三2", "北京3"));
		map.put("students", students);
		map.put("curTime",new Date());
		
		
		
//		第八步：创建一个Writer对象，指定生成的文件保存的路径及文件名。
		Writer out = new FileWriter(new File("D:\\temp\\html\\hello.html"));
		
//		第九步：调用模板对象的process方法生成静态文件。需要两个参数数据集和writer对象。
		template.process(map, out);
		
//		第十步：关闭writer对象。
		out.flush();
		out.close();
		
	}
	
	
}
