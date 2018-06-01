package com.zoop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeController extends HttpServlet{

	private static final long serialVersionUID = 6752895023494251078L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		String table = request.getParameter("table");
		String sql = "select column_name as name from information_schema.columns where table_schema='"+
		DBUtil.database+"' and table_name='"+table+"'";
		String sql_comment = "SELECT column_comment as name FROM INFORMATION_SCHEMA.Columns WHERE table_name='"+
		table+"' AND table_schema='"+DBUtil.database+"'";
		List<String> list = Handle.all(sql);
		List<String> listComment = Handle.all(sql_comment);
		Handle.generateBean(Handle.clazzHandle(table),Handle.property(list),listComment);
		Handle.generateDao(Handle.clazzHandle(table));
		Handle.generateService(Handle.clazzHandle(table));
		Handle.generateServiceImpl(Handle.clazzHandle(table));
		Handle.generateMapper(Handle.clazzHandle(table),list,Handle.property(list),table);
		Handle.generateController(Handle.clazzHandle(table));
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
	}
	
}
