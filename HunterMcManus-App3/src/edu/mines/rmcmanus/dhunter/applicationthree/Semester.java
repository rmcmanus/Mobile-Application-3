package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.ArrayList;

public class Semester {
	String semesterType;
	String semesterYear;
	String objectId;
	ArrayList<Course> courses;
	
	public Semester(String semesterType, String semesterYear, String objectId) {
		this.semesterType = semesterType;
		this.semesterYear = semesterType;
		this.objectId = objectId;
	}
}
