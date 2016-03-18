import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import com.qiao.domain.User;
import com.qiao.util.JedisUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
			locations={
					"classpath*:applicationContext.xml",
					"classpath*:spring-mvc-servlet.xml"
			}
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JedisTest {
	@Resource
	private JedisUtil jedisUtil;
	@Before
	public void before() {
		
	}
	@After
	public void after() {
		
	}
	
	//事物测试
//	@Test
	public void testTransactions() {
	    Jedis jedis = new Jedis("192.168.255.131", 1000);
	    long start = System.currentTimeMillis();
	    Transaction tx = jedis.multi();
	    try{
	    	for (int i = 0; i < 100; i++) {
		        tx.set("t" + i, "t" + i);
		        if(i==99)
		        	throw new Exception();
		    }
	    	List<Object> results = tx.exec();
	    }catch(Exception e){
	    	tx.discard();
	    }
	    long end = System.currentTimeMillis();
	    System.out.println("Transaction SET: " + ((end - start)/1000.0) + " seconds");
	    jedis.disconnect();
	}
	
	//管道测试
//	@Test
	public void testPipelined() {
	    Jedis jedis = new Jedis("192.168.255.131",1000);
	    Pipeline pipeline = jedis.pipelined();
	    long start = System.currentTimeMillis();
	    for (int i = 0; i < 100; i++) {
	        pipeline.set("p" + i, "p" + i);
	    }
	    List<Object> results = pipeline.syncAndReturnAll();
	    long end = System.currentTimeMillis();
	    System.out.println("Pipelined SET: " + ((end - start)/1000.0) + " seconds");
	    jedis.disconnect();
	}
	//负载均衡测试
//	@Test
	public void testLoadBalancing() throws Exception {
		while(1==1){
			String info = jedisUtil.getInfo();
			System.out.println(info);
			Thread.sleep(1000);
		}
		
	}
	//Redis集群测试
//	@Test
	public void testCluster() throws Exception {
		String value = jedisUtil.getValue2("a");
		System.out.println(value);
	}
	
	//
//	@Test
	public void test() throws Exception {
		User user = new User();
		user.setId("111");
		user.setName("年后");
		jedisUtil.setObject("dddd", user);
		User u = (User) jedisUtil.getObject("dddd");
		
	}
	@Test
	public void test2(){
		Jedis jedis = new Jedis("192.168.255.131",1001);
		String value = jedis.get("a");
		System.out.println(value);
	    jedis.disconnect();
	}
	
}
