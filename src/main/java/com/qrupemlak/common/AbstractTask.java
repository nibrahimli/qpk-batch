package com.qrupemlak.common;

import org.springframework.context.ApplicationContext;

public abstract class AbstractTask {
	
	private ApplicationContext applicationContext;
	
	public AbstractTask() {
		String[] contexts = onLoadApplicationContext();
		if (contexts != null) {
			applicationContext = CoreBackendContext.getInstance(contexts);
		}
	}
	
	/**
	 * To get a bean from the contexts.
	 * 
	 * @param beanName the concerned bean name.
	 * @return the bean.
	 */
	protected final Object getBean(final String beanName) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanName);
	}

	/**
	 * To get a bean from the contexts.
	 * 
	 * @param beanName the concerned bean name.
	 * @param requiredType the required bean type.
	 * @return the bean.
	 */
	public <T> T getBean(String beanName, Class<T> requiredType)
	{
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanName, requiredType);
	}
	
	
	
	protected abstract String[] onLoadApplicationContext();
	

}
