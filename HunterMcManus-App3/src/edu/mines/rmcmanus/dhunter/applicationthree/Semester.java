package edu.mines.rmcmanus.dhunter.applicationthree;

import java.util.ArrayList;

public class Semester {
	String semesterType;
	String semesterYear;
	ArrayList<Course> courses;
	
	public Semester(String semesterType, String semesterYear) {
		this.semesterType = semesterType;
		this.semesterYear = semesterType;
	}
}
