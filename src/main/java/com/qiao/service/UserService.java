package com.qiao.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiao.dao.UserDao;
import com.qiao.domain.User;
import com.qiao.util.JdbcUtil;
import com.qiao.util.JsonUtil;
import com.qiao.util.PoolUtil;

@Service
@Scope("prototype")
public class UserService {
	@Resource
	private UserDao userDao;
	@Resource
	private PoolUtil poolUtil;
	
	
	public List<User> getAllUsersByHibernate(){
		long start = System.currentTimeMillis();
		List<User> list = userDao.getAllUsersByHibernate();
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
		return list;
	}
	
	public List<User> getAllUsersByHibernate2(){
		long start = System.currentTimeMillis();
		List<User> list = userDao.getAllUsersByHibernate2();
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
		return list;
	}
	
	
	public List<User> getAllUsersByMyJDBC() throws Exception{
		long start = System.currentTimeMillis();
		List<User> list = userDao.getAllUsersByMyJDBC();
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
		return list;
	}
	
	public List<User> getAllUsersByJDBC() throws Exception{
		long start = System.currentTimeMillis();
		List<User> list = userDao.getAllUsersByJDBC();
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
		return list;
	}
	//测试hibernate一级缓存
	public void testHibernateFirstCache() throws Exception{
		SessionFactory sf =userDao.getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		session.beginTransaction();
		String sql = "from User u where u.id='8a80bc0b50173f480150174177710000'";

		Query query = session.createQuery(sql);

		long start1 = System.currentTimeMillis();
		List<User> list1 = query.list();
		long end1 = System.currentTimeMillis();
		System.out.println((end1-start1)/1000.0+"s");
		long start2 = System.currentTimeMillis();
		List<User> list2 = query.list();
		long end2 = System.currentTimeMillis();
		System.out.println((end2-start2)/1000.0+"s");
		session.getTransaction().commit();
	}
	
	public List<User> getUserById(String id) throws SQLException{
		String queryString = "from User u where u.id=?";
		HibernateTemplate temp = userDao.getHibernateTemplate();
		List<User> list = (List<User>) temp.find(queryString, id);	
		return list;
	}
	
	
	@Transactional
	public int updateUserAge(String id, int age){
		String queryString = "update User u set u.age=? where u.id=?";
		return userDao.getHibernateTemplate().bulkUpdate(queryString, new Object[]{age,id});
	}
	@Transactional
	public void addUser(String name, int age, String address){
		User user = new User();
		user.setAge(age);
		user.setName(name);
		user.setAddress(address);
		userDao.getHibernateTemplate().save(user);
		
	}
	@Transactional
	public void deleteUserById(String id){
		User user = new User();
		user.setId(id);
		userDao.getHibernateTemplate().delete(user);
	}

	public void addUsers(int count) throws Exception{
		

		final ExecutorService executor = Executors.newFixedThreadPool(50);
		int threadNum = (int) Math.round(count/100.0);
		int n = count%100;
		for(int i=0;i<threadNum;i++){
			Runnable r = null;
			if(n>0&&i==threadNum-1){
				final int m = n;
				r = new Runnable(){
					public void run() {
						// TODO Auto-generated method stub
						for(int j=0;j<m;j++){
							addUser("a", 11, "");
						}
					}
				};
			}else{
				final int m = 100;
				r = new Runnable(){
					public void run() {
						// TODO Auto-generated method stub
						for(int j=0;j<m;j++){
							addUser("a", 11, "");
						}
					}
				};
			}
			
			Thread thread = new Thread(r);
			executor.execute(thread);
		}	
		executor.shutdown();
		final Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if(!executor.isTerminated())
						poolUtil.showInfo();
					else
						t.cancel();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, 0, 1000);
		
	}
	
	@Transactional(timeout=20,rollbackFor=Exception.class) 
	public String test1() throws Exception{
		long start = System.currentTimeMillis();
		List<User>list = this.getAllUsersByJDBC();
		String msg = JsonUtil.obj2json(list);
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
		return msg;
	}
	
	@Transactional(timeout=5,rollbackFor=Exception.class) 
	public String test2(String id) throws Exception{
//		this.addUser("211", 1, "222");
		this.deleteUserById("8a80bc0b501db40a01501db41b7f0000");
//		Thread.sleep(5*1000);
		List<Map<String, Object>> list = userDao.getUserById(id);
		String msg = JsonUtil.obj2json(list);
		return msg;
	}
	
	@Transactional(timeout=20,rollbackFor=Exception.class) 
	public String test3() throws Exception{
		List list = getAllUsersByHibernate();
		String msg = JsonUtil.obj2json(list);
		return msg;
	}
}
