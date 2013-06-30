package ylr.YDB;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MySQL数据库操作类。
 * @author 董帅 创建时间：2013年5月21日 11时02分59秒
 *
 */
public class YMySQL implements YDatabase
{
	//测试方法。
//	public static void main(String[] args) 
//	{
//		try
//		{
//			YMySQLDatabase db = new YMySQLDatabase();
//			db.setServerName("192.168.56.90");
//			db.setUserName("yileiren");
//			db.setUserPassword("ylr");
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
	private int _port = 3306;
	
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
	YMySQL()
	{
		
	}
	
	/**
	 * 获取数据库类型。
	 * @return 数据库类型。
	 */
	public YDatabaseType getDatabaseType()
	{
		return YDatabaseType.MySQL;
	}

	/**
	 * 获取最后一次错误信息。
	 * @return 错误信息。
	 */
	public String getLastErrorMessage()
	{
		return this._lastErrorMessage;
	}

	/**
	 * 获取服务器名称。
	 * @return 服务器名称。
	 */
	public String getServerName()
	{
		return _serverName;
	}

	/**
	 * 设置服务器名称。
	 * @param serverName 服务器名称。
	 */
	public void setServerName(String serverName)
	{
		this._serverName = serverName;
	}

	/**
	 * 获取数据库端口。
	 * @return 数据库端口。
	 */
	public int getPort()
	{
		return _port;
	}

	/**
	 * 设置数据库端口。
	 * @param port 数据库端口。
	 */
	public void setPort(int port)
	{
		this._port = port;
	}

	/**
	 * 获取登录用户名。
	 * @return 登录用户名。
	 */
	public String getUserName()
	{
		return _userName;
	}

	/**
	 * 设置登录用户名。
	 * @param userName 登录用户名。
	 */
	public void setUserName(String userName)
	{
		this._userName = userName;
	}

	/**
	 * 获取登录用户密码。
	 * @return 登录用户密码。
	 */
	public String getUserPassword()
	{
		return _userPassword;
	}

	/**
	 * 设置登录用户密码。
	 * @param userPassword 登录用户密码。
	 */
	public void setUserPassword(String userPassword)
	{
		this._userPassword = userPassword;
	}

	/**
	 * 获取数据库名称。
	 * @return 数据库名称。
	 */
	public String getDatabaseName()
	{
		return _databaseName;
	}

	/**
	 * 设置数据库名称。
	 * @param databaseName 数据库名称。
	 */
	public void setDatabaseName(String databaseName)
	{
		this._databaseName = databaseName;
	}

	/**
	 * 连接数据库。
	 * @return 成功返回true，否则返回false。
	 */
	public boolean connectDataBase()
	{
		boolean retValue = false;
		try
		{
			if(null == this._conn)
			{
				String url="jdbc:mysql://" + this._serverName + ":" + String.valueOf(this._port) + "/" + this._databaseName;
				
				//加载驱动
				Class.forName("com.mysql.jdbc.Driver");
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
     * 开启数据库事务。
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
		try
		{
			Statement sta = this._conn.createStatement();
			retValue = sta.executeQuery(sql);
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "执行sql语句出错！||" + ex.getMessage();
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
		try
		{
			Statement sta = this._conn.createStatement();
			retValue = sta.executeUpdate(sql);
		}
		catch(Exception ex)
		{
			this._lastErrorMessage = "执行sql语句出错！||" + ex.getMessage();
		}
		return retValue;
	}

}
