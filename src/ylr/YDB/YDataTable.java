package ylr.YDB;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据表。
 * 
 * @author 董帅 创建时间：2013-07-05 10:57:23
 *
 */
public class YDataTable
{
	/**
	 * 列名。
	 */
	private List<String> columnsName = new ArrayList<String>();
	
	/**
	 * 数据行。
	 */
	private List<YRow> rows = new ArrayList<YRow>();
	
	/**
	 * 默认构造函数。
	 */
	public YDataTable()
	{
		
	}
	
	/**
	 * 构造函数。 
	 * 
	 * @param rs JDBC数据集。
	 * @throws Exception 未处理异常。
	 */
	public YDataTable(ResultSet rs) throws Exception
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i = 0;i < rsmd.getColumnCount();i++)
		{
			//添加列。
			this.addColumn(rsmd.getColumnName(i + 1));
		}
		
		//添加数据
		while(rs.next())
		{
			this.addRow();
			for(int i = 0;i < rsmd.getColumnCount();i++)
			{
				Object d = null;
				switch(rsmd.getColumnType(i + 1))
				{
				case java.sql.Types.BOOLEAN:
					{
						d = rs.getBoolean(i + 1);
						if(rs.wasNull())
							d = null;
						break;
					}
				case java.sql.Types.INTEGER:
					{
						d = rs.getInt(i + 1);
						if(rs.wasNull())
							d = null;
						break;
					}
				case java.sql.Types.CHAR:
				case java.sql.Types.NCHAR:
				case java.sql.Types.VARCHAR:
				case java.sql.Types.NVARCHAR:
					{
						d = rs.getString(i + 1);
						if(rs.wasNull())
							d = null;
						break;
					}
				case java.sql.Types.DOUBLE:
				case java.sql.Types.NUMERIC:
					{
						d = rs.getDouble(i + 1);
						if(rs.wasNull())
							d = null;
						break;
					}
				case java.sql.Types.DATE:
					{
						d = rs.getDate(i + 1);
						if(rs.wasNull())
							d = null;
						break;
					}
				case java.sql.Types.TIME:
					{
						d = rs.getTime(i + 1);
						if(rs.wasNull())
							d = null;
						break;
					}
				default:
					{
						Exception e = new Exception("未定义的数据类型！|数据列“" 
								+ rsmd.getColumnName(i + 1) + "”，数据类型“" +rsmd.getColumnTypeName(i + 1) + "”");
						throw e;
					}
				}
				
				this.setData(this.rowCount() - 1, i, d);
			}
			
		}
	}
	
	/**
	 * 新增列。
	 * 
	 * @param name 列名。
	 */
	public void addColumn(String name)
	{
		this.columnsName.add(name);
		for(int i = 0;i < this.rowCount();i++)
		{
			this.rows.get(i).addCells();
		}
	}
	
	/**
	 * 插入列。
	 * 
	 * @param index 索引。
	 * @param name 名称。
	 */
	public void insertColumn(int index,String name)
	{
		this.columnsName.add(index, name);
		for(int i = 0;i < this.rowCount();i++)
		{
			this.rows.get(i).insertCells(index);
		}
	}
	
	/**
	 * 移除指定位置的列。
	 * 
	 * @param index 位置。
	 */
	public void removeColumn(int index)
	{
		this.columnsName.remove(index);
		for(int i = 0;i < this.rowCount();i++)
		{
			this.rows.get(i).removeCells(index);
		}
	}
	
	/**
	 * 移除指定列。
	 * 
	 * @param name 列名。
	 * @throws Exception 未处理异常。
	 */
	public void removeColumn(String name) throws Exception
	{
		if(this.columnCount() > 0)
		{
			int i = 0;
			for(;i < this.columnCount();i++)
			{
				if(name.equals(this.columnsName.get(i)))
				{
					break;
				}
			}
			
			if(i < this.columnCount())
			{
				this.removeColumn(i);
			}
			else
			{
				Exception e = new Exception("指定列不存在！");
				throw e;
			}
		}
		else
		{
			Exception e = new Exception("指定列不存在！");
			throw e;
		}
	}
	
	/**
	 * 获取指定位置的列名。
	 * 
	 * @param index 索引位置。
	 * @return 列名。
	 */
	public String getColumnName(int index)
	{
		return this.columnsName.get(index);
	}
	
	/**
	 * 列数量。
	 * 
	 * @return 数量。 
	 */
	public int columnCount()
	{
		return this.columnsName.size();
	}
	
	/**
	 * 在表格末尾插入行，所有数据为null。
	 */
	public void addRow()
	{
		YRow r = new YRow(this.columnCount());
		this.rows.add(r);
	}
	
	/**
	 * 在指定位置插入行，所有数据为null。
	 * 
	 * @param index 索引位置。
	 */
	public void insertRow(int index)
	{
		YRow r = new YRow(this.columnCount());
		this.rows.add(index, r);
	}
	
	/**
	 * 设置数据。 
	 * 
	 * @param rowIndex 行索引。
	 * @param columnIndex 列索引。
	 * @param data 数据。
	 */
	public void setData(int rowIndex,int columnIndex,Object data)
	{
		this.rows.get(rowIndex).setData(columnIndex, data);
	}
	
	/**
	 * 设置数据。
	 * 
	 * @param rowIndex 行索引。
	 * @param columnName 列名。
	 * @param data 数据。
	 * @throws Exception 未处理异常。
	 */
	public void setData(int rowIndex,String columnName,Object data) throws Exception
	{
		if(this.columnCount() > 0)
		{
			int i = 0;
			for(;i < this.columnCount();i++)
			{
				if(columnName.equals(this.columnsName.get(i)))
				{
					break;
				}
			}
			
			if(i < this.columnCount())
			{
				this.setData(rowIndex, i, data);
			}
			else
			{
				Exception e = new Exception("指定列不存在！");
				throw e;
			}
		}
		else
		{
			Exception e = new Exception("指定列不存在！");
			throw e;
		}
	}
	
	/**
	 * 获取数据。
	 * 
	 * @param rowIndex 行索引。
	 * @param columnIndex 列索引。
	 * @return 数据。
	 */
	public Object getData(int rowIndex,int columnIndex)
	{
		return this.rows.get(rowIndex).getData(columnIndex);
	}
	
	/**
	 * 获取数据。
	 * 
	 * @param rowIndex 行索引。
	 * @param columnName 列索引。
	 * @return 数据。
	 * @throws Exception 未处理异常。
	 */
	public Object getData(int rowIndex,String columnName) throws Exception
	{
		Object retValue = null;
		if(this.columnCount() > 0)
		{
			int i = 0;
			for(;i < this.columnCount();i++)
			{
				if(columnName.equals(this.columnsName.get(i)))
				{
					break;
				}
			}
			
			if(i < this.columnCount())
			{
				retValue = this.getData(rowIndex, i);
			}
			else
			{
				Exception e = new Exception("指定列不存在！");
				throw e;
			}
		}
		else
		{
			Exception e = new Exception("指定列不存在！");
			throw e;
		}
		
		return retValue;
	}
	
	
	/**
	 * 获取表格行数。
	 * 
	 * @return 行数。
	 */
	public int rowCount()
	{
		return this.rows.size();
	}
	
	/**
	 * 数据行。
	 * 
	 * @author 董帅 创建时间：2013-07-05 11:08:22
	 *
	 */
	private class YRow
	{
		/**
		 * 数据。
		 */
		private List<Object> cells = new ArrayList<Object>();
		
		/**
		 * 构造函数，根据指定数量初始化数据列，所有数据都为null。 
		 * @param count 列数量。
		 */
		public YRow(int count)
		{
			for(int i = 0;i < count;i++)
			{
				this.addCells();
			}
		}
		
		/**
		 * 在末尾新增null数据。
		 */
		public void addCells()
		{
			this.cells.add(null);
		}
		
		/**
		 * 在末尾新增数据。 
		 * 
		 * @param data 数据。
		 */
		public void addCells(Object data)
		{
			this.cells.add(data);
		}
		
		/**
		 * 在指定位置插入数据，数据初始化为null。
		 * 
		 * @param index 指定位置。
		 */
		public void insertCells(int index)
		{
			this.cells.add(index, null);
		}
		
		/**
		 * 在索引位置插入数据。
		 * 
		 * @param 索引值。
		 * @param data 数据。
		 */
		public void insertCells(int index,Object data)
		{
			this.cells.add(index, data);
		}
		
		/**
		 * 移除指定位置的数据。
		 * 
		 * @param index 索引值。
		 */
		public void removeCells(int index)
		{
			this.cells.remove(index);
		}
		
		/**
		 * 列数量。 
		 * 
		 * @return 数量。
		 */
		public int cellsCount()
		{
			return this.cells.size();
		}
		
		/**
		 * 设置数据。
		 * 
		 * @param index 索引。
		 */
		public void setData(int index,Object data)
		{
			this.cells.set(index, data);
		}
		
		/**
		 * 获取指定位置的数据。 
		 * 
		 * @param index 位置。
		 * @return
		 */
		public Object getData(int index)
		{
			return this.cells.get(index);
		}
	}
}
