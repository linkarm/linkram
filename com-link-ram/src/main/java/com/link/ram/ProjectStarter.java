package com.link.ram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.link.ram.core.socket.SocketServer;

@SpringBootApplication
@ServletComponentScan 
public class ProjectStarter 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(ProjectStarter.class, args);
    	
    	SocketServer.getSocketServerInstance();
    }
}
