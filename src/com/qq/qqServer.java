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
	private JFrame f = new JFrame("������");
	private JPanel p = new JPanel();
	private JTextArea receiver = new JTextArea(5, 16);
	private JTextArea sender = new JTextArea(5, 16);
	private JButton btn = new JButton("����");
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
		
		System.out.println("����������������");
		
	}

	//���췽����װ----------------------
	public qqServer() {

		init();
		// ��������������-------------------

		try {
			server = new ServerSocket(9999);

			socketThread st = new socketThread();
			st.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	// �������ն���ͻ������ӵ��߳�--------��2��----Ƕ�ף�1��------
	class socketThread extends Thread {

		public void run() {

			try {

				while (true) {

					socket = server.accept();

					// ��ȡ�ͻ��˵Ķ˿ں�
					InetSocketAddress add = (InetSocketAddress) socket.getRemoteSocketAddress();
					int port = add.getPort();

					portList.add(port);

					System.out.println(portList.toString());
					
					// �ѺͿͻ������ӵ�socket������
					socketMap.put(port, socket);
					
					// ������Ϣ���߳�-----(1)---------
					receiveThread rt = new receiveThread(socket);
					rt.start();

					//��������ˢ��----
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

	// ������Ϣ���߳�-----(1)---------
	class receiveThread extends Thread {

		private Socket socket;

		public receiveThread(Socket socket) {

			this.socket = socket;
		}

		public void run() {

			// ������Ϣ--------
			try {

				// ��ȡ�ͻ��˵Ķ˿ں�
				InetSocketAddress add = (InetSocketAddress) socket
						.getRemoteSocketAddress();
				int port = add.getPort();
				//��������д�������message�����ȡ��Ϣ----------------
				
				
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
					//�ӹ�������Ϣǰ����˿ں�port���ٷ�װ---------------
					receiver.append(s + "    "+ hour + ":" + 
							minute + ":" + second + "\n" + port + "˵:" + 
							message.getMsg() + "\n");

					System.out.println("port:" + port);

					System.out.println("��������Ϣ1��"+receiver.getText());
					
					int flag = message.getFlag();
					System.out.println("�������յ���flag:" + flag);
					
					
					if(flag == 1){//Ⱥ�ķ���-----------------

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
								//+++++++++++++++++++++++++++++=��port��allchatting
								message1.setPort(port);
								
								message1.setFlag(1);
							
								oos.writeObject(message1);
								System.out.println(message.getFlag()+"******************");
								System.out.println("��������Ϣ2��"+receiver.getText());
								
								
								oos.flush();
							
							
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						
						receiver.setText("");
						sender.setText("");
						
						

				}else if(flag == 2){//˽�ķ���-----------------
				//public void privateMethod(){	
					System.out.println("˽�Ĵ�������Ϣ��"+ message.getMsg());
					int port1 = Integer.parseInt(Integer.toString(message.getPort()));
					System.out.println("˽�Ĵ�����port��"+ port1);
					
					//���ݴ�����port1��Ѱ�Ҷ�Ӧ�Ŀͻ���-------
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
	
				}else if(flag == 3){//��������-----------------
					
					// ��ȡ�ͻ��˵Ķ˿ں�
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