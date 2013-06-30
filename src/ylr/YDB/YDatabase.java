package ylr.YDB;

import java.sql.ResultSet;

/**
 * 数据库操作通用接口。
 * @author 董帅 创建时间：2013年5月21日 10时45分54秒
 *
 */
public interface YDatabase 
{
	/**
	 * 获取数据库类型。
	 * @return 数据库类型。
	 */
	YDatabaseType getDatabaseType();
	
	/**
	 * 获取最后一次错误信息。
	 * @return 错误信息。
	 */
	String getLastErrorMessage();
	
	/**
	 * 连接数据库。
	 * @return 成功返回true，否则返回false。
	 */
    boolean connectDataBase();
    
    /**
     * 断开数据库连接。
     * @return 成功返回true，否则返回false。
     */
    boolean disconnectDataBase();
    
    /**
     * 开启数据库事务。
     * @return 成功返回true，否则返回false。
     */
    boolean beginTransaction();
    
    /**
     * 提交数据库事务。
     * @return 成功返回true，否则返回false。
     */
    boolean commitTransaction();
    
    /**
     * 回滚数据库事务。
     * @return 成功返回true，否则返回false。
     */
    boolean rollbackTransaction();
    
    /**
     * 执行带有数据返回的sql。
     * @param sql 要执行的sql语句。
     * @return 成功返回数据集，否则返回null。
     */
    ResultSet executeSqlReturnData(String sql);
    
    /**
     * 执行没有数据返回的sql。
     * @param sql 要执行的sql语句。
     * @return 成功返回数据响应行数，失败返回-1。
     */
    int executeSqlWithOutData(String sql);
}
