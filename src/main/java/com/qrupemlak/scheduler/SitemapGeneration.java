package com.qrupemlak.scheduler;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitemapGeneration implements Daemon {
	
	private final static Logger logger = LoggerFactory.getLogger(SitemapGeneration.class); 
	private Scheduler scheduler = null;

	@Override
	public void destroy() {
		if(scheduler != null) scheduler = null;
	}

	@Override
	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
	}

	@Override
	public void start() throws Exception {
		
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JobDetail job = newJob(URLGeneration.class) 
			    .withIdentity("jobURLGeneration", "qrupEmlakSitemap") 
			    .build(); 
			        
			// Trigger the job to run now, and then repeat every 40 seconds 
			Trigger trigger = newTrigger() 
			    .withIdentity("triggerSitemap", "qrupEmlakSitemap") 
			    .startNow() 
			    .withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(1, 6, 0))            
			    .build(); 
			        
			// Tell quartz to schedule the job using our trigger 
			scheduler.scheduleJob(job, trigger);
			
			scheduler.start();
			logger.info("start url generation job for sitemap");
	}
	

	@Override
	public void stop() throws Exception {
		if(scheduler != null) scheduler.shutdown();
	}
	
	public static void main(String[] args) {
		SitemapGeneration sitemap = new SitemapGeneration();
		try {
			sitemap.start();
		} catch (Exception e) {
			logger.error("error occurred {}", e.getMessage());
		}
	}

}
