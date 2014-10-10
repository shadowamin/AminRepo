package com.saladinprojects.problemssolutions.models;

import java.util.ArrayList;


public class GroupeProbs implements Comparable<GroupeProbs>{
	private String Name;
	private ArrayList<Problem> Items;
	private int nbprioritys;
	
	public int getNbprioritys() {
		return nbprioritys;
	}
	public void setNbprioritys(int nbprioritys) {
		this.nbprioritys = nbprioritys;
	}
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
	@Override
	public int compareTo(GroupeProbs another) {
		if(this.nbprioritys<another.getNbprioritys())
		return 1;
		else
			return -1;
		
	}
	
	
}
