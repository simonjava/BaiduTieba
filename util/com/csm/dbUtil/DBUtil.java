package com.csm.dbUtil;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class DBUtil {
	private DBUtil() {
	}

	public static int count(Class<?> objClass) {
		return count(objClass, new String[] {}, new Object[] {});
	}

	/**
	 * @param objClass
	 *            被查询的实体类
	 * @param keysColumn
	 *            where关键字段
	 * @param values
	 *            where关键字段的值
	 * @return 这个查询的结果集的数目
	 */
	public static int count(Class<?> objClass, String[] keysColumn,
			Object[] values) {
		String tableName = getTableName(objClass);
		Columns columns = new Columns(keysColumn, values);
		if (columns.isEmpty())
			System.out.println("没有where子句,会查询所有数目");
		if (!ClassUtil.getCLassFieldChecker(objClass, columns).check()) {
			System.err.println("操作count中类型检查不匹配");
			return -1;
		}
		StringBuilder sqlBuilder = new StringBuilder("select count(*) from ");
		sqlBuilder.append(tableName);
		List<String> columnsName = columns.getColumnsName();
		if (columnsName.size() != 0) {
			sqlBuilder.append(" where ");
			for (int i = 0; i < columnsName.size(); i++) {
				sqlBuilder.append(columnsName.get(i)).append("=?");
				if (i != columnsName.size() - 1)
					sqlBuilder.append(" and ");
			}
		}
		String sql = sqlBuilder.toString();
		return new ProcessStatement(sql, columns.getColumnsValue())
				.startCount();
	}

	public static Object unique(Class<?> objClass, String keyColumn,
			Object value) {
		return unique(objClass, new String[] { keyColumn },
				new Object[] { value });
	}

	/**
	 * @param objClass
	 * @param keysColumn
	 * @param values
	 * @return 返回唯一的结果，客户端需要自行保证查询结果的唯一性，否则返回null
	 */
	public static Object unique(Class<?> objClass, String[] keysColumn,
			Object[] values) {
		List<?> results = select(objClass, keysColumn, values, null, -1, 0);
		if (results.size() == 1)
			return results.get(0);
		System.err.println("查找的对象并不是唯一");
		return null;
	}

	public static List<?> select(Class<?> objClass) {
		return select(objClass, new String[] {}, new Object[] {}, null, -1, 0);
	}

	public static List<?> select(Class<?> objClass, String[] keysColumn,
			Object[] values) {
		return select(objClass, keysColumn, values, null, -1, 0);
	}

	/**
	 * @param objClass
	 *            被查询的实体类
	 * @param keysColumn
	 *            where子句的字段名
	 * @param values
	 *            where子句的字段值
	 * @param orderClause
	 *            order子句，例如order by id desc
	 * @param start
	 *            开始的索引，为-1则无limit子句
	 * @param size
	 *            每次搜索的尺寸，当start为-1时无用
	 * @return 查询得到的结果集合
	 */
	public static List<?> select(Class<?> objClass, String[] keysColumn,
			Object[] values, String orderClause, int start, int size) {
		// select * from name where type = ? limit ?,?
		String tableName = getTableName(objClass);
		Columns columns = new Columns(keysColumn, values);
		if (columns.isEmpty())
			System.out.println("select时未指定field和对应的value,没有where子句,会查询所有");
		if (!ClassUtil.getCLassFieldChecker(objClass, columns).check()) {
			System.err.println("操作select中类型检查不匹配");
			return null;
		}
		StringBuilder sqlBuilder = new StringBuilder("select * from ");
		sqlBuilder.append(tableName);
		List<String> columnsName = columns.getColumnsName();
		if (columnsName.size() != 0) {
			sqlBuilder.append(" where ");
			for (int i = 0; i < columnsName.size(); i++) {
				sqlBuilder.append(columnsName.get(i)).append("=?");
				if (i != columnsName.size() - 1)
					sqlBuilder.append(" and ");
			}
		}
		if (orderClause != null) {
			sqlBuilder.append(" ").append(orderClause);
		}
		sqlBuilder.append(creatLimit(start, size));
		String sql = sqlBuilder.toString();
		return new ProcessStatement(sql, columns.getColumnsValue())
				.startQuery(objClass);
	}

	public static String delete(Class<?> objClass, String keyColumn,
			Object value) {
		return delete(objClass, new String[] { keyColumn },
				new Object[] { value });
	}

	/**
	 * @param objClass
	 *            删除的实体类
	 * @param keysColumn
	 *            where筛选字段
	 * @param values
	 *            where筛选字段对应的值
	 * @return
	 */
	public static String delete(Class<?> objClass, String[] keysColumn,
			Object[] values) {
		// delete from information where user_id = '222';
		String tableName = getTableName(objClass);
		Columns columns = new Columns(keysColumn, values);
		if (columns.isEmpty())
			return "delete时未指定field和对应的value，操作危险，return";
		if (!ClassUtil.getCLassFieldChecker(objClass, columns).check())
			return "操作detele中类型检查不匹配";
		StringBuilder sqlBuilder = new StringBuilder("delete from ");
		sqlBuilder.append(tableName);
		List<String> columnsName = columns.getColumnsName();
		if (columnsName.size() != 0) {
			sqlBuilder.append(" where ");
			for (int i = 0; i < columnsName.size(); i++) {
				sqlBuilder.append(columnsName.get(i)).append("=?");
				if (i != columnsName.size() - 1)
					sqlBuilder.append(" and ");
			}
		}
		String sql = sqlBuilder.toString();
		return new ProcessStatement(sql, columns.getColumnsValue())
				.startUpdate();
	}

	/**
	 * @param obj
	 * @param keyColumn
	 *            这里注意客户端程序员使用时，如果没有参数传入，不能简单的使用null，要用空数组，否则，null会被加入数组，会出错
	 * @return
	 */
	public static String update(Object obj, String keyColumn) {
		return update(obj, new String[] { keyColumn }, new String[] {});
	}

	/**
	 * @param obj
	 *            更新的对象
	 * @param keysColumn
	 *            where筛选字段
	 * @param unupdateColumns
	 *            不更新的字段
	 * @return
	 */
	public static String update(Object obj, String[] keysColumn,
			String... unupdateColumns) {
		// "update information set win=? ,contest_times=?,score=? where user_id =? and game=?";

		Columns columns = ClassUtil.getFieldInformationOfObj(obj);
		String tableName = getTableName(obj.getClass());
		columns.setInvalidColumns(unupdateColumns);
		columns.orderByKeys(keysColumn);
		StringBuilder sqlBuilder = new StringBuilder("update ");
		sqlBuilder.append(tableName).append(" set ");
		List<String> columnsName = columns.getColumnsName();
		int len = columnsName.size() - keysColumn.length;
		for (int i = 0; i < len; i++) {
			String columnName = columnsName.get(i);
			sqlBuilder.append(columnName).append("=?");
			if (i != len - 1)
				sqlBuilder.append(",");
		}
		if (len < columnsName.size()) {
			sqlBuilder.append(" where ");
			// columns剩下几项就是keys，必须按columns里面的顺序来
			for (int i = len; i < columnsName.size(); i++) {
				String columnName = columnsName.get(i);
				sqlBuilder.append(columnName).append("=?");
				if (i != columnsName.size() - 1)
					sqlBuilder.append(" and ");
			}
		}
		String sql = sqlBuilder.toString();
		return new ProcessStatement(sql, columns.getColumnsValue())
				.startUpdate();
	}

	public static String insert(Object obj) {
		return insert(obj, new String[] {});
	}

	/**
	 * @param obj
	 *            插入的对象
	 * @param uninsertedColumns
	 *            不插入的字段
	 * @return
	 */
	public static String insert(Object obj, String... uninsertedColumns) {
		// "insert into information(user_id,win,contest_times,game,score) values(?,?,?,?,?)";

		Columns columns = ClassUtil.getFieldInformationOfObj(obj);
		String tableName = getTableName(obj.getClass());
		columns.setInvalidColumns(uninsertedColumns);

		StringBuilder sqlBuilder = new StringBuilder("insert into ");
		StringBuilder valuesPart = new StringBuilder("values(");
		sqlBuilder.append(tableName).append("(");
		List<String> columnsName = columns.getColumnsName();
		for (int i = 0; i < columnsName.size(); i++) {
			String columnName = columnsName.get(i);
			sqlBuilder.append(columnName);
			valuesPart.append("?");
			if (i != columnsName.size() - 1) {
				sqlBuilder.append(",");
				valuesPart.append(",");
			}
		}
		valuesPart.append(")");
		sqlBuilder.append(") ").append(valuesPart);
		String sql = sqlBuilder.toString();
		return new ProcessStatement(sql, columns.getColumnsValue())
				.startUpdate();
	}

	private static String getTableName(Class<?> objClass) {
		return objClass.getSimpleName().toLowerCase();
	}

	/**
	 * 创建出执行select是的limit子句，如limit 2，20
	 * 
	 * @param start
	 *            起始索引，从0开始，为-1时默认不生成limit限制
	 * @param size
	 *            限制的大小
	 * @return
	 */
	private static String creatLimit(int start, int size) {
		if (start == -1)
			return "";
		StringBuilder limitBuiler = new StringBuilder(" limit ");
		limitBuiler.append(start).append(",").append(size);
		return limitBuiler.toString();
	}

	private static GeneralOperater generalOperater = new GeneralOperater();;

	public static GeneralOperater getGeneralOperater() {
		return generalOperater;
	}

	public static void main(String[] args) throws ClassNotFoundException,
			FileNotFoundException, SQLException {
		// getGeneralOperater().update(sql, values);
		// System.setOut(new PrintStream(new
		// File("/home/csmubuntu/Test1/log.txt")));
		// Information i = new Information("sss", 2, 3, "22aaa", 3);
		// getSqlInformationOfObj(i).describe();
		// System.out.println("getUser_id".matches("get\\w*"));
		// System.out.println(insert(i, new String[]{}));
		// System.out.println(update(i, new String[]{}));
		// System.out.println(delete(i.getClass(), "user_id", "ss"));
		// System.out.println(getGeneralOperater().query("select * from user where id=?",
		// new Object[]{"csm1"}));
		// System.out.println(select(User.class,new String[]{"id"},new
		// Object[]{"csm1"}));
		// System.out.println(count(User.class));
		//
		// System.out.println(DBUtil.getGeneralOperater().update("insert into information(user_id) values(?)",
		// new Object[]{"aaa"}));
		// PreparedStatement ps =
		// DBConnect.getConnection().prepareStatement("select * from user where id=?");
		// ps.setString(1, "csm1");
		// ps.executeQuery();
	}
}
