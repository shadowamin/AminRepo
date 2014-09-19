package com.saladinprojects.problemssolutions.models;

public class Solution {
	private int id;
	private String txtsol;
	private String date;
	private int score;
	private int idprob;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Solution(int id, String txtsol, String date, int score, int idprob) {
		super();
		this.id = id;
		this.txtsol = txtsol;
		this.date = date;
		this.score = score;
		this.idprob = idprob;
	}

	public Solution() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTxtsol() {
		return txtsol;
	}

	public int getIdprob() {
		return idprob;
	}

	public void setIdprob(int idprob) {
		this.idprob = idprob;
	}

	public void setTxtsol(String txtsol) {
		this.txtsol = txtsol;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
