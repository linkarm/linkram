package com.link.ram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan 
public class MessageServiceStarter 
{
    public static void main( String[] args ){
    	SpringApplication.run(MessageServiceStarter.class, args);
    }
}
