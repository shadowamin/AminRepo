package com.saladinprojects.problemssolutions.models;

public class Categorie {
private int id;
private String title;


public Categorie() {
	super();
}
public Categorie(int id, String title) {
	super();
	this.id = id;
	this.title = title;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}

}