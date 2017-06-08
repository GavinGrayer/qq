package com.qq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import sun.security.util.Password;



public class login {
	
	private user user;
	private Properties p;
	
	
	
	public login() {
		super();
		// TODO Auto-generated constructor stub
	}

	public login(user user) {
		super();
		this.user = user;
	}
	
	
	
	
	
	public int judge(){
		
		Properties p = new Properties();
		
		int a = 0;
		
		try {
			InputStream fileurl = login.class.getClassLoader().getResourceAsStream("resource/6.txt");

			p.load(fileurl);
			
			String password = p.getProperty(user.getUsername());
			
			
			if(user.getPassword().equals(password)){
				
				return 1;
				
			}
			
			
			
			
			
			
//			if(user.getUsername().equals(username)&&user.getPassword().equals(password)){
//				
//				return 1;
//				
//			}
			
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return a;
		
	}
	
}
