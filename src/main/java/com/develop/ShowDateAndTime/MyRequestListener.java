package com.develop.ShowDateAndTime;
import java.util.EventListener;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyRequestListener implements ServletRequestListener{
    public HttpServletRequest req;
	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInitialized(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
		req=(HttpServletRequest)arg0.getServletRequest();
		String requestId = UUID.randomUUID().toString();
		req.setAttribute("reqId", requestId);
		System.out.println(requestId);
		
		
	}
	

}
