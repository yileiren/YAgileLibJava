package ylr.main;

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
				System.out.println("1、测试网络数据发送。");
				System.out.println("0、退出程序。");
				System.out.print("请输入您的选择：");
				
				byte[] inputBuf = new byte[200];
				int inputCount = System.in.read(inputBuf);
				
				byte[] inputText = new byte[inputCount - 2];
				for(int i = 0;i < inputCount  - 2;i++)
				{
					inputText[i] = inputBuf[i];
				}
				
				String s = new String(inputText);
				int inputNum = Integer.valueOf(s);
				
				if(inputNum == 0)
				{
					System.out.println("程序已退出！");
					break;
				}
				else if(inputNum == 1)
				{
					sendData();
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

	public static void sendData()
	{
		try
		{
			System.out.print("请输入服务器IP：");
			byte[] inputBuf = new byte[2000];
			int inputCount = System.in.read(inputBuf);
			
			byte[] inputText = new byte[inputCount - 2];
			for(int i = 0;i < inputCount  - 2;i++)
			{
				inputText[i] = inputBuf[i];
			}
			String ip = new String(inputText);
			
			System.out.print("请输入服务器监听端口：");
			inputCount = System.in.read(inputBuf);
			
			inputText = new byte[inputCount - 2];
			for(int i = 0;i < inputCount  - 2;i++)
			{
				inputText[i] = inputBuf[i];
			}
			int port = Integer.valueOf(new String(inputText));
			
			System.out.print("请输入您要发送的数据：");
			inputCount = System.in.read(inputBuf);
			
			byte[] sendText = new byte[inputCount - 2];
			for(int i = 0;i < inputCount  - 2;i++)
			{
				sendText[i] = inputBuf[i];
			}
			
			String utf8Text = new String(sendText);
			
			Socket s = new Socket(ip, port);
			if(YConnection.sendData(s, utf8Text.getBytes("GBK"), 20, 1000))
			{
				System.out.println("发送数据成功！");
			}
			else
			{
				System.out.println("发送数据失败！");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
