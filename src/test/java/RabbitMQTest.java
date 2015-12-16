import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



/**
 * <p>Title: RabbitMQTest</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年11月2日
 */
public class RabbitMQTest {
	private ConnectionFactory factory = null;
	private final String host = "192.168.255.131";
	
	@Before
	public void before() {
		factory = new ConnectionFactory();
		factory.setHost(host); 
	}
	@After
	public void after() {
		
	}
	//发送消息
//	@Test
	public void test1() throws IOException, TimeoutException{
		Connection connection = null;
		Channel channel = null;
		String queueName = "ha.1";
		String message = "hello world";
		try{
			factory.setHost("192.168.255.131");
		    connection = factory.newConnection();
		    channel = connection.createChannel();
		    channel.queueDeclare(queueName, false, false, false, null);
		    channel.basicPublish("", queueName, null, message.getBytes());
		    System.out.println(" [x] Sent '" + message + "'");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			channel.close();
			connection.close();
		}
	}
	//接收消息
	@Test
	public void test2() throws IOException, TimeoutException{
		Connection connection = null;
		Channel channel = null;
		String queueName = "ha.1";
		try{
			factory.setHost("192.168.255.131");
		    connection = factory.newConnection();
		    channel = connection.createChannel();
		    channel.queueDeclare(queueName, false, false, false, null);
		    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    Consumer consumer = new DefaultConsumer(channel) {
		        @Override
		        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		            throws IOException {
		          String message = new String(body, "UTF-8");
		          System.out.println(" [x] Received '" + message + "'");
		        }
	        };
	        channel.basicConsume(queueName, true, consumer);
	        while(true){
	        	
	        }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			channel.close();
			connection.close();
		}
	}
}
