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

	// �ܲ���--------------------------------------
	private JFrame f = new JFrame();
	private JPanel p = new JPanel();
	// �ϲ�ͷ������������ǩ������-------------------
	private JPanel p_up = new JPanel();
	// �м䲿����ͷ��͸���ǩ��------------------------
	private JPanel p_up_center = new JPanel();
	// ���沿�ֺ����б�--------------------------------
	private JPanel p_under = new JPanel();
	private JPanel p_up_south = new JPanel();
	private JLabel p_headpicture = new JLabel();
	private JLabel p_username = new JLabel("                          İ�W��");
	private JLabel p_yiwei = new JLabel("                                    ");
	private JLabel p_personalsign = new JLabel("                  �˴����Ǹ���ǩ��");
	private JTextField search = new JTextField(
			"��������ϵ�ˡ��������졢Ⱥ����ҵ                                                   ");
	private JLabel p_friendlist_display = new JLabel(
			"            �����б�                  ");
	private JLabel p_under_friendlist_yiwei = new JLabel("");
	// �ײ�����Ⱥ�ĺ�˽�Ĺ���---------------------
	private JButton qunliao = new JButton("Ⱥ��");
	private JButton exit = new JButton("�˳�");
	private JPanel p_last = new JPanel();
	 private JLabel none = new JLabel(" ");
	//private JButton refresh = new JButton("ˢ��");

	// ����һ��Jlist��-----------------
	private JList friendJList = new JList();
	private Socket socket;

	// private Message message;
	private String ReceiveMsg;
	// ˽��
	private privateChatting siliaoChatting;
	// Ⱥ��
	private allChatting qunliaoChatting;

	private String s_receiveport;
	private Map<Integer, privateChatting> port_privateChattingMap = new HashMap<Integer, privateChatting>();
	private String username;
	private int allport;
	//�޲ι��췽��-----------------
	public testqqfriendlist() {
		super();
		// TODO Auto-generated constructor stub
	}
	// ���췽����װ--------------
	public testqqfriendlist(String username) {

		this.username = username;
		init();

		try {
			socket = new Socket("127.0.0.1", 9999);

			// ��ȡ�ͻ��˵Ķ˿ں�
			InetSocketAddress add = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			allport = add.getPort();
			System.out.println(allport);
			//friendJlistMsgListeners();
			
			// Ⱥ��----
			qunliaoListeners();
			// ˽��----
			friendJlistMsgListeners();
			
			//��������----
			exitlisteners();			

			// �����б��߳�---
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

	// �����б���ʾ���߳�--------------
	class friendJlistMsgThread extends Thread {

		public void run() {

			while (true) {

				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	
					Message message = (Message) ois.readObject();
					// ������򴫸��������ٴ��������б�-------------
					int flag = message.getFlag();
					System.out.println(flag);

					if (flag == 0) {// ��ʾ�����б�--------------------
						System.out.println("=------------------");
						DefaultListModel listModel = new DefaultListModel();

						for (int i = 0; i < message.getList().size(); i++) {

							listModel.addElement(message.getList().get(i));

						}

						friendJList.setModel(listModel);

					}else if (flag == 1) {// Ⱥ�ķ��͸������Ľ��տ򣡣���ȥд

						if (qunliaoChatting == null) {

							qunliaoChatting = new allChatting(socket,1);

						}

						String msg = message.getMsg();
						qunliaoChatting.setchat_text(msg);
						System.out.println("��������Ϣ" + msg);

					}else if (flag == 2) {// ˽�ķ��͸������Ľ��տ򣡣���ȥд

						Set set = port_privateChattingMap.keySet();
						Iterator ite = set.iterator();						
						Integer port = message.getPort();						
						privateChatting siliaoChatting = port_privateChattingMap.get(port);
						
						String msg = message.getMsg();
						System.out.println("˽�ĵ���Ϣ��" + msg);						
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

	// Ⱥ��-------------------------
	public void qunliaoListeners() {

		qunliao.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// ��socket��Ⱥ��flag=1����chatting---------
				int flag = 1;

				qunliaoChatting = new allChatting(socket, 1);
				System.out.println("��ǰ��flag" + flag);
				System.out.println("message�е�flag" + qunliaoChatting.getFlag());
				
			}

		});

	}
//�˳��������߼�����-------------------------------
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
	
	
	// �Ժ����б���м�����,˫�����������-----˽��---------------------
	public void friendJlistMsgListeners() {

		friendJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);

				if (e.getClickCount() == 2) {
					// ��socket��˽��flag=2����chatting---------
					Integer port = (Integer) friendJList.getSelectedValue();
					System.out.println("�����port��" + port);
					
					siliaoChatting = new privateChatting(socket, port);

					System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
					System.out.println(port);
					
					//siliaoChatting = new privateChatting(socket,port);
					//һ���˿ںŶ�Ӧһ��˽�Ĵ���-----
					port_privateChattingMap.put(port, siliaoChatting);
	
				}
				//���˿ںźͶ�Ӧ�Ĵ��ڴ���map��
				
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

		// p_up��������borderlayout��������-----------------
		p_up.setLayout(new BorderLayout());
		p_up.setBackground(new Color(113, 208, 255));

		// p_up_center����gridlayout��2��1������------------
		p_up_center.setLayout(new GridLayout(2, 1));
		p_up_center.setBackground(new Color(113, 208, 255));

		// p_under ��borderlayout����----------
		p_under.setLayout(new BorderLayout());
		p_under.setBackground(new Color(113, 208, 255));

		// �ϲ�ͷ������������ǩ������-------------------

		p_username.setText(username);
		
		p_up_center.add(p_username);
		p_up_center.add(p_personalsign);

		p_headpicture.setIcon(new ImageIcon(getClass().getResource(
				"/resource/3.jpg")));

		// �����Һ��Ѻ���ʾ���ѷ����ϱߣ�������gridlayout��2,1������--
		p_up_south.setLayout(new GridLayout(2, 1));
		p_up_south.setBackground(new Color(113, 208, 255));
		p_up_south.add(p_friendlist_display);
		p_up_south.add(search);

		p_up.add(p_headpicture, BorderLayout.WEST);
		p_up.add(p_up_center, BorderLayout.CENTER);
		p_up.add(p_yiwei, BorderLayout.EAST);
		p_up.add(p_up_south, BorderLayout.SOUTH);
		// ���·��ĺ����б������������÷���borderlayout����---

		// �����б�---------------------------------------
		p_under.add(friendJList, BorderLayout.CENTER);
		// ������ѡ�����------------------------------
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
