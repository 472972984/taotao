<!doctype html>
<html lang="en">
 <head>
  <meta charset="UTF-8">
  <meta name="Generator" content="EditPlus®">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
  <title>Document</title>
 </head>
 <body>
   ${hello}</br>
  
  	<label>学号：${student.id}</label></br>
  	<label>姓名：${student.name}</label></br>
  	<label>地址：${student.address}</label></br>
  
  	</br>
  	<#list students as s>
  	
  	<#if s_index %2 == 0>
  		<label>幸运儿</label></br>	
		<#else>
  		<label>你死定了</label></br>	
	</#if>
	<label>第${s_index+1}个学生</label></br>	
  	<label>学号：${s.id}</label></br>
  	<label>姓名：${s.name}</label></br>
  	<label>地址：${s.address}</label></br>
  	</#list> 
  	
  	<label>当前时间为：${curTime?datetime}</label>
  	
  
 </body>
</html>


