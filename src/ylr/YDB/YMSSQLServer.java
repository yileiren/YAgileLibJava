package ylr.YDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 微软SQLServer数据库操作类。
 * 
 * @author 董帅 创建时间：2013-06-30 22:17:36
 *
 */
public class YMSSQLServer implements YDataBase
{
	//测试方法。
//	public static void main(String[] args) 
//	{
//		try
//		{
//			YMSSQLServer db = new YMSSQLServer();
//			db.setServerName("192.168.16.70");
//			db.setUserName("sa");
//			db.setUserPassword("dongshuai@617");
//			db.setDatabaseName("test");
//			
//			if(db.connectDataBase())
//			{
//				if(!db.beginTransaction())
//				{
//					System.out.println(db.getLastErrorMessage());
//				}
//				System.out.println("dd");
//				int count = db.executeSqlWithOutData("INSERT INTO tb_test (name) VALUES ('ddd')");
//				if(count > 0)
//				{
//					db.commitTransaction();
//					System.out.println(count);
//					db.executeSqlWithOutData("INSERT INTO tb_test (name) VALUES ('ddd2')");
//					ResultSet data = db.executeSqlReturnData("SELECT * FROM tb_test");
//					while(data.next())
//					{
//						System.out.println("" + data.getInt("id") + "|" + data.getString("name"));
//					}
//				}
//				else
//				{
//					System.out.println(db.getLastErrorMessage());
//				}
//				db.disconnectDataBase();
//			}
//			else
//			{
//				System.out.println("connectError!" + db.getLastErrorMessage());
//			}
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//	}
	
	/**
	 * 数据库连接。
	 */
	private Connection _conn = null;
	
	/**
	 * 最后一次出错信息。
	 */
	private String _lastErrorMessage = "";
	
	/**
	 * 服务器名称，默认是""。
	 */
	private String _serverName = "";
	
	/**
	 * 数据库服务端口号。
	 */
	private int _port = 1433;
	
	/**
	 * 登录用户名。
	 */
	private String _userName = "";

	/**
	 * 登录密码。
	 */
	private String _userPassword = "";
	
	/***
	 * 要访问的数据库名称。
	 */
	private String _databaseName = "";
	
	/**
	 * 默认构造函数。
	 */
	YMSSQLServer()
	{
		
	}

	/**
	 * 获取数据库类型。
	 * 
	 * @return 数据库类型。
	 */
	public YDataBaseType getDatabaseType()
	{
		return YDataBaseType.MSSQL;
	}

	/**
	 * 获取最后一次错误信息。
	 * 
	 * @return 最后一次错误信息。
	 */
	public String getLastErrorMessage()
	{
		return this._lastErrorMessage;
	}

	/**
	 * 获取服务器名称。
	 * 
	 * @return 服务器名称。
	 */
	public String getServerName()
	{
		return _serverName;
	}

	/**
	 * 设置服务器名称。
	 * 
	 * @param _serverName 服务器名称。
	 */
	public void setServerName(String _serverName)
	{
		this._serverName = _serverName;
	}

	/**
	 * 获取服务器端口号。
	 * 
	 * @return 服务器端口号。
	 */
	public int getPort()
	{
		return _port;
	}

	/**
	 * 设置服务器端口号。
	 * 
	 * @param _port 服务器端口号。
	 */
	public void setPort(int _port)
	{
		this._port = _port;
	}

	/**
	 * 获取用户名。
	 * 
	 * @return 用户名。
	 */
	public String getUserName()
	{
		return _userName;
	}

	/**
	 * 设置用户名。
	 * 
	 * @param _userName 用户名。
	 */
	public void setUserName(String _userName)
	{
		this._userName = _userName;
	}

	/**
	 * 获取用户密码。
	 * 
	 * @return 用户密码。
	 */
	public String getUserPassword()
	{
		return _userPassword;
	}

	/**
	 * 设置用户密码。
	 * 
	 * @param _userPassword 用户密码。
	 */
	public void setUserPassword(String _userPassword)
	{
		this._userPassword = _userPassword;
	}

	/**
	 * 获取数据库名称。
	 * 
	 * @return 数据库名称。
	 */
	public String getDatabaseName()
	{
		return _databaseName;
	}

	/**
	 * 设置数据库名称。
	 * 
	 * @param _databaseName 数据库名称。
	 */
	public void setDatabaseName(String _databaseName)
	{
		this._databaseName = _databaseName;
	}

	/**
	 * 连接数据库。
	 * 
	 * @return 成功返回true，否则返回false。
	 */
	public boolean connectDataBase()
	{
		boolean retValue = false;
		try
		{
			if(null == this._conn)
			{
				String url="jdbc:sqlserver://" + this._serverName + ":" + String.valueOf(this._port) + ";DatabaseName=" + this._databaseName;
				
				//加载驱动
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				//连接数据库
				this._conn = DriverManager.getConnection(url, this._userName, this._userPassword);
			}
			retValue = true;
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "连接数据库失败！||" + ex.getMessage();
		}
		
		return retValue;
	}

	/**
	 * 断开数据库连接。
	 * 
	 * @return 成功返回true，否则返回false。
	 */
	public boolean disconnectDataBase()
	{
		boolean retValue = false;
		try
		{
			if(null != this._conn)
			{
				this._conn.close();
				this._conn = null;
			}
			retValue = true;
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "连接数据库失败！||" + ex.getMessage();
		}
		return retValue;
	}

	/**
	 * 开启事务。
	 * 
	 * @return 成功返回true，否则返回false。
	 */
	public boolean beginTransaction()
	{
		boolean retValue = false;
		try
		{
			this._conn.setAutoCommit(false);
			retValue = true;
		}
		catch (SQLException ex)
		{
			this._lastErrorMessage = "连接数据库失败！||" + ex.getMessage();
		}
		return retValue;
	}

	/**
     * 提交数据库事务。
     * @return 成功返回true，否则返回false。
     */
	public boolean commitTransaction()
	{
		boolean retValue = false;
		try
		{
			this._conn.commit();
			this._conn.setAutoCommit(true);
			retValue = true;
		}
		catch (SQLException ex)
		{
			this._lastErrorMessage = "连接数据库失败！||" + ex.getMessage();
		}
		return retValue;
	}

	/**
     * 回滚数据库事务。
     * @return 成功返回true，否则返回false。
     */
	public boolean rollbackTransaction()
	{
		boolean retValue = false;
		try
		{
			this._conn.rollback();
			this._conn.setAutoCommit(true);
			retValue = true;
		}
		catch (SQLException ex)
		{
			this._lastErrorMessage = "连接数据库失败！||" + ex.getMessage();
		}
		return retValue;
	}

	/**
     * 执行带有数据返回的sql。
     * @param sql 要执行的sql语句。
     * @return 成功返回数据集，否则返回null。
     */
	public ResultSet executeSqlReturnData(String sql)
	{
		ResultSet retValue = null;
		Statement sta = null;
		try
		{
			sta = this._conn.createStatement();
			retValue = sta.executeQuery(sql);
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "执行sql语句出错！||" + ex.getMessage();
		}
		finally
		{
			if(null != sta)
			{
				try
				{
					sta.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return retValue;
	}
	
	/**
     * 执行带有数据返回的sql。
     * @param sql 要执行的sql语句。
     * @param p sql语句使用的参数。
     * @return 成功返回数据集，否则返回null。
     */
	public ResultSet executeSqlReturnData(String sql, YSqlParameters p)
	{
		ResultSet retValue = null;
		PreparedStatement ps = null;
		try
		{
			ps = this._conn.prepareStatement(sql);
			
			//设置参数。
			for(int i = 0;i < p.getParametersCount();i++)
			{
				if(Integer.class == p.getValue(i).getClass())
				{
					ps.setInt(p.getIndex(i), (Integer) p.getValue(i));
				}
				else if(Long.class == p.getValue(i).getClass())
				{
					ps.setLong(p.getIndex(i), (Long) p.getValue(i));
				}
				else if(Date.class == p.getValue(i).getClass())
				{
					ps.setDate(p.getIndex(i), (Date) p.getValue(i));
				}
				else
				{
					ps.setString(p.getIndex(i), (String) p.getValue(i));
				}
			}
			
			retValue = ps.executeQuery();
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "执行sql语句出错！||" + ex.getMessage();
		}
		finally
		{
			if(null != ps)
			{
				try
				{
					ps.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return retValue;
	}

	/**
     * 执行没有数据返回的sql。
     * @param sql 要执行的sql语句。
     * @return 成功返回数据响应行数，失败返回-1。
     */
	public int executeSqlWithOutData(String sql)
	{
		int retValue = -1;
		Statement sta = null;
		try
		{
			sta = this._conn.createStatement();
			retValue = sta.executeUpdate(sql);
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "执行sql语句出错！||" + ex.getMessage();
		}
		finally
		{
			if(null != sta)
			{
				try
				{
					sta.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return retValue;
	}

	/**
     * 执行没有数据返回的sql。
     * @param sql 要执行的sql语句。
     * @param p sql语句使用的参数。
     * @return 成功返回数据响应行数，失败返回-1。
     */
	public int executeSqlWithOutData(String sql, YSqlParameters p)
	{
		int retValue = -1;
		PreparedStatement ps = null;
		try
		{
			ps = this._conn.prepareStatement(sql);
			
			//设置参数。
			for(int i = 0;i < p.getParametersCount();i++)
			{
				if(Integer.class == p.getValue(i).getClass())
				{
					ps.setInt(p.getIndex(i), (Integer) p.getValue(i));
				}
				else if(Long.class == p.getValue(i).getClass())
				{
					ps.setLong(p.getIndex(i), (Long) p.getValue(i));
				}
				else if(Date.class == p.getValue(i).getClass())
				{
					ps.setDate(p.getIndex(i), (Date) p.getValue(i));
				}
				else
				{
					ps.setString(p.getIndex(i), (String) p.getValue(i));
				}
			}
			
			retValue = ps.executeUpdate();
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "执行sql语句出错！||" + ex.getMessage();
		}
		finally
		{
			if(null != ps)
			{
				try
				{
					ps.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return retValue;
	}
}
