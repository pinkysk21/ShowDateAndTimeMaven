package com.develop.ShowDateAndTime;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.time.StopWatch;


@WebListener
public class MyListner implements ServletContextListener {
	public static Logger logger;
	public static StopWatch watch;
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	//	ServletContextListener.super.contextDestroyed(sce);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		logger=Logger.getLogger(MyListner.class.getName());
		watch=new StopWatch(getClass().getName());
		FileHandler fhandler;
		try {
		//	C:\Users\nandu\eclipse-workspace\
			File file=new File("C:\\Users\\nandu\\eclipse-workspace\\ShowDateAndTime\\app.log");
			if(!file.exists()) {
				file.createNewFile();
			}
			fhandler = new FileHandler("C:\\Users\\nandu\\eclipse-workspace\\ShowDateAndTime\\app.log",true);
			logger.addHandler(fhandler);
			SimpleFormatter format=new SimpleFormatter();
			fhandler.setFormatter(format);
			logger.info("Starting the application");
			
			System.out.println("Started");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	ServletContextListener.super.contextInitialized(sce);
	}
}
