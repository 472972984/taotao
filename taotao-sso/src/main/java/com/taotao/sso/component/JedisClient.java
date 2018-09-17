package com.taotao.sso.component;

/**
 * 定义 jedis 接口，实现控制单机版或者集群版
 * @author 陈汇奇
 *
 */
public interface JedisClient {

	//普通的键值
	public String set(String key, String value);
	public String get(String key);
	
	//类似于hashMap
	public Long hset(String key, String item , String value);
	public String hget(String key, String item);
	
	//加1
	public Long incr(String key);
	//减1
	public Long decr(String key);
	
	//设置key的过期时间
	public Long expire(String key , int second);
	
	//判断key多久过期
	public Long ttl(String key);
	
	//hset中删除一个key
	public Long hdel(String key,String item);
	
	//删除一个普通key
	public Long del(String key);
	
}
