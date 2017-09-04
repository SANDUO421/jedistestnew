package jedistest;

import java.util.Random;
import java.util.UUID;

import redis.clients.jedis.Jedis;

public class TaskScheduler {

	/**
	 * 任务生产者线程逻辑
	 * 
	 * @author
	 * 
	 */
	static class TaskProducer implements Runnable {

		public void run() {
			Jedis jedis = new Jedis("192.168.2.199");
			System.out.println("任务生产者启动.......");
			jedis.del("task-list");
			
			while (true) {

				UUID newTaskId = UUID.randomUUID();
				
				//"{"id":"100","operate":"insert"}"
				jedis.lpush("task-list", newTaskId.toString());
				System.out.println("生产者插入了一个新任务： " + newTaskId);
				try {
					int nextInt = new Random().nextInt(2);
					Thread.sleep(1000 + nextInt * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	/**
	 * 任务处理者线程逻辑
	 * 
	 * @author
	 * 
	 */
	static class TaskWorker implements Runnable {

		public void run() {

			Jedis jedis = new Jedis("192.168.2.199");
			System.out.println("任务处理者启动.......");
			jedis.del("status-list");

			while (true) {
				try {
					Thread.sleep(1000);
					// 从task-list弹出一个任务，并插入到status-list队列中
					String taskId = jedis.rpoplpush("task-list", "status-list");
					// 处理任务的逻辑......
					int nextInt = new Random().nextInt(9);
					// 模拟任务处理成功的情况
					if (nextInt % 4 != 0) {

						// 从status-list队列中弹出这个处理成功的任务
						jedis.lpop("status-list");
						System.out.println(taskId + ": 处理成功，并从任务调度系统中彻底删除");

					} else {
						// 模拟任务处理失败的情况
						jedis.rpoplpush("status-list", "task-list");
						System.out.println(taskId + ": 处理失败，并从status-list中弹回task-list");

					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	public static void main(String[] args) throws Exception {

		new Thread(new TaskProducer()).start();
		Thread.sleep(200);
		new Thread(new TaskWorker()).start();

	}

}
