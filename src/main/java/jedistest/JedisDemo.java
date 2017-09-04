package jedistest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class JedisDemo {
	Jedis jedis = null;

	@Before
	public void init() {
		jedis = new Jedis("192.168.142.128");
	}
	
	

	/**
	 * String类型数据的api测试
	 */
	@Test
	public void testString() {
		// 插入一条string类型的数据
		String res = jedis.set("jedis-s-key-01", "itcast ia the greatest it school");
		System.out.println(res);

		// 获取一条string类型的数据
		String value = jedis.get("jedis-s-key-01");
		System.out.println(value);

		// 子字符串的获取
		String value2 = jedis.getrange("jedis-s-key-01", 0, 5);
		System.out.println(value2);

		// 子字符串的替换
		jedis.setrange("jedis-s-key-01", 0, "itheima");
		String value3 = jedis.get("jedis-s-key-01");
		System.out.println(value3);

		jedis.setrange("jedis-s-key-01", 26, "trainningschool");
		String value4 = jedis.get("jedis-s-key-01");
		System.out.println(value4);
		
		//如果偏移量超出字符串长度，则会自动补\0x00
		jedis.setrange("jedis-s-key-01", 50, "very good");
		String value5 = jedis.get("jedis-s-key-01");
		System.out.println(value5);

		//setnx会判断指定的key是否存在，如果已存在，则不会插入数据
		for (int i = 0; i < 5; i++) {
			jedis.setnx("jedis-s-key-0" + i, "000-" + i);
		}
		

	}
	
	/**
	 * 测试List数据结构的操作
	 */
	@Test
	public void testList(){
		
		//在链表的头部插入元素
//		Long count = jedis.lpush("jedis-l-key-01", "zhangsan","lisi","wangwu","zhaoliu");
//		System.out.println("插入的元素个数为： " + count);
		
		List<String> res = jedis.lrange("jedis-l-key-01", 0, -1);
		System.out.println("创建的链表中的元素为：");
		for(String s:res){
			
			System.out.println(s);
		}
		
		//在链表的中间插入新元素
		jedis.linsert("jedis-l-key-01", LIST_POSITION.BEFORE, "lisi", "田七");
		
		System.out.println("插入新元素之后的链表元素为： ");
		
		List<String> res2 = jedis.lrange("jedis-l-key-01", 0, -1);
		for(String s:res2){
			
			System.out.println(s);
		}
		
		Long count = jedis.lrem("jedis-l-key-01", 3, "zhangsan");
		System.out.println("成功删除了" + count + "个zhangsan");
		List<String> res3 = jedis.lrange("jedis-l-key-01", 0, -1);
		for(String s:res3){
			
			System.out.println(s);
		}
		
	}
	
	/**
	 * 测试Hash数据结构
	 */
	@Test
	public void testHash(){
		
		jedis.hset("jedis-h-key-01", "name", "zhangsan");
		
		String name = jedis.hget("jedis-h-key-01", "name");
		System.out.println(name);
		
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("password", "123");
		fields.put("age", "18");
		jedis.hmset("jedis-h-key-01", fields);
		
		
		Set<String> keys = jedis.hkeys("jedis-h-key-01");
		System.out.println("所有的key如下：");
		for(String key :keys){
			System.out.println(key);
			
		}
		
		List<String> hvals = jedis.hvals("jedis-h-key-01");
		System.out.println("所有的value如下： ");
		for(String val:hvals){
			
			System.out.println(val);
		}
		
		System.out.println("一次性取出所有的key-value对：");
		Map<String, String> kvs = jedis.hgetAll("jedis-h-key-01");
		Set<Entry<String, String>> entrySet = kvs.entrySet();
		for(Entry<String, String> ent :entrySet){
			System.out.println(ent.getKey() + " : " + ent.getValue());
			
		}
		
	}
	
	/**
	 * 测试set类型的数据结构
	 */
	@Test
	public void testSet(){
		
		//创建一条set类型的数据并插入一些members
		Long sadd = jedis.sadd("jedis-set-key-01", "java","c","c++","js","ruby");
		System.out.println("创建了一个set类型的数据，并且插入"+sadd+"个成员");
		
		//获取一条set类型的数据的成员
		Set<String> members = jedis.smembers("jedis-set-key-01");
		System.out.println("获取到的成员为：");
		for(String m:members){
			System.out.println(m);
		}
		
		jedis.sadd("jedis-set-key-02", "c#","c",".net","python","ruby");
		
		
		//求两个集合的差集
		Set<String> set12 = jedis.sdiff("jedis-set-key-01","jedis-set-key-02");
		
		System.out.println("set01 减去 set02 的差集结果为：");
		
		for(String s:set12){
			System.out.println(s);
		}
		
		
		
		Set<String> set21 = jedis.sdiff("jedis-set-key-02","jedis-set-key-01");
		
		System.out.println("set02 减去 set01 的差集结果为：");
		
		for(String s:set21){
			System.out.println(s);
		}
		
		//求两个集合的并集
		Set<String> union12 = jedis.sunion("jedis-set-key-01","jedis-set-key-02");
		System.out.println("set01和set02的并集结果为:");
		for(String s:union12){
			System.out.println(s);
		}
		
		
		//求两个集合的交集
		
		
	}
	
	/**
	 * 测试sortedset数据结构
	 */
	@Test
	public void testSortedSet(){
		
		HashMap<String, Double> scoreMembers = new HashMap<String, Double>();
		scoreMembers.put("zhangsan", 100.00);
		scoreMembers.put("lisi", 90.00);
		scoreMembers.put("wangwu", 80.00);
		scoreMembers.put("zhaoliu", 70.00);
		scoreMembers.put("tianqi", 60.00);
		
		jedis.zadd("jedis-zset-key-01", scoreMembers);
		
		
		
		//获取指定名次区间的所有成员，顺序为分数的由低到高
		Set<String> allMembers = jedis.zrange("jedis-zset-key-01", 0, -1);
		System.out.println("所有的成员为：");
		for(String m: allMembers){
			
			System.out.println(m);
		}
		
		
		System.out.println("给赵六加40分之后，成员的排名情况为：");
		//给指定的成员增加分数
		jedis.zincrby("jedis-zset-key-01", 40, "zhaoliu");
		
		//获取指定名次区间的所有成员及其关联的分数，顺序为分数的由低到高
		Set<Tuple> zrangeWithScores = jedis.zrangeWithScores("jedis-zset-key-01", 0, -1);
		for(Tuple t:zrangeWithScores){
			
			System.out.println(t.getElement() + " : " +t.getScore());
		}
		
		
		System.out.println("按照分数由高到低的顺序打印排行榜：");
		
		//获取指定名次区间的所有成员及其关联的分数，顺序为分数的由高到低
		Set<Tuple> zrevrangeWithScores = jedis.zrevrangeWithScores("jedis-zset-key-01", 0, -1);
		for(Tuple t:zrevrangeWithScores){
			
			System.out.println(t.getElement() + " : " +t.getScore());
		}
		
		
		//扩展练习其他方法
		
	}
	
	
	/**
	 * 测试针对key的通用操作
	 */
	@Test
	public void testGeneralKey(){
		
		Set<String> keys = jedis.keys("*");
		for(String key:keys){
			System.out.println(key);
		}
	}
	
	
	
	

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.142.128", 6379);

		// 测试客户端与redis服务器的联通性
		String ping = jedis.ping();
		System.out.println(ping);
	}

}
