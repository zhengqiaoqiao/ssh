package com.qiao.util;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;



/**
 * <p>Title: RabbitMQUtil</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2015年11月2日
 */
public class RabbitMQUtil {
	private String host = "192.168.255.131";
	public void sendMessage(String queue, String message) {
		ConnectionFactory factory = null;
		Connection connection = null;
		Channel channel = null;
		try{
			factory = new ConnectionFactory();
			factory.setHost(host);
		    connection = factory.newConnection();
		    channel = connection.createChannel();
		    channel.queueDeclare(queue, false, false, false, null);
		    channel.basicPublish("", queue, null, message.getBytes());
		    System.out.println(" [x] Sent '" + message + "'");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				channel.close();
				connection.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public void recvMessage(String queue){
		ConnectionFactory factory = null;
		Connection connection = null;
		Channel channel = null;
		try{
			factory = new ConnectionFactory();
			factory.setHost(host);
		    connection = factory.newConnection();
		    channel = connection.createChannel();
		    channel.queueDeclare(queue, false, false, false, null);
		    Consumer consumer = new DefaultConsumer(channel) {
		        @Override
		        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		            throws IOException {
		          String message = new String(body, "UTF-8");
		          System.out.println(" [x] Received '" + message + "'");
		        }
		        @Override
		        public void handleRecoverOk(String s)
		        {
		        	 System.out.println(" [x] Received OK '" + s + "'");
		        }
	        };
	        channel.basicConsume(queue, true, consumer);
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				channel.close();
				connection.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	
	}
	
}
