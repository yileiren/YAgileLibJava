package ylr.YNetwork;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 网络连接处理类，用来处理连接状态和数据传输。
 * 数据传输过程中数据包格式为：
 * 数据包字段说明:     |状态字|数据包总个数|当前数据包序号|数据包中数据长度|数据|结束|
 * 各个字段长度(字节): | 1    |   4       |     4       |     4         | x  | 1 |
 * 
 * 详细说明：
 * 状态字：标识该数据包的类型，状态字类型为StatusWord中的。
 * 数据包总个数：本次发送的数据被分成的数据包总数，数据类型为int。
 * 当前数据包序号：本次发送的数据包在总个数中的序号，数据类型为int。
 * 数据包中数据长度：本数据包中的数据长度，数据类型为int。
 * 数据：数据包中的数据，长度为数据包中数据长度约定的长度。
 * 结束：使用空字符（'\0'）做为结束标识。
 * 
 * @author 董帅 创建时间：2013-2-20 11:16:07
 * */
public class YConnection
{
	/**
	 * 网络传输包状态字。
	 * @author 董帅 创建时间：2013-2-20 14:40:38
	 *
	 */
	public static final class StatusWord
	{
		public static final int EndFlag = 0x00;       /**< 数据包结束位。 */
		
		public static final byte Yes = 0x01;           /**< 请求成功。 */
		public static final byte No = 0x02;            /**< 请求失败。 */
		public static final byte Go = 0x03;            /**< 继续。 */
		public static final byte End = 0x04;           /**< 结束。 */
		public static final byte Error = 0x05;         /**< 出错。 */
		
		public static final byte SendData = 0x21;      /**< 请求发送数据。 */

		public static final byte DataPackage = (byte) 0xFF;   /**< 数据包，不含有状态。 */
	};
	
	/**
	 * 默认构造函数。
	 */
	YConnection()
	{
		
	}
	
	/**
	 * 使用指定的套接字发送二进制数据。
	 * 
	 * 数据发送握手协议：
	 * 1、请求发送数据。
	 * 2、等待服务器响应。
	 * 3、如果允许发送则逐个包发送数据，每个包发送结束后都要获取服务器响应结果。
	 * 4、所有数据发送完毕后发送结束协议。
	 * 5、退出。
	 * 
	 * @author 董帅 创建时间：2013-2-20 14:36:03
	 * 
	 * @param s 指定的套接字。
	 * @param data 要发送的数据。
	 * @param packageLength 被每个数据包最大长度。
	 * @param time 超时时间。
	 * 
	 * @return 成功返回true，否则返回false。
	 * 
	 * @throws Exception 程序未处理异常。
	 */
	public static boolean sendData(Socket s,byte[] data,int packageLength,int time) throws Exception
	{
		boolean retValue = true;
		
		try
		{
			s.setSoTimeout(time); //设置超时时间。
			
			OutputStream socketSndStream = s.getOutputStream(); //获取发送数据流
			InputStream socketRcvStream = s.getInputStream(); //获取接收数据流
			
			//发送数据请求
			byte[] sendDataReq = new byte[14];
			sendDataReq[0] = YConnection.StatusWord.SendData; //设置状态字
			
			//设置请求数据包总数
			sendDataReq[4] = (byte)((1 >> 24) & 0xFF);
			sendDataReq[3] = (byte)((1 >> 16) & 0xFF);
			sendDataReq[2] = (byte)((1 >> 8) & 0xFF);
			sendDataReq[1] = (byte)(1 & 0xFF);
			
			//设置请求数据包序号
			sendDataReq[8] = (byte)((1 >> 24) & 0xFF);
			sendDataReq[7] = (byte)((1 >> 16) & 0xFF);
			sendDataReq[6] = (byte)((1 >> 8) & 0xFF);
			sendDataReq[5] = (byte)(1 & 0xFF);
			
			//设置数据长度
			sendDataReq[12] = (byte)((0 >> 24) & 0xFF);
			sendDataReq[11] = (byte)((0 >> 16) & 0xFF);
			sendDataReq[10] = (byte)((0 >> 8) & 0xFF);
			sendDataReq[9] = (byte)(0 & 0xFF);
			
			//设置结束位
			sendDataReq[13] = YConnection.StatusWord.EndFlag;
			
			//发送
			socketSndStream.write(sendDataReq);
			
			//接收响应
			byte[] sendDataRes = new byte[20];
			int r = socketRcvStream.read(sendDataRes);
			
			if(r > 0)
			{
				//服务器允许发送数据
				if(YConnection.StatusWord.Yes == sendDataRes[0])
				{
					byte dataBuf[] = new byte[packageLength + 14];//数据发送缓冲区
					
					//计算数据包总数
					int packageCount = data.length / packageLength;
					if(data.length % packageLength > 0)
					{
						packageCount++;
					}
					
					dataBuf[4] = (byte)((packageCount >> 24) & 0xFF);
					dataBuf[3] = (byte)((packageCount >> 16) & 0xFF);
					dataBuf[2] = (byte)((packageCount >> 8) & 0xFF);
					dataBuf[4] = (byte)(packageCount & 0xFF);
					
					dataBuf[packageLength + 13] = YConnection.StatusWord.EndFlag;
				
					//发送数据
					int sendLength = 0; //发送数据计数
					while(data.length - sendLength > 0)
					{
						dataBuf[0] = (byte)YConnection.StatusWord.DataPackage; //设置状态字
						//当前数据包序号
						int packageOrder = sendLength / packageLength + 1;
						dataBuf[8] = (byte)((packageOrder >> 24) & 0xFF);
						dataBuf[7] = (byte)((packageOrder >> 16) & 0xFF);
						dataBuf[6] = (byte)((packageOrder >> 8) & 0xFF);
						dataBuf[5] = (byte)(packageOrder & 0xFF);
						
						//计算当前数据包中数据长度
						int l = 0;
						if(data.length - sendLength >= packageLength)
						{
							l = packageLength;
						}
						else
						{
							l = data.length - sendLength;
							
						}
						
						dataBuf[12] = (byte)((l >> 24) & 0xFF);
						dataBuf[11] = (byte)((l >> 16) & 0xFF);
						dataBuf[10] = (byte)((l >> 8) & 0xFF);
						dataBuf[9] = (byte)(l & 0xFF);
						
						//拷贝数据到dataBuf
						for(int i = 0;i < l;i++)
						{
							dataBuf[13 + i] = data[sendLength + i];
							
						}
						dataBuf[l + 13] = YConnection.StatusWord.EndFlag;
						sendLength += l;
						
						//发送
						socketSndStream.write(dataBuf, 0, l + 14);
						
						//接收服务器响应
						r = socketRcvStream.read(dataBuf);
						if(r > 0)
						{
							if(YConnection.StatusWord.Go != dataBuf[0])
							{
								//对方不允许继续发送数据
								retValue = false;
								break;
							}
						}
						else
						{
							//发送数据包后接收对方回复失败
							retValue = false;
							break;
						}
					}
				}
				else
				{
					//服务器不允许发送数据
					retValue = false;
				}
			}
			else
			{
				//接收发送请求响应数据失败。
				retValue = false;
			}
			
			//数据传输结束
			byte[] sendEndDataReq = new byte[14];
			sendEndDataReq[0] = YConnection.StatusWord.End; //设置状态字
			
			//设置请求数据包总数
			sendEndDataReq[4] = (byte)((1 >> 24) & 0xFF);
			sendEndDataReq[3] = (byte)((1 >> 16) & 0xFF);
			sendEndDataReq[2] = (byte)((1 >> 8) & 0xFF);
			sendEndDataReq[1] = (byte)(1 & 0xFF);
			
			//设置请求数据包序号
			sendEndDataReq[8] = (byte)((1 >> 24) & 0xFF);
			sendEndDataReq[7] = (byte)((1 >> 16) & 0xFF);
			sendEndDataReq[6] = (byte)((1 >> 8) & 0xFF);
			sendEndDataReq[5] = (byte)(1 & 0xFF);
			
			//设置数据长度
			sendEndDataReq[12] = (byte)((0 >> 24) & 0xFF);
			sendEndDataReq[11] = (byte)((0 >> 16) & 0xFF);
			sendEndDataReq[10] = (byte)((0 >> 8) & 0xFF);
			sendEndDataReq[9] = (byte)(0 & 0xFF);
			
			//设置结束位
			sendEndDataReq[13] = YConnection.StatusWord.EndFlag;
			
			socketSndStream.write(sendEndDataReq);
		}
		catch(Exception ex)
		{
			retValue = false;
			throw ex;
		}
		
		return retValue;
	}
	
	/**
	 * 使用指定的套接字发送文本。
	 * @author 董帅 创建时间：2013-2-21 10:45:44
	 * 
	 * @param s 指定的套接字。
	 * @param text 要发送的文本。
	 * @param packageLength 被每个数据包最大长度。
	 * @param time 超时时间。
	 * @param decode 指定网络数据流中的编码集。
	 * 
	 * @return 成功返回true，否则返回false。
	 * 
	 * @throws Exception 抛出的未知异常。
	 */
	public static boolean sendString(Socket s,String text,int packageLength,int time,String decode) throws Exception
	{
		try
		{
			//将字符串转换成二进制
			byte[] data = new byte[text.getBytes(decode).length + 1];
			for(int i = 0;i < text.getBytes(decode).length;i++)
			{
				data[i] = text.getBytes(decode)[i];
			}
			data[text.getBytes(decode).length] = 0x00;
			
			return YConnection.sendData(s, data,packageLength,time);
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}
	
	/**
	 * 接收二进制数据。
	 * 
	 * @author 董帅 创建时间：2013-2-25 11:18:36
	 * @param s 接收数据使用的套接字。
	 * @param bufLength 接收数据缓冲区长度。
	 * @param time 超时时间
	 * @return 成功返回接收到得数据，否则返回null。
	 * @throws Exception 抛出的未处理异常。
	 */
	public static byte[] recaiveData(Socket s,int bufLength,int time) throws Exception
	{
		byte[] retValue = null;
		try
		{
			s.setSoTimeout(time); //设置超时时间。
			
			OutputStream socketSndStream = s.getOutputStream(); //获取发送数据流
			InputStream socketRcvStream = s.getInputStream(); //获取接收数据流
			
			byte[] rcvBuf = new byte[bufLength + 14]; //接收数据缓冲区
			byte[] resData = new byte[14]; //响应数据
			
			int r = socketRcvStream.read(rcvBuf);
			if(r > 0)
			{
				if(YConnection.StatusWord.SendData == rcvBuf[0])
				{
					resData[0] = YConnection.StatusWord.Yes;
					
					//设置请求数据包总数
					resData[4] = (byte)((1 >> 24) & 0xFF);
					resData[3] = (byte)((1 >> 16) & 0xFF);
					resData[2] = (byte)((1 >> 8) & 0xFF);
					resData[1] = (byte)(1 & 0xFF);
					
					//设置请求数据包序号
					resData[8] = (byte)((1 >> 24) & 0xFF);
					resData[7] = (byte)((1 >> 16) & 0xFF);
					resData[6] = (byte)((1 >> 8) & 0xFF);
					resData[5] = (byte)(1 & 0xFF);
					
					//设置数据长度
					resData[12] = (byte)((0 >> 24) & 0xFF);
					resData[11] = (byte)((0 >> 16) & 0xFF);
					resData[10] = (byte)((0 >> 8) & 0xFF);
					resData[9] = (byte)(0 & 0xFF);
					
					//设置结束位
					resData[13] = YConnection.StatusWord.EndFlag;
					
					//允许传输
					socketSndStream.write(resData);
					
					//接收数据
					retValue = new byte[0];
					while(true)
					{
						r = socketRcvStream.read(rcvBuf);
						
						if(r > 0)
						{
							if(YConnection.StatusWord.DataPackage == rcvBuf[0])
							{
								int packageLength = 0; //数据包中数据长度
								
								byte bLoop;  
								for ( int i = 0; i < 4 ; i++) 
								{  
									bLoop = rcvBuf[9 + i];  
									packageLength += (bLoop & 0xFF) << (8 * i);  
								}
								
								System.out.println(packageLength);
								
								if(packageLength > 0)
								{
									byte[] d = new byte[retValue.length + packageLength];
									for(int i = 0;i < retValue.length;i++)
									{
										d[i] = retValue[i];
									}
									
									for(int i = 0;i < packageLength;i++)
									{
										d[i + retValue.length] = rcvBuf[13 + i];
									}
									retValue = d;
								}

								//继续接收数据
								resData[0] = YConnection.StatusWord.Go;
								socketSndStream.write(resData);
							}
							else if(YConnection.StatusWord.End == rcvBuf[0])
							{
								//接收数据完成。
								break;
							}
							else
							{
								//接收到得数据不是数据包，发送出错响应并停止接收。
								retValue = null;
								resData[0] = YConnection.StatusWord.Error;
								socketSndStream.write(resData);
								break;
							}
						}
						else
						{
							//接收数据失败
							break;
						}
					}
					
				}//判断请求位为发送数据请求
			}//接收请求数据
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return retValue;
	}
	
	/**
	 * 接收字符串。
	 * @param s 使用的额套接字。
	 * @param bufLength 接收缓冲区长度
	 * @param time 超时时间。
	 * @param decode 使用的编码集
	 * @return 成功返回接收到得字符串，否则返回null。
	 * @throws Exception 抛出的未处理异常。
	 */
	public static String receiveString(Socket s,int bufLength,int time,String decode) throws Exception
	{
		String retValue = null; //返回数据
		
		try
		{
			byte[] data = YConnection.recaiveData(s, bufLength, time);
			
			if(data != null && data.length > 0)
			{
				retValue = new String(data,decode);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return retValue;
	}
}
