package com.qiao.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.qiao.model.pojo.Student;
import com.qiao.util.JdbcUtil;

@Repository
public class TestDao extends HibernateDao{
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private JdbcUtil jdbcUtil;
	
	public List<Student> getStudentInfo(){
		
		HibernateTemplate ht = getHibernateTemplate();
		ht.setCacheQueries(true);
		String hql = "select s from Student s left join s.room";
		List<Student> list = (List<Student>) ht.find(hql);
		
		return list;
	}
	
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
}
