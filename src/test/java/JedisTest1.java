
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest1 {
	@Before
	public void before() {
		
	}
	@After
	public void after() {
		
	}
	//高可用性
//	@Test
	public void test1(){
		while(true){
			Jedis jedis = new Jedis("192.168.255.100",6379,2000);
			try{
				String value = jedis.get("a");
				System.out.println(value);
			}catch(Exception e){
				System.out.println("redis服务器发生故障，系统正在切换！");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}finally{
				jedis.disconnect();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//负载均衡
	@Test
	public void test2(){
		while(true){
			Jedis jedis = new Jedis("192.168.255.100",1000,2000);
			try{
				String value = jedis.get("a");
				String port = jedis.configGet("port").get(1);
				System.out.println("redis服务的端口:"+port);
				System.out.println("a:"+value);
				Thread.sleep(1000);
			}catch(Exception e){
				System.out.println("keepalived服务器发生故障，系统正在切换！");
			}finally{
				jedis.disconnect();
			}
			
		}
	}
	//负载均衡(多线程)
//	@Test
	public void test3(){
		int i = 0;
		int num = 20;
		ArrayList<Thread> list = new ArrayList<Thread>();
		while(i<num){
			Thread t = new Thread(new Myrun());
			list.add(t);
			t.start();
			i++;
		}
		while(true){
			boolean flag = true;
			for(int n=0;n<list.size();n++){
				if(list.get(n).isAlive()){
					flag = false;
					break;
				}
					
			}
			if(flag){
				break;
			}
		}
		System.out.println("测试结束！");
	}
	
	public class Myrun implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			Jedis jedis = new Jedis("192.168.255.100",1000,2000);
			String port = jedis.configGet("port").get(1);
			System.out.println("redis服务的端口:"+port);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				jedis.disconnect();
			}
		}
		
	}
	
	
}
