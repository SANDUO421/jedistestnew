package jedistest;

import java.util.ArrayList;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ShardClusterTest {
	
	public static void main(String[] args) {
		
		//poolConfig是连接池的配置参数
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		
		//shards是分片集群中所有分片服务器信息列表,JedisShardInfo是分片服务器信息
		ArrayList<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		
		//将集群中的两台shard服务器信息封装到两个JedisShardInfo对象中 
		JedisShardInfo shard1 = new JedisShardInfo("192.168.2.199", 6379);
		JedisShardInfo shard2 = new JedisShardInfo("192.168.2.199", 6380);
		
		//将分片服务器信息对象添加到分片服务器信息列表shards中
		shards.add(shard1);
		shards.add(shard2);
		
		//创建一个带数据分片功能的jedis连接池
		ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, shards);
		
		//从连接池中获取一个(带数据分片功能的)jedis连接
		ShardedJedis jedis = shardedJedisPool.getResource();
		
		for(int i=0;i<1000;i++){
			jedis.set("string-key-"+i, "1000" + i);
		}
		
		jedis.close();
		
		shardedJedisPool.close();
		
	}
	
	

}
