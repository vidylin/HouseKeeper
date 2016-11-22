package com.hrsst.housekeeper.common.data;

import java.io.Serializable;

public class JAContact implements Serializable{
	// id
	private int id;
	private String gwid = "";
	private String jaid = "";
	private String pwd = "";
	private String user = "admin";
	private int port;
	private String activeUser="0";
	private int channl=4;
	private int[] channls;
	// 不存数据库
	private String ip = "";
	public JAContact() {
		super();
	}
	
	

	public JAContact(int id, String gwid, String jaid, String pwd, String user,
			int port, String activeUser, int channl, String ip) {
		super();
		this.id = id;
		this.gwid = gwid;
		this.jaid = jaid;
		this.pwd = pwd;
		this.user = user;
		this.port = port;
		this.activeUser = activeUser;
		this.channl = channl;
		this.ip = ip;
		paserChannels(channl);
	}

	private void paserChannels(int channl){
		channls=new int[channl];
		for(int i=0;i<channl;i++){
			channls[i]=i;
		}
	}

	public int[] getChannls() {
		return channls;
	}

	public void setChannls(int[] channls) {
		this.channls = channls;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid) {
		this.gwid = gwid;
	}
	public String getJaid() {
		return jaid;
	}
	public void setJaid(String jaid) {
		this.jaid = jaid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(String activeUser) {
		this.activeUser = activeUser;
	}

	public int getChannl() {
		return channl;
	}

	public void setChannl(int channl) {
		this.channl = channl;
		paserChannels(channl);
	}



	@Override
	public String toString() {
		return "JAContact [id=" + id + ", gwid=" + gwid + ", jaid=" + jaid
				+ ", pwd=" + pwd + ", user=" + user + ", port=" + port
				+ ", activeUser=" + activeUser + ", channl=" + channl + ", ip="
				+ ip + "]";
	}

}
