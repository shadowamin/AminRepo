package com.saladinprojects.problemssolutions.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;



public class Problem implements Comparable<Problem>{
	private int idprob;
	private String resprob;
	private String textprob;
	private int typeprob;
	private int cat;
	private boolean withcat=false;
	private String date;
	private String Tag;
private int priority ;


	public String getTag() {
	return Tag;
}

public void setTag(String tag) {
	Tag = tag;
}



	public Problem() {
	super();
	// TODO Auto-generated constructor stub
}



	public boolean isWithcat() {
		return withcat;
	}



	public void setWithcat(boolean withcat) {
		this.withcat = withcat;
	}



	public Problem(int idprob, String resprob, String textprob, int typeprob,
			int cat, String date, int priority) {
		super();
		this.idprob = idprob;
		this.resprob = resprob;
		this.textprob = textprob;
		this.typeprob = typeprob;
		this.cat = cat;
		this.date = date;
		this.priority = priority;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getResprob() {
		return resprob;
	}

	public int getIdprob() {
		return idprob;
	}

	public void setIdprob(int idprob) {
		this.idprob = idprob;
	}

	public void setResprob(String resprob) {
		this.resprob = resprob;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTextprob() {
		return textprob;
	}

	public void setTextprob(String textprob) {
		this.textprob = textprob;
	}

	public int getTypeprob() {
		return typeprob;
	}

	public void setTypeprob(int typeprob) {
		this.typeprob = typeprob;
	}

	public int getCat() {
		return cat;
	}

	public void setCat(int cat) {
		this.cat = cat;
	}



	@Override
	public int compareTo(Problem another) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //	Date date1 = sdf.parse("2009-12-31");
		try {
			if(this.typeprob>another.typeprob)
				return 1;
				else if(this.typeprob<another.typeprob)
						return -1;
				else if(this.priority>another.priority)
		return -1;
		else if(this.priority<another.priority)
				return 1;
		else if(this.date.length()< another.date.length())
			return 1;
		else if (this.date.length()> another.date.length())
			return -1;
		else if(sdf.parse(this.date).after(sdf.parse(another.date)))
					return -1;
				else if(sdf.parse(this.date).before(sdf.parse(another.date)))
					return 1;
				else
					return 0;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return 0;
		
		}
				

}
