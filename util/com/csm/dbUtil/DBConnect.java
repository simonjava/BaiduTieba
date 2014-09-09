package com.csm.dbUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.csm.fileUtil.DirUtil;

public class DBConnect
{
	private static final String ConfigFilePath = "config/mysql.properties";
	private static final String driver;
	private static final String user;
	private static final String password;
	private static final String ip;
	private static final String port;
	private static final String database;
	private static final String characterEncoding;
	private static final String url;
	static
	{
		Properties mysqlProperties = new Properties();
		try
		{
			File configFile = new File(DirUtil.getProjectDir()+ConfigFilePath);
			System.out.println("程序认为的数据config目录"+configFile.getAbsolutePath());
			mysqlProperties.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		driver = mysqlProperties.getProperty("driver","com.mysql.jdbc.Driver");
		user = mysqlProperties.getProperty("user","root");
		password = mysqlProperties.getProperty("password","123");
		ip = mysqlProperties.getProperty("ip","127.0.0.1");
		port = mysqlProperties.getProperty("port","3306");
		database = mysqlProperties.getProperty("database");
		characterEncoding = mysqlProperties.getProperty("characterEncoding","utf-8");
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("jdbc:mysql://").append(ip).append(":").append(port).append("/").append(database).append("?useUnicode=true&characterEncoding=").append(characterEncoding);
		url = urlBuilder.toString();
		System.out.println("数据库初始化完成 url = "+url);
	}
	public static Connection getConnection()
	{
		try
		{
			Class.forName(driver); // 加载驱动
			Connection conn = DriverManager.getConnection(url, user, password); // 登陆
			return conn;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void closeAll(Connection conn, PreparedStatement prsts,
			ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (prsts != null)
		{
			try
			{
				prsts.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (conn != null)
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{

	}

}
