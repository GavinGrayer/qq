package com.qq;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.qq.Message;
import com.qq.allChatting;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;

import sun.applet.resources.MsgAppletViewer;
import sun.net.www.content.text.plain;

public class testqqfriendlist {

	// 总布局--------------------------------------
	private JFrame f = new JFrame();
	private JPanel p = new JPanel();
	// 上部头像、网名、个性签名布局-------------------
	private JPanel p_up = new JPanel();
	// 中间部分有头像和个性签名------------------------
	private JPanel p_up_center = new JPanel();
	// 下面部分好友列表--------------------------------
	private JPanel p_under = new JPanel();
	private JPanel p_up_south = new JPanel();
	private JLabel p_headpicture = new JLabel();
	private JLabel p_username = new JLabel("                          陌乄涯");
	private JLabel p_yiwei = new JLabel("                                    ");
	private JLabel p_personalsign = new JLabel("                  此处尽是个性签名");
	private JTextField search = new JTextField(
			"搜索：联系人、多人聊天、群、企业                                                   ");
	private JLabel p_friendlist_display = new JLabel(
			"            好友列表                  ");
	private JLabel p_under_friendlist_yiwei = new JLabel("");
	// 底部加入群聊和私聊功能---------------------
	private JButton qunliao = new JButton("群聊");
	private JButton exit = new JButton("退出");
	private JPanel p_last = new JPanel();
	 private JLabel none = new JLabel(" ");
	//private JButton refresh = new JButton("刷新");

	// 建立一个Jlist表-----------------
	private JList friendJList = new JList();
	private Socket socket;

	// private Message message;
	private String ReceiveMsg;
	// 私聊
	private privateChatting siliaoChatting;
	// 群聊
	private allChatting qunliaoChatting;

	private String s_receiveport;
	private Map<Integer, privateChatting> port_privateChattingMap = new HashMap<Integer, privateChatting>();
	private String username;
	private int allport;
	//无参构造方法-----------------
	public testqqfriendlist() {
		super();
		// TODO Auto-generated constructor stub
	}
	// 构造方法封装--------------
	public testqqfriendlist(String username) {

		this.username = username;
		init();

		try {
			socket = new Socket("127.0.0.1", 9999);

			// 获取客户端的端口号
			InetSocketAddress add = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			allport = add.getPort();
			System.out.println(allport);
			//friendJlistMsgListeners();
			
			// 群聊----
			qunliaoListeners();
			// 私聊----
			friendJlistMsgListeners();
			
			//好友下线----
			exitlisteners();			

			// 好友列表线程---
			friendJlistMsgThread fjmt = new friendJlistMsgThread();
			fjmt.start();
			
			searchlisteners();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}
	public void searchlisteners(){
		search.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount() == 1){
					search.setText("");
				}
			}
			
			
		});
	
	}

	// 好友列表显示的线程--------------
	class friendJlistMsgThread extends Thread {

		public void run() {

			while (true) {

				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	
					Message message = (Message) ois.readObject();
					// 由聊天框传给服务器再传给好友列表-------------
					int flag = message.getFlag();
					System.out.println(flag);

					if (flag == 0) {// 显示好友列表--------------------
						System.out.println("=------------------");
						DefaultListModel listModel = new DefaultListModel();

						for (int i = 0; i < message.getList().size(); i++) {

							listModel.addElement(message.getList().get(i));

						}

						friendJList.setModel(listModel);

					}else if (flag == 1) {// 群聊发送给聊天框的接收框！！回去写

						if (qunliaoChatting == null) {

							qunliaoChatting = new allChatting(socket,1);

						}

						String msg = message.getMsg();
						qunliaoChatting.setchat_text(msg);
						System.out.println("传来的消息" + msg);

					}else if (flag == 2) {// 私聊发送给聊天框的接收框！！回去写

						Set set = port_privateChattingMap.keySet();
						Iterator ite = set.iterator();						
						Integer port = message.getPort();						
						privateChatting siliaoChatting = port_privateChattingMap.get(port);
						
						String msg = message.getMsg();
						System.out.println("私聊的消息：" + msg);						
						if(siliaoChatting == null){
							
							siliaoChatting = new privateChatting(socket,port);
							
							port_privateChattingMap.put(port, siliaoChatting);
								
							System.out.println("666666666666666666666666666");
							
							siliaoChatting.setchat_text(msg);
							
						}else{
							
							siliaoChatting.setchat_text(msg);
						}
		
					}
				}

				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	// 群聊-------------------------
	public void qunliaoListeners() {

		qunliao.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// 将socket和群聊flag=1传给chatting---------
				int flag = 1;

				qunliaoChatting = new allChatting(socket, 1);
				System.out.println("当前的flag" + flag);
				System.out.println("message中的flag" + qunliaoChatting.getFlag());
				
			}

		});

	}
//退出好友下线监听器-------------------------------
	public void exitlisteners(){
		
        exit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				
					Message message = new Message();
					message.setFlag(3);
					oos.writeObject(message);
					f.dispose();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
  
        });
		
	}
	
	
	// 对好友列表进行监听器,双击进入聊天框-----私聊---------------------
	public void friendJlistMsgListeners() {

		friendJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);

				if (e.getClickCount() == 2) {
					// 将socket和私聊flag=2传给chatting---------
					Integer port = (Integer) friendJList.getSelectedValue();
					System.out.println("点击的port：" + port);
					
					siliaoChatting = new privateChatting(socket, port);

					System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
					System.out.println(port);
					
					//siliaoChatting = new privateChatting(socket,port);
					//一个端口号对应一个私聊窗口-----
					port_privateChattingMap.put(port, siliaoChatting);
	
				}
				//将端口号和对应的窗口存入map中
				
				Set set = port_privateChattingMap.keySet();
				Iterator ite = set.iterator();
				while(ite.hasNext()){
					
					Integer port3 = (Integer) ite.next();
					System.out.println(port3);
					privateChatting siliaoChatting2 = port_privateChattingMap.get(port3);
					System.out.println(siliaoChatting2);
				}
				
			}

		});

	}

	public void init() {

		p.setLayout(new BorderLayout());
		p.setBackground(new Color(113, 208, 255));

		// p_up布局里用borderlayout（）布局-----------------
		p_up.setLayout(new BorderLayout());
		p_up.setBackground(new Color(113, 208, 255));

		// p_up_center里用gridlayout（2，1）布局------------
		p_up_center.setLayout(new GridLayout(2, 1));
		p_up_center.setBackground(new Color(113, 208, 255));

		// p_under 用borderlayout布局----------
		p_under.setLayout(new BorderLayout());
		p_under.setBackground(new Color(113, 208, 255));

		// 上部头像、网名、个性签名布局-------------------

		p_username.setText(username);
		
		p_up_center.add(p_username);
		p_up_center.add(p_personalsign);

		p_headpicture.setIcon(new ImageIcon(getClass().getResource(
				"/resource/3.jpg")));

		// 将查找好友和显示好友放在南边，里面用gridlayout（2,1）布局--
		p_up_south.setLayout(new GridLayout(2, 1));
		p_up_south.setBackground(new Color(113, 208, 255));
		p_up_south.add(p_friendlist_display);
		p_up_south.add(search);

		p_up.add(p_headpicture, BorderLayout.WEST);
		p_up.add(p_up_center, BorderLayout.CENTER);
		p_up.add(p_yiwei, BorderLayout.EAST);
		p_up.add(p_up_south, BorderLayout.SOUTH);
		// 将下方的好友列表和添加朋友设置放入borderlayout布局---

		// 好友列表---------------------------------------
		p_under.add(friendJList, BorderLayout.CENTER);
		// 添加朋友、设置------------------------------
		p_last.setLayout(new GridLayout(1, 3));
		p_last.setBackground(new Color(113, 208, 255));

		p_last.add(qunliao);
		p_last.add(none);
		p_last.add(exit);

		p.add(p_up, BorderLayout.NORTH);
		p.add(p_under, BorderLayout.CENTER);
		p.add(p_last, BorderLayout.SOUTH);

		f.add(p);
		f.setSize(360, 750);
		f.setVisible(true);
		f.setResizable(false);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testqqfriendlist tfl = new testqqfriendlist("8");

	}

}
