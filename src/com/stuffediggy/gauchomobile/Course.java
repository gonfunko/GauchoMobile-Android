package com.stuffediggy.gauchomobile;

public class Course {
	String name;
	Integer courseID;
    /*NSString *name;
    NSArray *assignments;
    NSMutableDictionary *participants;
    NSArray *participantsArray;
    NSArray *grades;
    NSArray *dashboardItems;
    NSArray *forums;
    NSInteger courseID;*/
	
	public Course() {
		name = "";
		courseID = 0;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCourseID(Integer newID) {
		courseID = newID;
	}
	
	public Integer getCourseID() {
		return courseID;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Course) {
			if (this.courseID == ((Course)obj).courseID &&
				this.name.equals(((Course)obj).name)) {
				return true;
			}
		}
		
		return false;
	}

}
