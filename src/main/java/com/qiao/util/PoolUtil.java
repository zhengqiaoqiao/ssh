package com.qiao.util;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;
@Component
public class PoolUtil {
	@Resource(name="dataSource")
	private ComboPooledDataSource cpds;
	
	private Timer timer;
	
	public void showInfo() throws SQLException{
		int d1 = cpds.getNumBusyConnections();
		int d2 = cpds.getNumIdleConnections();
		int d3 = cpds.getNumConnections();
		System.out.println("########################################");
		System.out.println("活动连接数："+d1);
		System.out.println("空闲连接数："+d2);
		System.out.println("总连接数："+d3);
	}
	
	public void start(){
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					showInfo();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, 0, 1000);
	}
	
	public void stop(){
		if(timer!=null)
			timer.cancel();
	}

	public ComboPooledDataSource getCpds() {
		return cpds;
	}

	public void setCpds(ComboPooledDataSource cpds) {
		this.cpds = cpds;
	}
}
