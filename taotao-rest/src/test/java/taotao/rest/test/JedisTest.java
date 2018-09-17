package taotao.rest.test;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	
	//单机版测试
//	@org.junit.Test
	public void testJedisSingle() throws Exception{
		//192.168.1.10 为 redis 主机的ip地址
		Jedis jedis = new Jedis("192.168.171.130", 6379);
		
		jedis.set("key", "value");
//		jedis.set("phone", "13575785255");
		String string = jedis.get("key");
		System.out.println(string);
		jedis.close();

		
	}
	
	
	//单机版使用连接池
//	@Test
	public void testJedisPool() throws Exception{
		//创建一个连接池对象
		//系统中该对象是单例的
		JedisPool jedisPool = new JedisPool("192.168.1.11", 6379);
		Jedis jedis = jedisPool.getResource();
		
		jedis.set("username", "472972984");
		String username = jedis.get("username");
		String phone = jedis.get("phone");
		
		jedis.close();
		
		System.out.println("username:"+username);
		System.out.println("phone:"+phone);
		//系统结束时需要关闭连接池
		jedisPool.close();
		
	}
	
	
	
//	@Test
	public void testJedisCluster() throws Exception{
		
		//nodes 中指定每个节点的地址
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.1.11", 7001));
		nodes.add(new HostAndPort("192.168.1.11", 7002));
		nodes.add(new HostAndPort("192.168.1.11", 7003));
		nodes.add(new HostAndPort("192.168.1.11", 7004));
		nodes.add(new HostAndPort("192.168.1.11", 7005));
		nodes.add(new HostAndPort("192.168.1.11", 7006));
		
		//JedisCluster 在系统中是单例的
		JedisCluster cluster = new JedisCluster(nodes);
		
//		cluster.set("name", "chenhuiqi");
//		cluster.set("age", "100");
		
		String age = cluster.get("age");
		String name = cluster.get("name");
		
		System.out.println("age:"+age);
		System.out.println("name:"+name);
		
		
		//系统关闭时JedisCluster需要close
		cluster.close();
		
	}
	
}
