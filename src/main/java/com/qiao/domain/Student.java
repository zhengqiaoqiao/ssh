package com.qiao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Lazy;
@Entity
@Table(name = "student")
public class Student implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8133278348085180127L;

	/**
	 * id
	 */
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false,
			insertable = true, updatable = true, length = 32)
	private String id;
	
	@Column(name = "name")
	private String name;
	
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "rid", insertable = false, updatable = false)
	private String rid;
	
//	ManyToOne指定了多对一的关系，fetch=FetchType.LAZY属性表示在多的那一方通过延迟加载的方式加载对象(默认不是延迟加载)
	@ManyToOne(fetch=FetchType.EAGER)
//	通过 JoinColumn 的name属性指定了外键的名称 rid　(注意：如果我们不通过JoinColum来指定外键的名称，系统会给我们声明一个名称)
	@JoinColumn(name="rid")
	private ClassRoom room;
	
//	 增加版本属性
	@Version
	private int version;

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public ClassRoom getRoom() {
		return room;
	}

	public void setRoom(ClassRoom room) {
		this.room = room;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
