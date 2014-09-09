package com.csm.dbUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProcessStatement
{
	String sql;
	List<Object> values;

	public ProcessStatement(String sql, List<Object> values)
	{
		this.sql = sql;
		this.values = values;
	}
	
	public String startUpdate()
	{
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			con = DBConnect.getConnection();
			ps = con.prepareStatement(sql);
			process(ps);
			ps.executeUpdate();
			return "执行" + sql + "成功";
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DBConnect.closeAll(con, ps, null);
		}
		return "执行" + sql + "失败";
	}
	
	/**
	 * @return 返回查询结果集的数目
	 */
	public int startCount()
	{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			con = DBConnect.getConnection();
			ps = con.prepareStatement(sql);
			process(ps);
			rs = ps.executeQuery();
			System.out.println("执行" + sql + "成功,现在返回结果");
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DBConnect.closeAll(con, ps, rs);
		}
		System.err.println("执行" + sql + "失败");
		return -1;
	}
	
	/**
	 * @return 返回ResultSet结果集，不封装
	 */
	public ResultSet startQuery()
	{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			con = DBConnect.getConnection();
			ps = con.prepareStatement(sql);
			process(ps);
			rs = ps.executeQuery();
			System.out.println("执行" + sql + "成功,现在直接返回ResultSet结果");
			return rs;
		} catch (SQLException e)
		{
			e.printStackTrace();
		} 
		System.err.println("执行" + sql + "失败");
		return null;
	}
	
	/**
	 * @param objClass
	 *            用于返回的封装好的实体类，需要知道是哪个实体类
	 * @return
	 */
	public List<?> startQuery(Class<?> objClass)
	{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			con = DBConnect.getConnection();
			ps = con.prepareStatement(sql);
			process(ps);
			rs = ps.executeQuery();
			System.out.println("执行" + sql + "成功,现在收集结果");
			return allocate(objClass,rs);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DBConnect.closeAll(con, ps, rs);
		}
		System.out.println("执行" + sql + "失败");
		return null;
	}

	private List<?> allocate(Class<?> objClass, ResultSet rs)
			throws SQLException
	{
		List<Object> results = new ArrayList<Object>();
		try
		{
			while (rs.next())
			{
				Object obj = objClass.newInstance();
				Columns columns = ClassUtil.getFieldInformationOfObj(obj);
				for (Column column : columns)
				{
					String columnLabel = column.getName();
					Class<?> columnType = column.getType();
					Object columnValue = null;
					if (String.class.equals(columnType))
					{
						columnValue = rs.getString(columnLabel);
					} else if (int.class.equals(columnType)
							|| Integer.class.equals(columnType))
					{
						columnValue = rs.getInt(columnLabel);
					} else if (float.class.equals(columnType)
							|| Float.class.equals(columnType))
					{
						columnValue = rs.getFloat(columnLabel);
					} else if (double.class.equals(columnType)
							|| Double.class.equals(columnType))
					{
						columnValue = rs.getDouble(columnLabel);
					} else if (byte[].class.equals(columnType))
					{
						columnValue = rs.getBytes(columnLabel);
					} else if (Date.class.equals(columnType))
					{
						columnValue = rs.getDate(columnLabel);
					} else if (Array.class.equals(columnType))
					{
						columnValue = rs.getArray(columnLabel);
					} else if (BigDecimal.class.equals(columnType))
					{
						columnValue = rs.getBigDecimal(columnLabel);
					} else if (Blob.class.equals(columnType))
					{
						columnValue = rs.getBlob(columnLabel);
					} else if (Boolean.class.equals(columnType))
					{
						columnValue = rs.getBoolean(columnLabel);
					} else
					{
						System.err
								.println(columnType.getName() + "不是上述类型，填充失败");
					}
					column.setValue(columnValue);
				}
				System.out.println(ClassUtil.fillFieldInformationOfObj(obj,
						columns));
				results.add(obj);
			}
			return results;
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 自动填充PreparedStatement
	 * 
	 * @param ps
	 * @throws SQLException
	 */
	private void process(PreparedStatement ps) throws SQLException
	{
		System.out.println(values);
		for (int i = 1; i <= values.size(); i++)
		{
			Object value = values.get(i - 1);
			if (String.class.isInstance(value))
			{
				ps.setString(i, (String) value);
			} else if (int.class.isInstance(value)
					|| Integer.class.isInstance(value))
			{
				ps.setInt(i, (Integer) value);
			} else if (float.class.isInstance(value)
					|| Float.class.isInstance(value))
			{
				ps.setFloat(i, (Float) value);
			} else if (double.class.isInstance(value)
					|| Double.class.isInstance(value))
			{
				ps.setDouble(i, (Double) value);
			} else if (byte[].class.isInstance(value))
			{
				ps.setBytes(i, (byte[]) value);
			} else if (Date.class.isInstance(value))
			{
				ps.setDate(i, (Date) value);
			} else if (Array.class.isInstance(value))
			{
				ps.setArray(i, (Array) value);
			} else if (BigDecimal.class.isInstance(value))
			{
				ps.setBigDecimal(i, (BigDecimal) value);
			} else if (InputStream.class.isInstance(value))
			{
				ps.setBinaryStream(i, (InputStream) value);
			} else if (Blob.class.isInstance(value))
			{
				ps.setBlob(i, (Blob) value);
			} else if (Boolean.class.isInstance(value))
			{
				ps.setBoolean(i, (Boolean) value);
			} else if (Timestamp.class.isInstance(value))
			{
				ps.setTimestamp(i, (Timestamp) value);
			} else
			{
				System.err.println(value.getClass().getName() + "不是上述类型，填充失败");
			}
		}
	}
}
