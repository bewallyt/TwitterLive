package com.play.twitterlive;


public class Employee {
    public static String name;
    public static String title;
    
    public Employee(String name, String title){
    	this.name = name;
    	this.title = title;
    }
    
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		Employee.name = name;
	}
	public static String getTitle() {
		return title;
	}
	public static void setTitle(String title) {
		Employee.title = title;
	}
    
    
}
