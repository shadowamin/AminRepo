package com.saladinprojects.problemssolutions.models;

import java.util.ArrayList;


public class GroupeProbs {
	private String Name;
	private ArrayList<Problem> Items;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public ArrayList<Problem> getItems() {
		return Items;
	}
	public void setItems(ArrayList<Problem> Items) {
		this.Items = Items;
	}
	
}
