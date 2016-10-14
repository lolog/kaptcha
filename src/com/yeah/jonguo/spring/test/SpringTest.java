package com.yeah.jonguo.spring.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.10.11
 * @company        CIMCSSC
 * @description    spring framework environment
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/app.xml")
public class SpringTest {
	@Autowired
	private Date date;
	
	@Test
	public void springTest1 () {
		System.out.println(date);
	}
}
