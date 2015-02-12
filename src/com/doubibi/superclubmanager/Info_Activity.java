package com.doubibi.superclubmanager;

public class Info_Activity {

	private String club, atyName, atyTime, atyTheme, atyContext;
	private Boolean atyRelease;
	
	public Info_Activity(String club, String atyName, String atyTime,
			String atyTheme, String atyContext, Boolean atyRelease) {
		super();
		this.club = club;
		this.atyName = atyName;
		this.atyTime = atyTime;
		this.atyTheme = atyTheme;
		this.atyContext = atyContext;
		this.atyRelease = atyRelease;
	}

	public String getClub() {
		return club;
	}
	
	public void setClub(String club) {
		this.club = club;
	}
	public String getAtyName() {
		return atyName;
	}
	public void setAtyName(String atyName) {
		this.atyName = atyName;
	}
	public String getAtyTime() {
		return atyTime;
	}
	public void setAtyTime(String atyTime) {
		this.atyTime = atyTime;
	}
	public String getAtyTheme() {
		return atyTheme;
	}
	public void setAtyTheme(String atyTheme) {
		this.atyTheme = atyTheme;
	}
	public String getAtyContext() {
		return atyContext;
	}
	public void setAtyContext(String atyContext) {
		this.atyContext = atyContext;
	}
	public Boolean getAtyRelease() {
		return atyRelease;
	}

	public void setAtyRelease(Boolean atyRelease) {
		this.atyRelease = atyRelease;
	}
	
	
}
