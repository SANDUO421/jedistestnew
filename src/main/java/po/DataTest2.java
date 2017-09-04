package po;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;

public class DataTest2 {

	public static void main(String[] args) {

		Jedis jedis = new Jedis("192.168.142.128", 6379);
		
		//insertDataToRedis(jedis);
		
		getDataFromRedis(jedis);

	}
	
	private static void getDataFromRedis(Jedis jedis){
		String key  = "country-key2";
		Map<String,String> map = jedis.hgetAll(key);
		
		if (map != null) {
			for (Entry<String,String> entry : map.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				System.out.println("value转为对象  : " + JsonUtils.formatJsonToObj(entry.getValue(), Country.class));
			}
		}
	}
	
	
	
	private static void insertDataToRedis(Jedis jedis){
		List<Country> cList = loadData();
		if(cList != null){
			for (Country country : cList) {
				jedis.hset("country-key2", country.getCode(), JsonUtils.formatObjToJson(country));
			}
			System.out.println("==================== 存入数据完成.... ");
		}
	}
	
	
	
	private static List<Country> loadData() {

		// 使用jdbc访问数据库
		String sql = "select * from t_country";

		// 数据库链接
		Connection connection = null;

		// 预编译statement
		PreparedStatement preparedStatement = null;

		// 结果集
		ResultSet resultSet = null;

		// 区域列表
		List<Country> list = new ArrayList<Country>();

		try {
			// 加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");

			// 连接数据库
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "2143");

			// 创建preparedStatement
			preparedStatement = connection.prepareStatement(sql);

			// 获取结果集
			resultSet = preparedStatement.executeQuery();

			// 结果集解析
			while (resultSet.next()) {
				Country coun = new Country();
				coun.setId(resultSet.getInt("c_id"));
				coun.setName(resultSet.getString("c_name"));
				coun.setCode(resultSet.getString("c_code"));
				coun.setStatus(resultSet.getString("c_status"));

				list.add(coun);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

}
