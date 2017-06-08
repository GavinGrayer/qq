package com.qq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.qq.Message;


public class qqServer implements Serializable {
	private JFrame f = new JFrame("服务器");
	private JPanel p = new JPanel();
	private JTextArea receiver = new JTextArea(5, 16);
	private JTextArea sender = new JTextArea(5, 16);
	private JButton btn = new JButton("发送");
	private Socket socket;
	private ServerSocket server;
	private Map<Integer, Socket> socketMap = new HashMap<Integer, Socket>();
	private List<Integer> portList = new ArrayList<Integer>();

	
	public void init() {

		p.add(receiver);
		p.add(sender);
		p.add(btn);

		f.add(p);

		f.setSize(200, 260);
		f.setVisible(true);
		f.setResizable(false);
		
		System.out.println("服务器已启动！！");
		
	}

	//构造方法封装----------------------
	public qqServer() {

		init();
		// 建立服务器连接-------------------

		try {
			server = new ServerSocket(9999);

			socketThread st = new socketThread();
			st.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	// 启动接收多个客户端连接的线程--------（2）----嵌套（1）------
	class socketThread extends Thread {

		public void run() {

			try {

				while (true) {

					socket = server.accept();

					// 获取客户端的端口号
					InetSocketAddress add = (InetSocketAddress) socket.getRemoteSocketAddress();
					int port = add.getPort();

					portList.add(port);

					System.out.println(portList.toString());
					
					// 把和客户端连接的socket存起来
					socketMap.put(port, socket);
					
					// 接收消息的线程-----(1)---------
					receiveThread rt = new receiveThread(socket);
					rt.start();

					//遍历好友刷新----
					Set set = socketMap.keySet();
					Iterator ite = set.iterator();
					while(ite.hasNext()){
						
						Integer ports = (Integer) ite.next();
						Socket socket = socketMap.get(ports);
						
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						Message message = new Message();						
						message.setFlag(0);
						message.setList(portList);
						oos.writeObject(message);
												
					}

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// 接收消息的线程-----(1)---------
	class receiveThread extends Thread {

		private Socket socket;

		public receiveThread(Socket socket) {

			this.socket = socket;
		}

		public void run() {

			// 接收消息--------
			try {

				// 获取客户端的端口号
				InetSocketAddress add = (InetSocketAddress) socket
						.getRemoteSocketAddress();
				int port = add.getPort();
				//从聊天框中传过来的message拆包获取信息----------------
				
				
				while (true) {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

					Message message = (Message) ois.readObject();
					
					Date date = new Date();
					int year = date.getYear()+1900;
					int mouth = date.getMonth();
					int day = date.getDay();
					int hour = date.getHours();
					int minute = date.getMinutes();
					int second = date.getSeconds();
					
					String s = String.valueOf(year) + "-" + String.valueOf(mouth) + "-" + String.valueOf(day);
					//加工，在消息前加入端口号port，再封装---------------
					receiver.append(s + "    "+ hour + ":" + 
							minute + ":" + second + "\n" + port + "说:" + 
							message.getMsg() + "\n");

					System.out.println("port:" + port);

					System.out.println("传来的消息1："+receiver.getText());
					
					int flag = message.getFlag();
					System.out.println("服务器收到的flag:" + flag);
					
					
					if(flag == 1){//群聊方法-----------------

						Set<Integer> keys = socketMap.keySet();
						Iterator<Integer> ite = keys.iterator();

						
						while(ite.hasNext()){
							
							Integer port2 = ite.next();
							Socket socket = socketMap.get(port2);
						
							ObjectOutputStream oos;
							try {
								oos = new ObjectOutputStream(socket.getOutputStream());
						
								Message message1 = new Message();

								message1.setMsg(receiver.getText());
								//message1.setMsg(message1.getMsg());
								//+++++++++++++++++++++++++++++=传port给allchatting
								message1.setPort(port);
								
								message1.setFlag(1);
							
								oos.writeObject(message1);
								System.out.println(message.getFlag()+"******************");
								System.out.println("传来的消息2："+receiver.getText());
								
								
								oos.flush();
							
							
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						
						receiver.setText("");
						sender.setText("");
						
						

				}else if(flag == 2){//私聊方法-----------------
				//public void privateMethod(){	
					System.out.println("私聊传来的消息："+ message.getMsg());
					int port1 = Integer.parseInt(Integer.toString(message.getPort()));
					System.out.println("私聊传来的port："+ port1);
					
					//根据传来的port1来寻找对应的客户端-------
					Socket socket = socketMap.get(port1);
					Message message2 = new Message();
					
					message2.setPort(port);
					message2.setMsg(receiver.getText());
					System.out.println(message2.getMsg());
					message2.setFlag(2);

					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(message2);
						oos.flush();
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					receiver.setText("");
					sender.setText("");
	
				}else if(flag == 3){//好友下线-----------------
					
					// 获取客户端的端口号
					InetSocketAddress add2 = (InetSocketAddress) socket.getRemoteSocketAddress();
					int remove_port = add2.getPort();
					portList.remove((Integer)remove_port);
					socketMap.remove((Integer)remove_port);
					Set set = socketMap.keySet();
					Iterator ite = set.iterator();
					while(ite.hasNext()){
						
						Integer i = (Integer)ite.next();
						Socket socket = socketMap.get(i);
						Message msg2 = new Message();
						msg2.setList(portList);
						msg2.setPort(0);
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(msg2);
						oos.flush();
						
					}

				}

			}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {

		qqServer qst = new qqServer();

	}

}