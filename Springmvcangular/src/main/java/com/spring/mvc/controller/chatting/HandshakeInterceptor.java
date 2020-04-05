package com.spring.mvc.controller.chatting;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;  

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{
  
	private static final Log logger = LogFactory.getLog(HttpSessionHandshakeInterceptor.class);

	private Collection<String> attributeNames;
 
	@Override
    public boolean beforeHandshake(ServerHttpRequest request,ServerHttpResponse response, WebSocketHandler wsHandler,
         Map<String, Object> attributes) throws Exception {    
     System.out.println("들어왔는감");
    	if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			if (session != null) {
				Enumeration<String> names = session.getAttributeNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					if (CollectionUtils.isEmpty(this.attributeNames) || this.attributeNames.contains(name)) {
						if (logger.isTraceEnabled()) {
							logger.trace("Adding HTTP session attribute to handshake attributes: " + name);
						}
						attributes.put(name, session.getAttribute(name));
						attributes.put("aa", session.getAttribute("aa"));
						System.out.println(session.getAttribute(name));
						System.out.println(session.getAttribute("aa"));
						
					}
					else {
						if (logger.isTraceEnabled()) {
							logger.trace("Skipped HTTP session attribute");
	 					}
					}
				}
			}
		}
		return true;
    }
  
    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception ex) {
        System.out.println("After Handshake");
  
        super.afterHandshake(request, response, wsHandler, ex);
    }
  
}


