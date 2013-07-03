package ylr.YDB;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/**
 * 数据库配置文件操作类。
 * 
 * @author 董帅 创建时间：2013-07-03 15:05:42 
 *
 */
public class YDataBaseConfig
{
	/**
	 * 获取配置节点。
	 * 
	 * @param file 配置文件路径。
	 * @param nodeName 配置节点名称。
	 * @return 成功返回配置节点，否则返回null。
	 * @throws Exception 未处理异常。
	 */
	static public Element getConfitNode(String file,String nodeName) throws Exception
	{
		Element node = null;
		try
		{
			// step 1: 获得dom解析器工厂（工作的作用是用于创建具体的解析器）  
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	        // step 2:获得具体的dom解析器  
	        DocumentBuilder db = dbf.newDocumentBuilder();

	        // step3: 解析一个xml文档，获得Document对象（根结点）  
	        Document document = db.parse(new File(file));
	        
	        //获取配置根节点
	        NodeList configList = document.getElementsByTagName("DataBaseConfig");
	        
	        //获取配置节点
	        if(configList.getLength() > 0)
	        {
	        	Element configElement = (Element)configList.item(0);
	        	NodeList nodeList = configElement.getElementsByTagName(nodeName);
	        	if(nodeList.getLength() > 0)
	        	{
	        		node = (Element)nodeList.item(0);
	        	}
	        }
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return node;
	}
	
	/**
	 * 获取数据库类型。
	 * 
	 * @param configNode 配置节点。
	 * @return 数据库类型。
	 */
	static public YDataBaseType getDataBaseType(Element configNode)
	{
		YDataBaseType type = YDataBaseType.None;
		
		try
		{
			//获取数据库类型。
			String attr = configNode.getAttribute("databaseType");
			if(null != attr)
			{
				if("MSSQL".equals(attr))
				{
					type = YDataBaseType.MSSQL;
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return type;
	}
	
	/**
	 * 获取数据库类型。
	 * 
	 * @param file 配置文件路径。
	 * @param nodeName 配置节点名称。
	 * @return 数据库类型。
	 * @throws Exception 未处理异常。
	 */
	static public YDataBaseType getDataBaseType(String file,String nodeName) throws Exception
	{
		YDataBaseType type = YDataBaseType.None;
		
		try
		{
			//获取配置节点。
			Element node = getConfitNode(file,nodeName);
			//获取数据库类型。
			type = getDataBaseType(node);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return type;
	}
	
	/**
	 * 获取微软SQLServer数据库操作对象。
	 * 
	 * @param configNode 配置节点。
	 * @return 成功返回数据库操作对象，失败返回null。
	 */
	static public YMSSQLServer getMSSQLDataBase(Element configNode)
	{
		YMSSQLServer db = null;
		
		try
		{
			//获取数据库类型。
			YDataBaseType type = getDataBaseType(configNode);
			if(YDataBaseType.MSSQL == type)
			{
				db = new YMSSQLServer();
				
				//获取服务器地址。
				NodeList list = configNode.getElementsByTagName("ServerName");
				if(list.getLength() > 0)
				{
					db.setServerName(list.item(0).getTextContent());
				}
				
				//获取端口号
				list = configNode.getElementsByTagName("ServerPort");
				if(list.getLength() > 0)
				{
					String port = list.item(0).getTextContent();
					db.setPort(Integer.valueOf(port));
				}
				
				//获取数据库名称
				list = configNode.getElementsByTagName("DataBaseName");
				if(list.getLength() > 0)
				{
					db.setDatabaseName(list.item(0).getTextContent());
				}
				
				//获取用户名
				list = configNode.getElementsByTagName("UserID");
				if(list.getLength() > 0)
				{
					db.setUserName(list.item(0).getTextContent());
				}
				
				//获取用户密码
				list = configNode.getElementsByTagName("UserPassword");
				if(list.getLength() > 0)
				{
					db.setUserPassword(list.item(0).getTextContent());
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return db;
	}
	
	/**
	 * 获取数据库操作对象。
	 * 
	 * @param file 配置文件路径。
	 * @param nodeName 配置节点名称。
	 * @return 数据库操作对象。
	 * @throws Exception 未处理异常。
	 */
	static public YDataBase getDataBase(String file,String nodeName) throws Exception
	{
		YDataBase db = null;
		
		try
		{
			//获取配置节点
			Element configElement = getConfitNode(file, nodeName);
			
			//获取数据库类型。
			YDataBaseType type = getDataBaseType(configElement);
			if(YDataBaseType.MSSQL == type)
			{
				db = getMSSQLDataBase(configElement);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return db;
	}
	
//	static public void main(String[] args)
//	{
//		try
//		{
//			System.out.println(getDataBaseType("/home/yileiren/Projects/workspace/YAgileLibJava/src/ylr/YDB/DataBaseConfig.xml","SQLServer"));
//			YMSSQLServer s = (YMSSQLServer) getDataBase("/home/yileiren/Projects/workspace/YAgileLibJava/src/ylr/YDB/DataBaseConfig.xml","SQLServer");
//			
//			System.out.println(s.getServerName());
//			System.out.println(s.getDatabaseName());
//			System.out.println(s.getPort());
//			System.out.println(s.getUserName());
//			System.out.println(s.getUserPassword());
//			
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
