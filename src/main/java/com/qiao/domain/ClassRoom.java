package com.qiao.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "classroom")
public class ClassRoom implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true, length = 32)
	private String id;
	
	@Column(name = "name")
	private String name;
	
//　　OneToMany指定了一对多的关系，mappedBy="rid"指定了由多的那一方来维护关联关系，mappedBy指的是多的一方对1的这一方的依赖的属性，(注意：如果没有指定由谁来维护关联关系，则系统会给我们创建一张中间表)
	@OneToMany(cascade=CascadeType.ALL,mappedBy = "rid",fetch=FetchType.EAGER)
//　　LazyCollection属性设置成EXTRA指定了当如果查询数据的个数时候，只会发出一条 count(*)的语句，提高性能
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<Student> students;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

}
