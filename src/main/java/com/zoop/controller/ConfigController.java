package com.zoop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class ConfigController extends HttpServlet{

	private static final long serialVersionUID = 4977025497996042936L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		String sql = "select table_name as name from information_schema.tables where table_schema='"+
		DBUtil.database+"' and table_type='base table'";
		List<String> list = Handle.all(sql);
		String arr = JSON.toJSONString(list);
		PrintWriter out = response.getWriter();
		out.write(arr);
		out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
	}
	
}
