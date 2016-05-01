package com.qrupemlak.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CoreBackendContext {

	static private ApplicationContext applicationContext;
	
	public static ApplicationContext getInstance() throws Exception{
		if(applicationContext != null){
			return applicationContext;
		}
		else{
			throw new Exception("ApplicationContext is null");
		}
		
	}
	
	public static ApplicationContext getInstance(String[] contexts){
		if(applicationContext == null){
			applicationContext = new ClassPathXmlApplicationContext(contexts);
		}
		return applicationContext;
	}
	
}