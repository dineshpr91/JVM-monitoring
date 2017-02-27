package com.mbean.stats.utils;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

	public static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	public static final ObjectMapper mapper = new ObjectMapper();

	public static String objectToJson(Object obj) {
		String jsonString = null;

		try {
			jsonString = mapper.writeValueAsString(obj);

		} catch (IOException e) {
			logger.error("Error while converting {} object to Json : {}",
					new Object[] { obj.getClass(), e.getStackTrace() });
		}
		return jsonString;
	}
}
