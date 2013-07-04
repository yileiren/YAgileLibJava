package ylr.YDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SQL语句参数对象，用来构建语句防止sql注入。
 * 
 * @author 董帅 创建时间：2013-07-04 13:34:51
 *
 */
public class YSqlParameters
{
	/**
	 * 参数列表。
	 */
	List<SqlPar> parameters = new ArrayList<>();
	
	
	/**
	 * 默认构造函数。
	 */
	public YSqlParameters()
	{
	}
	
	/**
	 * 添加参数。
	 * 
	 * @param index SQL语句索引位置。
	 * @param value 数据
	 */
	public void addParameter(int index,int value)
	{
		SqlPar p = new SqlPar();
		p.setIndex(index);
		p.setValue(value);
		this.parameters.add(p);
	}
	
	/**
	 * 添加参数。
	 * 
	 * @param index SQL语句索引位置。
	 * @param value 数据
	 */
	public void addParameter(int index,long value)
	{
		SqlPar p = new SqlPar();
		p.setIndex(index);
		p.setValue(value);
		this.parameters.add(p);
	}
	
	/**
	 * 添加参数。
	 * 
	 * @param index SQL语句索引位置。
	 * @param value 数据
	 */
	public void addParameter(int index,double value)
	{
		SqlPar p = new SqlPar();
		p.setIndex(index);
		p.setValue(value);
		this.parameters.add(p);
	}
	
	/**
	 * 添加参数。
	 * 
	 * @param index SQL语句索引位置。
	 * @param value 数据
	 */
	public void addParameter(int index,String value)
	{
		SqlPar p = new SqlPar();
		p.setIndex(index);
		p.setValue(value);
		this.parameters.add(p);
	}
	
	/**
	 * 添加参数。
	 * 
	 * @param index SQL语句索引位置。
	 * @param value 数据
	 */
	public void addParameter(int index,Date value)
	{
		SqlPar p = new SqlPar();
		p.setIndex(index);
		p.setValue(value);
		this.parameters.add(p);
	}
	
	/**
	 * 获取参数个数。
	 * 
	 * @return 参数个数。
	 */
	public int getParametersCount()
	{
		return this.parameters.size();
	}
	
	/**
	 * 获取指定位置参数的索引。
	 * 
	 * @param at 位置,从0开始。
	 * @return 索引，越界返回-1。
	 */
	public int getIndex(int at)
	{
		if(at >= 0 && at < this.parameters.size())
		{
			return this.parameters.get(at).getIndex();
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * 获取指定位置的数据。 
	 * 
	 * @param at 位置，从0开始。
	 * @return 返回指定位置的数据，越界返回null。
	 */
	public Object getValue(int at)
	{
		if(at >= 0 && at < this.parameters.size())
		{
			return this.parameters.get(at).getValue();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 清除参数。
	 */
	public void clear()
	{
		this.parameters.clear();
	}
	
	/**
	 * 参数类。
	 * 
	 * @author 董帅 创建时间：2013-07-04 13:38:37
	 *
	 */
	private class SqlPar
	{
		private int index;
		
		private Object value = null;

		public int getIndex()
		{
			return index;
		}

		public void setIndex(int index)
		{
			this.index = index;
		}

		public Object getValue()
		{
			return value;
		}

		public void setValue(Object value)
		{
			this.value = value;
		}
	}
}
