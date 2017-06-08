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

	
	//总布局-------------------------------------------
	private JFrame f = new JFrame();
	private JPanel p = new JPanel();
	//北边用borderlayout布局，将网名和个性签名放入西边的gridlayout里------------------
	private JPanel p_north = new JPanel();
	private JPanel p_north_west = new JPanel();
	private JLabel usernamewangming = new JLabel("           群聊窗口");
	private JLabel personalsign = new JLabel("           此处尽是签名");
	//east东边布局放QQ秀----------------------------------------
	private JLabel qqxiu = new JLabel(new ImageIcon(getClass().getResource("/resource/4.png")));
	//中间部分用gridlayout布局------------------------------------------
	private JPanel chat = new JPanel();	
		//五部分-----------------------------------
	private JLabel chatobject = new JLabel("          聊天对象（IP:abc)在线");
	private static JTextArea chat_text = new JTextArea(null,10,200);
	private JLabel yiwei = new JLabel();
	//private JTextArea chat_write = new JTextArea(null,15,1);
	private JPanel chat_close_and_send = new JPanel();	
	//chat_close_and_send布局里再分borderlayout布局---------------------------
	private JPanel yiwei_north = new JPanel();
	private JTextArea chat_write = new JTextArea(null,10,58);
	private JPanel yiwei_south = new JPanel();

	//在p的南面加入发送和关闭----------------------------------------
	private JPanel p_south = new JPanel();
	private JButton guanbiLabel = new JButton("关闭");
	private JButton sendiLabel = new JButton("群聊发送");
	private JLabel hello = new JLabel("请输入内容         ");
	private JLabel hello1 = new JLabel("                                                                                  ");
	private JButton qunliao = new JButton("群聊");
	private JButton siliao = new JButton("私聊");	
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

	//获取信息------------------
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
	


	//群聊发送消息--------------------------
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
					//发给服务器-------------
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
		
		//总布局用bordelayout----------------------------
		p.setLayout(new BorderLayout());
		p.setBackground(new Color(113, 208, 255));
		
		
		//北边用borderlayout布局-----------------------
		p_north.setLayout(new BorderLayout());
		p_north.setBackground(new Color(113, 208, 255));
		
		//p_north_west里放入gridlayout布局-----------------------------
		p_north_west.setLayout(new GridLayout(3,1));
		p_north_west.setBackground(new Color(113, 208, 255));

		p_north_west.add(usernamewangming);
		p_north_west.add(personalsign);
		p_north_west.add(chatobject);
		
		p_north.add(p_north_west,BorderLayout.WEST);

		//中间部分用gridlayout布局----------------------------------
		chat.setLayout(new BorderLayout());
		chat.setBackground(new Color(113, 208, 255));
		
		chat.add(chat_text,BorderLayout.CENTER);

		
		//下方chat_close_and_send布局里放入yiwei_north,chat_write,yiwei_south--------
		
		yiwei_north.setLayout(new FlowLayout());
		
		//yiwei_north.setBackground(new Color(113, 208, 255));
		
		yiwei_north.add(hello);

		chat_close_and_send.add(yiwei_north,BorderLayout.NORTH);
		chat_close_and_send.add(chat_write,BorderLayout.CENTER);

		chat.add(chat_close_and_send,BorderLayout.SOUTH);

		//将guanbiLabel和sendilabel放入p的南边------------------------
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
