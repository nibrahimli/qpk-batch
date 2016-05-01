package com.qrupemlak.scheduler;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.nibrahimli.database.qrupEmlak.dao.AnnouncementDao;
import com.nibrahimli.database.qrupEmlak.dao.QrupEmlakSitemapDao;
import com.nibrahimli.database.qrupEmlak.entity.Announcement;
import com.nibrahimli.database.qrupEmlak.entity.QrupEmlakSitemap;
import com.nibrahimli.database.qrupEmlak.entity.QrupEmlakSitemap.ChangeFreq;
import com.qrupemlak.common.AbstractTask;
import com.qrupemlak.common.CoreBackendContext;


public class URLGeneration extends AbstractTask implements Job{
	
	private final static Logger logger = LoggerFactory.getLogger(URLGeneration.class);
	public final static String APP_CONTEXT = "/spring/context.xml";
	
	private AnnouncementDao announcementDao;
	private QrupEmlakSitemapDao qrupEmlakSitemapDao ;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ApplicationContext appContext = null ;
		try {
			appContext =  CoreBackendContext.getInstance();
		} catch (Exception e) {
			logger.error("error occurred {}", e.getMessage());
		}
		announcementDao = (AnnouncementDao) appContext
				.getBean("announcementDaoImpl");
		qrupEmlakSitemapDao = (QrupEmlakSitemapDao) appContext
				.getBean("qrupEmlakSitemapDaoImpl");
		
		List<Announcement> announcementList = announcementDao.getAllDistinctOrderByDate();
		List<QrupEmlakSitemap> qrupEmlakSitemapList = qrupEmlakSitemapDao.getAll();
		for (Announcement announcement : announcementList) {
			String title = announcement.getTitle().replaceAll(" ", "-").toLowerCase();
			String loc = "http://qrupemlak.com/elan/"+title+"---"+announcement.getId();
			if(!isLocExist(qrupEmlakSitemapList, loc)){
				QrupEmlakSitemap qrupEmlakSitemap = new QrupEmlakSitemap();
				qrupEmlakSitemap.setLoc(loc);
				qrupEmlakSitemap.setChangefreq(ChangeFreq.never);
				qrupEmlakSitemap.setLastMod(new Date());
				qrupEmlakSitemap.setPriority(new Float(1.0));
				qrupEmlakSitemapDao.create(qrupEmlakSitemap);
			}
		}
		
		//contact page
		String contactLoc = "http://qrupemlak.com/kontakt/";
		if(!isLocExist(qrupEmlakSitemapList, contactLoc)){
			QrupEmlakSitemap qrupEmlakSitemap = new QrupEmlakSitemap();
			qrupEmlakSitemap.setLoc(contactLoc);
			qrupEmlakSitemap.setChangefreq(ChangeFreq.never);
			qrupEmlakSitemap.setLastMod(new Date());
			qrupEmlakSitemap.setPriority(new Float(0.5));
			qrupEmlakSitemapDao.create(qrupEmlakSitemap);
		}
		
		//home page
		String homeLoc = "http://qrupemlak.com/";
		if(!isLocExist(qrupEmlakSitemapList, homeLoc)){
			QrupEmlakSitemap qrupEmlakSitemap = new QrupEmlakSitemap();
			qrupEmlakSitemap.setLoc(homeLoc);
			qrupEmlakSitemap.setChangefreq(ChangeFreq.daily);
			qrupEmlakSitemap.setLastMod(new Date());
			qrupEmlakSitemap.setPriority(new Float(1.0));
			qrupEmlakSitemapDao.create(qrupEmlakSitemap);
		}
	
//		advanced search
		String advancedSearchLoc = "http://qrupemlak.com/axtaris/";
		if(!isLocExist(qrupEmlakSitemapList, homeLoc)){
			QrupEmlakSitemap qrupEmlakSitemap = new QrupEmlakSitemap();
			qrupEmlakSitemap.setLoc(advancedSearchLoc);
			qrupEmlakSitemap.setChangefreq(ChangeFreq.daily);
			qrupEmlakSitemap.setLastMod(new Date());
			qrupEmlakSitemap.setPriority(new Float(1.0));
			qrupEmlakSitemapDao.create(qrupEmlakSitemap);
		}
	}
	
	private boolean isLocExist(List<QrupEmlakSitemap> qrupEmlakSitemapList, String loc) {
		for (QrupEmlakSitemap qrupEmlakSitemap : qrupEmlakSitemapList) {
			if(qrupEmlakSitemap.getLoc().equals(loc))
				return true;
		}
		return false;
	}

	@Override
	protected String[] onLoadApplicationContext() {
		return new String[] {APP_CONTEXT};
	}
}