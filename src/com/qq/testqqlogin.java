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
	
	//�ϲ��ֵ�QQͼƬ
	private JLabel labelloginpicture = new JLabel(new ImageIcon(getClass().getResource("/resource/1.jpg")));

	//�²��ֵ��ܿ��P1
	private JPanel p1 = new JPanel();
	private JLabel username = new JLabel("    �û��ʺ� :    ");
	private JLabel password = new JLabel("    �û����� :    ");
	
	//�ʺ������ı���
	private JTextField writeusername = new JTextField(12);
	private JPasswordField writepassword = new JPasswordField(12);
	private Checkbox rememberpwd = new Checkbox("      ��ס����        ",false);
	private Checkbox autologin = new Checkbox("      �Զ���¼        ",false);
	
	//�·���¼��ť
	private JButton labelloginpicture1 = new JButton("��ȫ��¼");
	
	//���ͷ��
	private JLabel labelloginpicture2 = new JLabel(new ImageIcon(getClass().getResource("/resource/3.jpg")));

	//p1�е�center����panel-------------------------
	private JPanel p1_center = new JPanel();
	
	//���м���û��ʺš��û����롢��ס������Զ���¼����ȫ��¼����4��
	private JPanel p1_center_1 = new JPanel();
	private JPanel p1_center_2 = new JPanel();
	private JPanel p1_center_3 = new JPanel();
	private JPanel p1_center_4 = new JPanel();
	//����ס���롢�Զ���¼�����ұ߾���
	private JLabel p1_center_3_yiwei = new JLabel("            ");
	
	//p1�е�west����panel--------------------------
	private JPanel p1_west = new JPanel();
	
	//���ͷ����panel-------------------------
	private JPanel p1_west_center = new JPanel();
	private JLabel yiwei = new JLabel("           ");
	
	//�ұߵķ������ע���ʺź��һ�����------------------------------
	private JPanel p1_east = new JPanel();
	private JPanel p1_east_1 = new JPanel();
	private JPanel p1_east_2 = new JPanel();
	private JLabel regist = new JLabel("ע���ʺ�                                   ");
	private JLabel find = new JLabel("�һ�����                                     ");

	//��¼ʧ�ܵĶԻ���------------------------------
	private JDialog dl = new JDialog(f,false);
	private JLabel error = new JLabel("�ʺŻ������������������");

	public testqqlogin() {
		//�ܿ��Ϊgridlayout��---------------------------
		p.setLayout(new GridLayout(2,1));
		//�²�����center��Borderlayout��-------------------------
		p1.setLayout(new BorderLayout());
		
		//����ͷ��---------------------------
		p1_west.setLayout(new BorderLayout());	
				
		//�ұ�-------------------------------
		p1_east.setLayout(new GridLayout(4,1,5,5));
		
		//ÿ�㶼��flowlayout����----------------------------		
		p1_east_1.setLayout(new FlowLayout());		
		p1_east_2.setLayout(new FlowLayout());		
		p1_east_1.add(regist);		
		p1_east_2.add(find);
				
		p1_east.add(p1_east_1);
		p1_east.add(p1_east_2);

		//���ʺ����룬��ס���룬�Զ���¼���͵�¼����center----------		
				//��p1_center�н��������񲼾�------------------------
			p1_center.setLayout(new GridLayout(4,1,5,5));		
					//ÿ�㶼��flowlayout���ֱ��־���----------------------------		
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
	
			//�Ի���------------------------------------
			dl.add(error);
			dl.setSize(200, 200);
			
			//��¼ʧ�����������----------
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = dl.getSize();
			dl.setLocation((screenSize.width - frameSize.width) / 2,
						(screenSize.height - frameSize.height) / 2);

			//��¼��ת------------------------------------
			//����login���judge�ж�---------------------
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
						
						System.out.println("��¼ʧ��");
						dl.setVisible(true);
					}
					
					
				}
					
			});
		
			p1_center.add(p1_center_1);
			p1_center.add(p1_center_2);
			p1_center.add(p1_center_3);
			p1_center.add(p1_center_4);			
			
			//���ϲ�ͼƬ����p��----------------------------------
			p.add(labelloginpicture);
	
			//����ߵ�ͷ�����p1_west_east��-----------------------		
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
		
		//jframe����--------
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
