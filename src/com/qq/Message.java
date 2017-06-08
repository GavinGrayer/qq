package com.qq;


import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息对象
 * @author Administrator
 *
 */
public class Message implements Serializable{

	private String msg;
	
	private int port;
	
	private String font;
	
	private List<Integer> list = new ArrayList<Integer>();

	private Map<Integer, String> map = new HashMap<Integer, String>();
	
	private String receiveMsg;

	private int flag;
	
	
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getReceiveMsg() {
		return receiveMsg;
	}

	public void setReceiveMsg(String receiveMsg) {
		this.receiveMsg = receiveMsg;
	}

	

	
	

	public Message(Map<Integer, String> map) {
		super();
		this.map = map;
	}
	
	public Map getMap(){
		
		return map;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}
		
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	//构造方法--------------
	public Message(int flag,Map<Integer,String> map){
		
		this.flag = flag;
		this.map = map;
	}

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
