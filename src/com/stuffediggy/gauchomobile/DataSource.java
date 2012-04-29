package com.stuffediggy.gauchomobile;

import java.util.ArrayList;

public class DataSource {
	private static DataSource instance = null;
	private ArrayList<Course> courses = new ArrayList<Course>();
	private String username, password;
	private Course currentCourse;
	
	protected DataSource() {
		// Exists only to defeat instantiation.
	}

	public static DataSource getInstance() {
		if (instance == null) {
			instance = new DataSource();
		}
		return instance;
	}
	
	public void setUsername(String newUsername) {
		username = newUsername;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String newPassword) {
		password = newPassword;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setCurrentCourse(Course _course) {
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).equals(_course)) {
				currentCourse = courses.get(i);
			}
		}
	}
	
	public Course getCurrentCourse() {
		return currentCourse;
	}
	
	public void addCourse(Course newCourse) {
		courses.add(newCourse);
	}
	
	public void removeAllCourses() {
		courses.clear();
	}
	
	public ArrayList<Course> getAllCourses() {
		return courses;
	}
}
