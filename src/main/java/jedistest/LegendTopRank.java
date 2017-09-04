package jedistest;

import java.util.Random;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * 模拟英雄联盟游戏盒子的英雄排行榜实现
 * 
 * @author
 * 
 */
public class LegendTopRank {

	// 数据采集线程，获取英雄（游戏角色）出场数据并插入到redis数据库中
	static class HeroScoreGenerate implements Runnable {

		public void run() {
			String[] heros = new String[] { "盲僧", "盖伦", "石头", "亚索", "女警", "光辉", "剑圣", "放逐" };

			Random random = new Random();
			Jedis jedis = new Jedis("192.168.2.199");
			// 模拟不断产生新的英雄出场数据
			while (true) {
				try {
					Thread.sleep(1000 + random.nextInt(10) * 100);
					// 随机挑选了一个英雄，表示用这个挑选出来的英雄参与了一场游戏
					int index = random.nextInt(8);
					String hero = heros[index];
					
					// 在redis数据库中给相应的英雄增加分数
					jedis.zincrby("TopRankChuChang", 1, hero);
					
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			}

		}

	}

	// 获取排行榜单的线程
	public static void main(String[] args) {
		new Thread(new HeroScoreGenerate()).start();
		Jedis jedis = new Jedis("192.168.2.199");
		int i = 0;
		// 不断地去获取最新的排行榜
		while (true) {

			try {
				Thread.sleep(3000);
				Set<Tuple> heros = jedis.zrevrangeWithScores("TopRankChuChang", 0, -1);
				System.out.printf("第%s次获取排行榜：", i);
				System.out.println();
				
				for (Tuple t : heros) {
					System.out.println(t.getElement() + " : " + t.getScore());
				}

				i++;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
