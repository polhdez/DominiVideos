package com.domain.videos;

import java.util.ArrayList;

public class User {
	private String email;
	private String name;
	private String surnames;
	private String password;
	private ArrayList<Video> userVideos = new ArrayList<Video>();
	
	public User(String email, String name, String surnames, String password) throws Exception {
		this.setEmail(email);
		this.setName(name);
		this.setSurnames(surnames);
		this.setPassword(password);
	}
	
	// Getters and setters
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) throws Exception {
		checkIfEmpty(email);
		this.name = email;
	}
	
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) throws Exception {
		checkIfEmpty(name);
		this.name = name;
	}

	public String getSurnames() {
		return this.surnames;
	}

	public void setSurnames(String surnames) throws Exception {
		checkIfEmpty(surnames);
		this.surnames = surnames;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) throws Exception {
		checkIfEmpty(password);
		this.password = password;
	}
	
	public void addUserVideo(Video video)  {
		this.userVideos.add(video);
	}
	
	public ArrayList<Video> getUserVideos() {
		return this.userVideos;
	}
	
	// Methods
	
	private void checkIfEmpty(String str) throws Exception {
		if(str.isEmpty()) {
			throw new EmptyStringException("[!] " + str + " is empty!");
		}
	}
}
