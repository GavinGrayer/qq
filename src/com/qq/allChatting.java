package com.qq;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.qq.Message;

import com.qq.*;

public class allChatting {

	
	//�ܲ���-------------------------------------------
	private JFrame f = new JFrame();
	private JPanel p = new JPanel();
	//������borderlayout���֣��������͸���ǩ���������ߵ�gridlayout��------------------
	private JPanel p_north = new JPanel();
	private JPanel p_north_west = new JPanel();
	private JLabel usernamewangming = new JLabel("           Ⱥ�Ĵ���");
	private JLabel personalsign = new JLabel("           �˴�����ǩ��");
	//east���߲��ַ�QQ��----------------------------------------
	private JLabel qqxiu = new JLabel(new ImageIcon(getClass().getResource("/resource/4.png")));
	//�м䲿����gridlayout����------------------------------------------
	private JPanel chat = new JPanel();	
		//�岿��-----------------------------------
	private JLabel chatobject = new JLabel("          �������IP:abc)����");
	private static JTextArea chat_text = new JTextArea(null,10,200);
	private JLabel yiwei = new JLabel();
	//private JTextArea chat_write = new JTextArea(null,15,1);
	private JPanel chat_close_and_send = new JPanel();	
	//chat_close_and_send�������ٷ�borderlayout����---------------------------
	private JPanel yiwei_north = new JPanel();
	private JTextArea chat_write = new JTextArea(null,10,58);
	private JPanel yiwei_south = new JPanel();

	//��p��������뷢�ͺ͹ر�----------------------------------------
	private JPanel p_south = new JPanel();
	private JButton guanbiLabel = new JButton("�ر�");
	private JButton sendiLabel = new JButton("Ⱥ�ķ���");
	private JLabel hello = new JLabel("����������         ");
	private JLabel hello1 = new JLabel("                                                                                  ");
	private JButton qunliao = new JButton("Ⱥ��");
	private JButton siliao = new JButton("˽��");	
	private Socket socket;	
	private Message message;	
	private int flag;
	private int port;
	
	

	public allChatting() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void allChatting(int port){
		
		this.port=port;
	}

	//��ȡ��Ϣ------------------
	public void setchat_text(String msg){
		
		chat_text.append(msg);

	}
	
	public int getFlag(){
		
		return flag;
	}

	public allChatting(Socket socket,int flag) {
		
		this.socket = socket;
		this.flag = flag;
		
		
		init();
		sendMsgListeners();  

	}
	


	//Ⱥ�ķ�����Ϣ--------------------------
	public void sendMsgListeners(){
		
		sendiLabel.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sendMsg = chat_write.getText();
				System.out.println("++++++++++++++++++++++++");
				
				try {

					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

					Message message = new Message();
	
					message.setMsg(sendMsg);
					
					message.setFlag(1);
					System.out.println(flag);
					//����������-------------
					oos.writeObject(message);
					
					oos.flush();
						
					chat_write.setText("");

				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
	
		});
		
		
	}

	public void init(){
		
		//�ܲ�����bordelayout----------------------------
		p.setLayout(new BorderLayout());
		p.setBackground(new Color(113, 208, 255));
		
		
		//������borderlayout����-----------------------
		p_north.setLayout(new BorderLayout());
		p_north.setBackground(new Color(113, 208, 255));
		
		//p_north_west�����gridlayout����-----------------------------
		p_north_west.setLayout(new GridLayout(3,1));
		p_north_west.setBackground(new Color(113, 208, 255));

		p_north_west.add(usernamewangming);
		p_north_west.add(personalsign);
		p_north_west.add(chatobject);
		
		p_north.add(p_north_west,BorderLayout.WEST);

		//�м䲿����gridlayout����----------------------------------
		chat.setLayout(new BorderLayout());
		chat.setBackground(new Color(113, 208, 255));
		
		chat.add(chat_text,BorderLayout.CENTER);

		
		//�·�chat_close_and_send���������yiwei_north,chat_write,yiwei_south--------
		
		yiwei_north.setLayout(new FlowLayout());
		
		//yiwei_north.setBackground(new Color(113, 208, 255));
		
		yiwei_north.add(hello);

		chat_close_and_send.add(yiwei_north,BorderLayout.NORTH);
		chat_close_and_send.add(chat_write,BorderLayout.CENTER);

		chat.add(chat_close_and_send,BorderLayout.SOUTH);

		//��guanbiLabel��sendilabel����p���ϱ�------------------------
		guanbiLabel.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				f.dispose();
			}
	
		});
		
		p_south.setLayout(new FlowLayout());
		p_south.setBackground(new Color(113, 208, 255));
		p_south.add(hello1);
		p_south.add(sendiLabel);
		p_south.add(guanbiLabel);
		
		
		
		p.add(p_north,BorderLayout.NORTH);		
		p.add(chat,BorderLayout.CENTER);		
		p.add(qqxiu,BorderLayout.EAST);		
		p.add(p_south,BorderLayout.SOUTH);
		
		f.add(p);
		f.setSize(820,670);
		f.setVisible(true);
		f.setResizable(false);
	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Chatting tqc = new Chatting();
		
	}

}
