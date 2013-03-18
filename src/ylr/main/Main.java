package ylr.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import ylr.YNetwork.YConnection;

public class Main {

	public static void main(String[] args) 
	{
		while(true)
		{
			try
			{
				System.out.println("测试程序启动，请选择您要进行的操作：");
				System.out.println("1、测试网络数据发送（使用GBK编码）。");
				System.out.println("2、测试网络数据发送（使用UTF-8编码）。");
				System.out.println("3、测试网络数据接收（使用GBK编码）。");
				System.out.println("0、退出程序。");
				System.out.print("请输入您的选择：");
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
				String inputText = reader.readLine();
				
				int inputNum = Integer.valueOf(inputText);
				
				if(inputNum == 0)
				{
					System.out.println("程序已退出！");
					break;
				}
				else if(inputNum == 1)
				{
					sendData("GBK");
				}
				else if(inputNum == 2)
				{
					sendData("UTF-8");
				}
				else if(inputNum == 3)
				{
					rcvData("GBK");
				}
				else
				{
					System.out.println("你输入的序号有误，成重新输入！");
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 数据发送测试程序。
	 * 
	 * @param decode 网络数据流中的编码集。
	 */
	public static void sendData(String decode)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
			
			System.out.print("请输入服务器IP：");
			String ip = reader.readLine();
			
			System.out.print("请输入服务器监听端口：");
			int port = Integer.valueOf(reader.readLine());
			
			String sendText = "1234567890这是个测试数据abcdefghijklmnopqrstuvwxyz0987654321这是个测试数据！ABCDEFGHIGKLMNOPQRSTUVWXYZ...end:";
			
			System.out.println("您要发送的数据是：" + sendText);
			
			Socket s = new Socket(ip, port);
			if(YConnection.sendString(s, sendText,20,1000,decode))
			{
				System.out.println("发送数据成功！");
			}
			else
			{
				System.out.println("发送数据失败！");
			}
			s.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void rcvData(String decode)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
			
			System.out.print("请输入服务器监听端口：");
			int port = Integer.valueOf(reader.readLine());
			
			ServerSocket server = new ServerSocket(port);
			
			Socket socket = server.accept();
			
			String data = YConnection.receiveString(socket, 20, 1000, "GBK");
			
			System.out.println(data);
			
			socket.close();
			server.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
