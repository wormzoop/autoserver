package com.zoop.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Handle {

	/**
	 * 获得所有的表名称
	 * 获得表所有字段的名称
	 * @return
	 */
	public static List<String> all(String sql) {
		Connection con = null;
		List<String> list = new ArrayList<String>();
		try {
			con = DBUtil.getConnection();
			Statement state = con.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while(rs.next()) {
				list.add(rs.getString("name"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				con.close();
			}catch(Exception e) {
				
			}
		}
		return list;
	}
	
	//生成实体类字段
	public static List<String> property(List<String> list){
		List<String> l = new ArrayList<String>();
		for(String str : list){
			int len;
			while((len = str.indexOf('_')) != -1){
				str = str.replaceAll("_"+str.substring(len+1, len+2), str.substring(len+1, len+2).toUpperCase());
			}
			l.add(str);
		}
		return l;
	}
	
	//生成类名
	public static String clazzHandle(String str){
		str = str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
		int len;
		while((len = str.indexOf('_')) != -1){
			str = str.replaceAll("_"+str.substring(len+1, len+2), str.substring(len+1, len+2).toUpperCase());
		}
		return str;
	}
	
	/**
	 * 生成实体类
	 * @param clazz 类名
	 * @param column 表字段列表
	 */
	public static void generateBean(String clazz, List<String> column1, List<String> comment){
		try{
			List<String> column = new ArrayList<String>();
			column.addAll(column1);
			List<String> columnPage = new ArrayList<String>();
			columnPage.add("pageSize");
			columnPage.add("pageNo");
			columnPage.add("start");
			columnPage.add("end");
			File file = new File(DBUtil.dir+clazz+".java");
			if(!file.exists()){
				file.createNewFile();
			}
			StringBuilder text = new StringBuilder();
			text.append("package "+DBUtil.pk+".model."+DBUtil.type+";\r\n\r\npublic class "+clazz+"{\r\n\r\n\t");
			for(int i = 0; i < column.size(); i++) {
				text.append("private String "+column.get(i)+";//"+comment.get(i)+"\r\n\t");
			}
			text.append("private Integer pageSize ;\r\n\t");
			text.append("private Integer pageNo;\r\n\t");
			text.append("private Integer start;\r\n\t");
			text.append("private Integer end;\r\n\t");
			text.append("\r\n\t");
			for(int i = 0; i < column.size(); i++){
				String str = column.get(i);
				text.append("public void set"+str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase())+"(String "+str+"){\r\n\t\t");
				text.append("this."+str+" = "+str+";\r\n\t}\r\n\t");
				text.append("public String get"+str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase())+"(){\r\n\t\t");
				text.append("return "+str+";\r\n\t}\r\n\t");
			}
			for(int i = 0; i < columnPage.size(); i++) {
				String str = columnPage.get(i);
				text.append("public void set"+str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase())+"(Integer "+str+"){\r\n\t\t");
				text.append("this."+str+" = "+str+";\r\n\t}\r\n\t");
				text.append("public Integer get"+str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase())+"(){\r\n\t\t");
				if(i == columnPage.size()-1){
					text.append("return "+str+";\r\n\t}\r\n");
				}else{
					text.append("return "+str+";\r\n\t}\r\n\t");
				}
			}
			text.append("}");
			FileOutputStream out = new FileOutputStream(file);
			out.write(text.toString().getBytes());
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成dao
	 * @param clazz 类名
	 */
	public static void generateDao(String clazz){
		try{
			File file = new File(DBUtil.dir+clazz+"Dao.java");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StringBuilder sb = new StringBuilder();
			sb.append("package "+DBUtil.pk+".dao."+DBUtil.type+";\r\n\r\nimport "+DBUtil.pk+".model."+DBUtil.type+"."+clazz+";\r\n");
			sb.append("import org.mybatis.spring.annotation.MapperScan;\r\n");
			sb.append("import java.util.List;\r\n\r\n");
			sb.append("@MapperScan\r\npublic interface "+clazz+"Dao{\r\n\r\n\t");
			sb.append("Integer count("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("List<"+clazz+"> list("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("Integer add("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("Integer delete("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("Integer update("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append(clazz + " detail("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n}");
			out.write(sb.toString().getBytes());
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成service
	 * @param clazz 类名
	 */
	public static void generateService(String clazz){
		try{
			File file = new File(DBUtil.dir+clazz+"Service.java");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StringBuilder sb = new StringBuilder();
			sb.append("package "+DBUtil.pk+".service."+DBUtil.type+";\r\n\r\nimport "+DBUtil.pk+".model."+DBUtil.type+"."+clazz+";\r\n");
			sb.append("import java.util.List;\r\n\r\n");
			sb.append("public interface "+clazz+"Service{\r\n\r\n\t");
			sb.append("Integer count("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("List<"+clazz+"> list("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("Integer add("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("Integer delete("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append("Integer update("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n\t");
			sb.append(clazz + " detail("+clazz+" "+clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase())+");\r\n\r\n}");
			out.write(sb.toString().getBytes());
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成serviceImpl
	 * @param clazz 类名
	 */
	public static void generateServiceImpl(String clazz){
		try{
			File file = new File(DBUtil.dir+clazz+"ServiceImpl.java");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StringBuilder sb = new StringBuilder();
			String low = clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase());
			//sb.append("package "+DBUtil.pk+".service.impl."+DBUtil.type+";\r\n\r\nimport "+DBUtil.pk+".model."+DBUtil.type+"."+clazz+";\r\n");
			sb.append("package "+DBUtil.pk+".impl."+DBUtil.type+";\r\n\r\nimport "+DBUtil.pk+".model."+DBUtil.type+"."+clazz+";\r\n");
			sb.append("import org.springframework.stereotype.Service;\r\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
			sb.append("import java.util.List;\r\n");
			sb.append("import "+DBUtil.pk+".dao."+DBUtil.type+"."+clazz+"Dao;\r\n\r\n");
			sb.append("import "+DBUtil.pk+".service."+DBUtil.type+"."+clazz+"Service;\r\n\r\n");
			sb.append("@Service\r\n");
			sb.append("public class "+clazz+"ServiceImpl implements "+clazz+"Service{\r\n\r\n\t");
			sb.append("@Autowired\r\n\tprivate "+clazz+"Dao "+low+"Dao;\r\n\r\n\t");
			sb.append("public Integer count("+clazz+" "+low+"){\r\n\t\t");
			sb.append("return "+low+"Dao.count("+low+");\r\n\t}\r\n\r\n\t");
			sb.append("public List<"+clazz+"> list("+clazz+" "+low+"){\r\n\t\t");
			sb.append("return "+low+"Dao.list("+low+");\r\n\t}\r\n\r\n\t");
			sb.append("public Integer add("+clazz+" "+low+"){\r\n\t\t");
			sb.append("return "+low+"Dao.add("+low+");\r\n\t}\r\n\r\n\t");
			sb.append("public Integer delete("+clazz+" "+low+"){\r\n\t\t");
			sb.append("return "+low+"Dao.delete("+low+");\r\n\t}\r\n\r\n\t");
			sb.append("public Integer update("+clazz+" "+low+"){\r\n\t\t");
			sb.append("return "+low+"Dao.update("+low+");\r\n\t}\r\n\r\n\t");
			sb.append("public "+clazz+" detail("+clazz+" "+low+"){\r\n\t\t");
			sb.append("return "+low+"Dao.detail("+low+");\r\n\t}\r\n\r\n}");
			out.write(sb.toString().getBytes());
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成mapper文件
	 * @param clazz 类名
	 * @param column 表字段列表
	 * @param proper 实体类字段
	 * @param table 表名
	 */
	public static void generateMapper(String clazz, List<String> column, List<String> proper, String table) {
		try {
			File file = new File(DBUtil.dir+clazz+"Mapper.xml");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StringBuilder sb = new StringBuilder();
			String low = clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0,1).toLowerCase());
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
			sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
			sb.append("<mapper namespace=\""+DBUtil.pk+".dao."+DBUtil.type+"."+clazz+"Dao\">\r\n\r\n\t");
			sb.append("<resultMap id=\""+low+"Map\" type=\""+DBUtil.pk+".model."+DBUtil.type+"."+clazz+"\">\r\n\t\t");
			sb.append("<id column=\""+column.get(0)+"\" property=\""+proper.get(0)+"\"/>\r\n\t\t");
			for(int i = 1; i < column.size(); i++) {
				if(i == column.size()-1) {
					sb.append("<result column=\""+column.get(i)+"\" property=\""+proper.get(i)+"\"/>\r\n\t");
				}else {
					sb.append("<result column=\""+column.get(i)+"\" property=\""+proper.get(i)+"\"/>\r\n\t\t");
				}
			}
			sb.append("</resultMap>\r\n\r\n\t");
			//---------------count-----------------------
			sb.append("<select id=\"count\" resultType=\"int\">\r\n\t\t");
			sb.append("select count(*) from "+table+" where 1=1 \r\n\t\t");
			for(int i = 0; i < column.size(); i++) {
				sb.append("<if test=\""+proper.get(i)+"!=null and "+proper.get(i)+"!=''\">\r\n\t\t\t");
				sb.append("and "+column.get(i)+"=#{"+proper.get(i)+"}\r\n\t\t");
				sb.append("</if>\r\n\t\t");
			}
			sb.append("limit #{start},#{end}\r\n\t");
			sb.append("</select>\r\n\r\n\t");
			//--------------list-----------------------
			sb.append("<select id=\"list\" resultMap=\""+low+"Map\">\r\n\t\t");
			sb.append("select ");
			for(int i = 0; i < column.size(); i++) {
				if(i == column.size()-1) {
					sb.append(column.get(i));
				}else {
					sb.append(column.get(i)+", ");
				}
			}
			sb.append(" from "+table+" where 1=1 \r\n\t\t");
			for(int i = 0; i < column.size(); i++) {
				sb.append("<if test=\""+proper.get(i)+"!=null and "+proper.get(i)+"!=''\">\r\n\t\t\t");
				sb.append("and "+column.get(i)+"=#{"+proper.get(i)+"}\r\n\t\t");
				sb.append("</if>\r\n\t\t");
			}
			sb.append("limit #{start},#{end}\r\n\t");
			sb.append("</select>\r\n\r\n\t");
			//-------------------add-------------------------------
			sb.append("<insert id=\"add\">\r\n\t\t");
			sb.append("insert into "+table+"(");
			for(int i = 0; i < column.size(); i++) {
				if(i == column.size()-1) {
					sb.append(column.get(i)+")");
				}else {
					sb.append(column.get(i)+",");
				}
			}
			sb.append(" values (");
			for(int i = 0; i < proper.size(); i++) {
				if(i == proper.size()-1) {
					sb.append("#{"+proper.get(i)+"})");
				}else {
					sb.append("#{"+proper.get(i)+"},");
				}
			}
			sb.append("\r\n\t</insert>\r\n\r\n\t");
			//-------------delete----------------------
			sb.append("<delete id=\"delete\">\r\n\t\t");
			sb.append("delete from "+table+" where id = #{id}\r\n\t</delete>\r\n\r\n\t");
			//-------------detail----------------------
			sb.append("<select id=\"detail\" resultMap=\""+low+"Map\">\r\n\t\t");
			sb.append("select ");
			for(int i = 0; i < column.size(); i++) {
				if(i == column.size()-1) {
					sb.append(column.get(i));
				}else {
					sb.append(column.get(i)+", ");
				}
			}
			sb.append(" from "+table+" where id=#{id}\r\n\t</select>\r\n\r\n\t");
			//-------------update----------------------
			sb.append("<update id=\"update\">\r\n\t\t");
			sb.append("update "+table+"\r\n\t\t");
			sb.append("<set>\r\n\t\t\t");
			for(int i = 1; i < column.size(); i++) {
				if(i == column.size()-1) {
					sb.append("<if test=\""+proper.get(i)+"!=null\">\r\n\t\t\t\t");
					sb.append(column.get(i)+"=#{"+proper.get(i)+"}\r\n\t\t\t");
					sb.append("</if>\r\n\t\t");
				}else {
					sb.append("<if test=\""+proper.get(i)+"!=null\">\r\n\t\t\t\t");
					sb.append(column.get(i)+"=#{"+proper.get(i)+"},\r\n\t\t\t");
					sb.append("</if>\r\n\t\t\t");
				}
			}
			sb.append("</set>\r\n\t\t");
			sb.append("where "+column.get(0)+"=#{"+proper.get(0)+"}\r\n\t</update>\r\n\r\n</mapper>");
			out.write(sb.toString().getBytes());
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成controller
	 * @param clazz 类名
	 */
	public static void generateController(String clazz){
		try{
			File file = new File(DBUtil.dir+clazz+"Controller.java");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StringBuilder sb = new StringBuilder();
			String low = clazz.replaceFirst(clazz.substring(0,1), clazz.substring(0, 1).toLowerCase());
			sb.append("package "+DBUtil.pk+".controller."+DBUtil.type+";\r\n\r\n");
			sb.append("import org.apache.log4j.Logger;\r\n");
			sb.append("import "+DBUtil.pk+".model."+DBUtil.type+"."+clazz+";\r\n");
			sb.append("import java.util.List;\r\n");
			sb.append("import java.util.Map;\r\n");
			sb.append("import java.util.HashMap;\r\n");
			sb.append("import javax.servlet.http.HttpServletRequest;\r\n");
			sb.append("import org.springframework.web.bind.annotation.CrossOrigin;\r\n");
			sb.append("import org.springframework.web.bind.annotation.RequestBody;\r\n");
			sb.append("import org.springframework.web.bind.annotation.ResponseBody;\r\n");
			sb.append("import com.heisa.platform.util.ConstantUtil;\r\n");
			sb.append("import com.heisa.platform.util.ResponseObject;\r\n");
			sb.append("import io.swagger.annotations.Api;\r\n");
			sb.append("import io.swagger.annotations.ApiOperation;\r\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
			sb.append("import org.springframework.stereotype.Controller;\r\n");
			sb.append("import org.springframework.web.bind.annotation.RequestMapping;\r\n");
			sb.append("import "+DBUtil.pk+".service."+DBUtil.type+"."+clazz+"Service;\r\n\r\n");
			sb.append("@Api(tags = \"\")\r\n");
			sb.append("@CrossOrigin\r\n");
			sb.append("@Controller\r\n");
			sb.append("@RequestMapping(\""+low+"\")\r\n");
			sb.append("public class "+clazz+"Controller{\r\n\r\n\t");
			sb.append("private static final Logger logger = Logger.getLogger("+clazz+"Controller.class);\r\n\r\n\t");
			sb.append("@Autowired\r\n\t");
			sb.append("private "+clazz+"Service "+low+"Service;\r\n\r\n\t");
			sb.append("@ApiOperation(value = \"count\", notes = \"count\", httpMethod = \"POST\")\r\n\t");
			sb.append("@RequestMapping(value=\"/count\")\r\n\t");
			sb.append("@ResponseBody\r\n\t");
			sb.append("public ResponseObject count(@RequestBody(required=false) "+clazz+" "+low+", HttpServletRequest request){\r\n\t\t");
			sb.append("try{\r\n\t\t\t");
			sb.append("int count = "+low+"Service.count("+low+");\r\n\t\t\t");
			sb.append("Map<String, Object> map = new HashMap<String, Object>();\r\n\t\t\t");
			sb.append("map.put(\"count\",count);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.TRUE, 200, map);\r\n\t\t");
			sb.append("}catch(Exception e){\r\n\t\t\t");
			sb.append("logger.error(e.getMessage(),e);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.FALSE, 500, \"请求失败\");\r\n\t\t}\r\n\t}\r\n\r\n\t");
			sb.append("@ApiOperation(value = \"list\", notes = \"list\", httpMethod = \"POST\")\r\n\t");
			sb.append("@RequestMapping(value=\"/list\")\r\n\t");
			sb.append("@ResponseBody\r\n\t");
			sb.append("public ResponseObject list(@RequestBody(required=false) "+clazz+" "+low+", HttpServletRequest request){\r\n\t\t");
			sb.append("try{\r\n\t\t\t");
			sb.append(low+".setStart("+low+".getPageSize() * ("+low+".getPageNo() - 1));\r\n\t\t\t");
			sb.append(low+".setEnd("+low+".getPageSize());\r\n\t\t\t");
			sb.append("List<"+clazz+"> list = "+low+"Service.list("+low+");\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.TRUE, 200, list);\r\n\t\t");
			sb.append("}catch(Exception e){\r\n\t\t\t");
			sb.append("logger.error(e.getMessage(), e);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.FALSE, 500, \"请求失败\");\r\n\t\t}\r\n\t}\r\n\r\n\t");
			sb.append("@ApiOperation(value = \"add\", notes = \"add\", httpMethod = \"POST\")\r\n\t");
			sb.append("@RequestMapping(value=\"/add\")\r\n\t");
			sb.append("@ResponseBody\r\n\t");
			sb.append("public ResponseObject add(@RequestBody(required=false) "+clazz+" "+low+", HttpServletRequest request){\r\n\t\t");
			sb.append("try{\r\n\t\t\t");
			sb.append(low+"Service.add("+low+");\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.TRUE, 200, \"新增成功\");\r\n\t\t");
			sb.append("}catch(Exception e){\r\n\t\t\t");
			sb.append("logger.error(e.getMessage(), e);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.FALSE, 500, \"请求失败\");\r\n\t\t}\r\n\t}\r\n\r\n\t");
			sb.append("@ApiOperation(value = \"delete\", notes = \"delete\", httpMethod = \"POST\")\r\n\t");
			sb.append("@RequestMapping(value=\"/delete\")\r\n\t");
			sb.append("@ResponseBody\r\n\t");
			sb.append("public ResponseObject delete(@RequestBody(required=false) "+clazz+" "+low+", HttpServletRequest request){\r\n\t\t");
			sb.append("try{\r\n\t\t\t");
			sb.append(low+"Service.delete("+low+");\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.TRUE, 200, \"删除成功\");\r\n\t\t");
			sb.append("}catch(Exception e){\r\n\t\t\t");
			sb.append("logger.error(e.getMessage(), e);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.FALSE, 500, \"删除失败\");\r\n\t\t}\r\n\t}\r\n\r\n\t");
			sb.append("@ApiOperation(value = \"update\", notes = \"update\", httpMethod = \"POST\")\r\n\t");
			sb.append("@RequestMapping(value=\"/update\")\r\n\t");
			sb.append("@ResponseBody\r\n\t");
			sb.append("public ResponseObject update(@RequestBody(required=false) "+clazz+" "+low+", HttpServletRequest request){\r\n\t\t");
			sb.append("try{\r\n\t\t\t");
			sb.append(low+"Service.update("+low+");\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.TRUE, 200, \"修改成功\");\r\n\t\t");
			sb.append("}catch(Exception e){\r\n\t\t\t");
			sb.append("logger.error(e.getMessage(), e);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.FALSE, 500, \"修改失败\");\r\n\t\t}\r\n\t}\r\n\r\n\t");
			sb.append("@ApiOperation(value = \"detail\", notes = \"detail\", httpMethod = \"POST\")\r\n\t");
			sb.append("@RequestMapping(value=\"/detail\")\r\n\t");
			sb.append("@ResponseBody\r\n\t");
			sb.append("public ResponseObject detail(@RequestBody(required=false) "+clazz+" "+low+", HttpServletRequest request){\r\n\t\t");
			sb.append("try{\r\n\t\t\t");
			sb.append(clazz+" detail = "+low+"Service.detail("+low+");\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.TRUE, 200, detail);\r\n\t\t");
			sb.append("}catch(Exception e){\r\n\t\t\t");
			sb.append("logger.error(e.getMessage(), e);\r\n\t\t\t");
			sb.append("return new ResponseObject(ConstantUtil.FALSE, 500, \"修改失败\");\r\n\t\t}\r\n\t}\r\n\r\n}");
			out.write(sb.toString().getBytes());
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[]args) {
		String sql = "select table_name as name from information_schema.tables where table_schema='pyburial' and table_type='base table'";
		String sql1 = "select column_name as name from information_schema.columns where table_schema='pyburial' and table_name='sys_dictionary'";
		List<String> list = all(sql1);
		generateController(clazzHandle("sys_dictionary"));
	}
	
}
