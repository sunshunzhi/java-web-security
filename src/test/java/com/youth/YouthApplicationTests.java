package com.youth;

import com.youth.dao.MenuDao;
import com.youth.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class YouthApplicationTests {

	@Autowired
	private UserDao userDao;

//	@Autowired
//	PasswordEncoder passwordEncoder;

	@Autowired
	MenuDao menuDao;

	@Test
	void contextLoads() {

//		menuDao.selectPermsByUserId(2L).forEach(System.out::println);
//		userDao.selectList(null).forEach(System.out::println);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("111"));
		System.out.println(passwordEncoder.matches("111", "$2a$10$6/yfcPXsHpBl9u6DK/6YXeK8vce8novEnBAIcTqvSlGsFC4tdtRUa"));
	}

}
