package po;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

	public static String formatObjToJson(Object object) {

		return JSONObject.toJSONString(object);

	}

	public static <T> T formatJsonToObj(String json , Class<T> clazz) {
		
		return JSONObject.parseObject(json, clazz);
		
	}

}
