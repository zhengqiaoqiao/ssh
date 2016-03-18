package com.qiao.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.qiao.domain.User;
import com.qiao.util.JdbcUtil;

@Repository
public class UserDao extends HibernateDao{
	private final Logger LOGGER = Logger.getLogger(UserDao.class);
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private JdbcUtil jdbcUtil;
	
	public List<Map<String, Object>> getUserById(String id){
		List<Map<String, Object>> list = null;
		String sql = "select * from user where id=?";
		list = this.jdbcTemplate.queryForList(sql, new Object[]{id});
		return list;
	}
	
	public List<User> getAllUsersByHibernate(){
		HibernateTemplate ht = getHibernateTemplate();
		ht.setCacheQueries(true);
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		List<User> list = (List<User>) ht.findByCriteria(criteria,-1,-1);
		return list;
	}
	
	public List<User> getAllUsersByHibernate2(){
		HibernateTemplate ht = getHibernateTemplate();
		ht.setCacheQueries(false);
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		List<User> list = (List<User>) ht.findByCriteria(criteria,-1,-1);
		return list;
	}
	
	public List<User> getAllUsersByJDBC(){
		String sql = "select * from user";
		List<Map<String, Object>> lt = getJdbcTemplate().queryForList(sql);
		List<User> list = new ArrayList<User>();
		for(Map<String, Object> map:lt){
			User user = new User();
			user.setId(map.get("id")==null?"":map.get("id").toString());
			user.setName(map.get("name")==null?"":map.get("name").toString());
			user.setAge(Integer.parseInt(map.get("age")==null?"0":map.get("age").toString()));
			user.setAddress(map.get("address")==null?"":map.get("address").toString());
			list.add(user);
		}
		return list;
	}
	
	public List<User> getAllUsersByMyJDBC() throws Exception{
		String sql = "select * from user";
		List<Map<String, Object>> lt = jdbcUtil.select(sql);
		List<User> list = new ArrayList<User>();
		for(Map<String, Object> map:lt){
			User user = new User();
			user.setId(map.get("id")==null?"":map.get("id").toString());
			user.setName(map.get("name")==null?"":map.get("name").toString());
			user.setAge(Integer.parseInt(map.get("age")==null?"0":map.get("age").toString()));
			user.setAddress(map.get("address")==null?"":map.get("address").toString());
			list.add(user);
		}
		return list;
	}
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
}
