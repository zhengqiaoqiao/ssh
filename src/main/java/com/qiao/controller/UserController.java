package com.qiao.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiao.domain.User;
import com.qiao.service.UserService;
import com.qiao.util.JedisUtil;
import com.qiao.util.JsonUtil;

@Controller
@RequestMapping("/user")
@Scope("prototype")
public class UserController {
	private final Logger LOGGER = Logger.getLogger(UserController.class);
	@Resource
	private UserService userService;
	
	@Resource
	private JedisUtil jedisUtil;
	
	@ResponseBody
	@RequestMapping("/get")
	public String get(){
		String msg = "";
		try {
			List<User> list = userService.getAllUsersByHibernate();
			msg = JsonUtil.obj2json(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public String getUserById(@PathVariable String id){
		String msg = "";
		try {
			List<User> list = userService.getUserById(id);
			msg = JsonUtil.obj2json(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/update")
	public String updateUserAge(String id, int age){
		String msg = "success";
		try{
			userService.updateUserAge(id, age);
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/add")
	public String add(String name, int age, String address){
		String msg = "success";
		try{
			userService.addUser(name, age, address);
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/delete")
	public String deleteUserById(String id){
		String msg = "redirect:/user/get.do";
		try{
			userService.deleteUserById(id);;
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/addUsers")
	public String addUsers(int count){
		String msg = "success";
		try{
			userService.addUsers(count);;
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/test1")
	public String test1(){
		String msg = "success";
		try{
			msg = userService.test1();
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/test2")
	public String test2(String id){
		String msg = "success";
		try{
			msg = userService.test2(id);
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping("/test3")
	public String test3(){
		String msg = "success";
		try{
			msg = userService.test3();
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
	@ResponseBody
	@RequestMapping("/test4")
	public String test4(){
		String msg = "success";
		try{
			msg = jedisUtil.getValue("a");
		}catch(Exception e){
			msg = "error";
			e.printStackTrace();
		}
		return msg;
	}
}
