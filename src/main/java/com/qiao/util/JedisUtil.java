package com.qiao.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
	@Resource
	private JedisPool jedisPool;
	
	@Resource
	private JedisPool jedisPoolMaster;
	
	@Resource
	private JedisCluster jedisCluster;
	
	public String getInfo(){
		Jedis jedis = jedisPool.getResource();
		String s1 = jedis.configGet("port").get(0);
		String s2 = jedis.configGet("port").get(1);
		String s3 = jedis.get("a");
		String info = s1+":"+s2+"-----a:"+s3;
		return info;
	}
	
	public String getValue2(String key){
		String value = jedisCluster.get(key);
		return value;
	}
	

	public String getValue(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get(key);
		return value;
	}
	
	
	
	public void setValue(String key, String value) {
		Jedis jedis = jedisPoolMaster.getResource();
		jedis.set(key, value);
		
	}
	
	public Object getObject(String key) {
		Jedis jedis = jedisPool.getResource();
		byte[] value = jedis.get((key).getBytes());
		if(value == null)
		{
			return null;
		}
		Object obj = SerializeUtil.deserialize(value);
		return obj;
	}

	public void setObject(String key, Object value) {
		Jedis jedis = jedisPoolMaster.getResource();
		if(value != null)
		{
			jedis.set((key).getBytes(), SerializeUtil.serialize(value));
		}
	}

	public void remove(String... keys) {
		Jedis jedis = jedisPool.getResource();
		jedis.del(keys);
	}
	
	public void remove(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.del(key);
	}
	
	static class SerializeUtil {
		public static byte[] serialize(Object value) {
			if (value == null) {
				throw new NullPointerException("Can't serialize null");
			}
			byte[] rv = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new ObjectOutputStream(bos);
				os.writeObject(value);
				os.close();
				bos.close();
				rv = bos.toByteArray();
			} catch (IOException e) {
				throw new IllegalArgumentException("Non-serializable object", e);
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return rv;
		}

		public static Object deserialize(byte[] in) {
			Object rv = null;
			ByteArrayInputStream bis = null;
			ObjectInputStream is = null;
			try {
				if (in != null) {
					bis = new ByteArrayInputStream(in);
					is = new ObjectInputStream(bis);
					rv = is.readObject();
					is.close();
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if(null != is){
						is.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(null != bis){
						bis.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return rv;
		}
	}
}
