package ylr.YFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件读写类。
 * @author 董帅 2013年3月18日 11时00分38秒
 *
 */
public class YConfigFile 
{
	/**
	 * 设置配置文件。
	 * @author 董帅 2013年3月18日 11时20分00秒
	 * 
	 * @param path 配置文件路径。
	 * @param key 配置键名。
	 * @param value 数值
	 * 
	 * @return 成功返回true，否则返回false。
	 * 
	 * @throws IOException 抛出的未处理异常。
	 */
	public static boolean setConfig(String path,String key,String value) throws IOException
	{
		FileInputStream is = null;
		File configFile = new File(path);
		FileOutputStream out = null;
		Properties prop = null;
		
		try 
		{
			if(!(configFile.exists()))
			{
				if(!configFile.createNewFile())
				{
					return false;
				}
			}
		
			prop = new Properties();
			is = new FileInputStream(configFile);
			prop.load(is);
			prop.setProperty(key, value);

			out = new FileOutputStream(configFile);
			prop.store(out, "");
			
		}
		catch (IOException e) 
		{    
			throw e;
		}
		finally
		{
			try 
			{
				if(out != null)
				{
					out.close();
				}
				if(is != null)
				{
					is.close();
				}
			}
			catch (IOException e) 
			{
				throw e;
			}
		}
		
		return true;
	}
	
	/**
	 * 获取配置文件中的配置。
	 * @author 董帅 创建时间：2013年3月18日 12时52分46秒
	 * 
	 * @param path 配置文件路径。
	 * @param key 配置键值。
	 * @param value 获取失败时的默认值。
	 * 
	 * @return 配置的内容，如果获取失败返回默认值。
	 * 
	 * @throws IOException 未处理异常。
	 */
	public static String getConfig(String path,String key,String value) throws IOException
	{
		File  configFile = new File(path);
		FileInputStream in = null;
		
		try
		{
			if(!configFile.exists())
			{
				if(!configFile.createNewFile())
				{
					return value;
				}
			}
			Properties prop = new Properties();
			
			in = new FileInputStream(configFile);
			prop.load(in);
			
			return prop.getProperty(key, value);
		}
		catch(IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if(in != null){
					in.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
