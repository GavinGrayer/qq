package com.qq;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class testqqlogin {

	private JFrame f = new JFrame("QQ");
	private JPanel p = new JPanel();
	
	//上部分的QQ图片
	private JLabel labelloginpicture = new JLabel(new ImageIcon(getClass().getResource("/resource/1.jpg")));

	//下部分的总框架P1
	private JPanel p1 = new JPanel();
	private JLabel username = new JLabel("    用户帐号 :    ");
	private JLabel password = new JLabel("    用户密码 :    ");
	
	//帐号密码文本框
	private JTextField writeusername = new JTextField(12);
	private JPasswordField writepassword = new JPasswordField(12);
	private Checkbox rememberpwd = new Checkbox("      记住密码        ",false);
	private Checkbox autologin = new Checkbox("      自动登录        ",false);
	
	//下方登录按钮
	private JButton labelloginpicture1 = new JButton("安全登录");
	
	//左侧头像
	private JLabel labelloginpicture2 = new JLabel(new ImageIcon(getClass().getResource("/resource/3.jpg")));

	//p1中的center建立panel-------------------------
	private JPanel p1_center = new JPanel();
	
	//给中间的用户帐号、用户密码、记住密码和自动登录、安全登录分配4层
	private JPanel p1_center_1 = new JPanel();
	private JPanel p1_center_2 = new JPanel();
	private JPanel p1_center_3 = new JPanel();
	private JPanel p1_center_4 = new JPanel();
	//将记住密码、自动登录顶到右边居中
	private JLabel p1_center_3_yiwei = new JLabel("            ");
	
	//p1中的west建立panel--------------------------
	private JPanel p1_west = new JPanel();
	
	//左边头像建立panel-------------------------
	private JPanel p1_west_center = new JPanel();
	private JLabel yiwei = new JLabel("           ");
	
	//右边的分两层放注册帐号和找回密码------------------------------
	private JPanel p1_east = new JPanel();
	private JPanel p1_east_1 = new JPanel();
	private JPanel p1_east_2 = new JPanel();
	private JLabel regist = new JLabel("注册帐号                                   ");
	private JLabel find = new JLabel("找回密码                                     ");

	//登录失败的对话框------------------------------
	private JDialog dl = new JDialog(f,false);
	private JLabel error = new JLabel("帐号或密码错误！请重新输入");

	public testqqlogin() {
		//总框架为gridlayout型---------------------------
		p.setLayout(new GridLayout(2,1));
		//下部分在center用Borderlayout型-------------------------
		p1.setLayout(new BorderLayout());
		
		//西边头像---------------------------
		p1_west.setLayout(new BorderLayout());	
				
		//右边-------------------------------
		p1_east.setLayout(new GridLayout(4,1,5,5));
		
		//每层都用flowlayout布局----------------------------		
		p1_east_1.setLayout(new FlowLayout());		
		p1_east_2.setLayout(new FlowLayout());		
		p1_east_1.add(regist);		
		p1_east_2.add(find);
				
		p1_east.add(p1_east_1);
		p1_east.add(p1_east_2);

		//将帐号密码，记住密码，自动登录，和登录放入center----------		
				//在p1_center中建四行网格布局------------------------
			p1_center.setLayout(new GridLayout(4,1,5,5));		
					//每层都用flowlayout布局保持居中----------------------------		
			p1_center_1.setLayout(new FlowLayout());		
			p1_center_2.setLayout(new FlowLayout());			
			p1_center_3.setLayout(new FlowLayout());			
			p1_center_4.setLayout(new FlowLayout());
			
			p1_center_1.add(username);
			p1_center_1.add(writeusername);			
			
			p1_center_2.add(password);
			p1_center_2.add(writepassword);
			
			p1_center_3.add(p1_center_3_yiwei);
			p1_center_3.add(rememberpwd);
			p1_center_3.add(autologin);
					
			p1_center_4.add(labelloginpicture1);
	
			//对话框------------------------------------
			dl.add(error);
			dl.setSize(200, 200);
			
			//登录失败跳出框居中----------
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = dl.getSize();
			dl.setLocation((screenSize.width - frameSize.width) / 2,
						(screenSize.height - frameSize.height) / 2);

			//登录跳转------------------------------------
			//利用login里的judge判断---------------------
			labelloginpicture1.setBackground(Color.LIGHT_GRAY );
			
			labelloginpicture1.addActionListener(new ActionListener(){

				
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String writeusername = testqqlogin.this.writeusername.getText();
					String writepassword = testqqlogin.this.writepassword.getText();
					
					user user = new user(writeusername,writepassword);
					
					login login = new login(user);
					
					if(login.judge() == 1){
						
						testqqfriendlist tqfl = new testqqfriendlist(writeusername.toString());
						f.setVisible(false);
						
					}
					
					if(login.judge() != 1){
						
						System.out.println("登录失败");
						dl.setVisible(true);
					}
					
					
				}
					
			});
		
			p1_center.add(p1_center_1);
			p1_center.add(p1_center_2);
			p1_center.add(p1_center_3);
			p1_center.add(p1_center_4);			
			
			//将上部图片放入p里----------------------------------
			p.add(labelloginpicture);
	
			//将左边的头像放入p1_west_east中-----------------------		
		p1_west.add(labelloginpicture2,BorderLayout.CENTER);		
		p1_west.add(yiwei,BorderLayout.WEST);

		p1.add(p1_west,BorderLayout.WEST);
		p1.add(p1_center,BorderLayout.CENTER);
		p1.add(p1_east,BorderLayout.EAST);

		p.add(p1);
		
		Dimension screenSize2 = Toolkit.getDefaultToolkit().getScreenSize();
		f.add(p);
		f.setSize(600,500);
		//f.setSize(screenSize2.width/2,screenSize2.height/2);
		f.setVisible(true);
		
		//jframe居中--------
		Dimension screenSize1 = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize1 = f.getSize();
		f.setLocation((screenSize1.width - frameSize1.width) / 2,
					(screenSize1.height - frameSize1.height) / 2);
	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testqqlogin tql = new testqqlogin();
	}

}
