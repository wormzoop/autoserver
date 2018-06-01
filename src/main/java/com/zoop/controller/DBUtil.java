package com.zoop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {

	private static String driver = "com.mysql.jdbc.Driver";
	
	private static String url = "";
	
	private static String username = "";
	
	private static String password = "";
	
	public static String database = "";
	
	public static String pk = "";
	
	public static String type = "";
	
	public static String dir = "";
	
	//读取配置文件
	static {
		InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			url = pro.getProperty("url");
			username = pro.getProperty("username");
			password = pro.getProperty("password");
			pk = pro.getProperty("pk");
			type = pro.getProperty("type");
			database = pro.getProperty("database");
			dir = pro.getProperty("dir");
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		
	}
	
	//获得连接
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,username,password);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
}
