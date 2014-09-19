package com.saladinprojects.problemssolutions.db;
 

import java.util.ArrayList;

import com.saladinprojects.problemssolutions.models.Categorie;
import com.saladinprojects.problemssolutions.models.Problem;
import com.saladinprojects.problemssolutions.models.Solution;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DBHandler extends SQLiteOpenHelper {
 
    private static final int DATABASE_VERSION = 1;
	private static DBHandler m_Instance = null;
    private static final String DATABASE_NAME = "pands";
 
    private static final String TABLE_PROBS = "problems";
    private static final String TABLE_SOL = "solutions";
    private static final String TABLE_CAT = "categories";
 private String[] defaultCats={"work","study","health","family","religion"};
 
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    public static DBHandler getInstance(Context ctx) {
    	m_Instance = new DBHandler(ctx);
		return m_Instance;
	}
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
    	}
    
	public SQLiteDatabase openDatabase() {
		 SQLiteDatabase db = this.getWritableDatabase();
		
		
		return db;
	}  
    

    public void AddSolution(Solution solution)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
   	 if(!tableExists(TABLE_SOL))
    {
   	  String CREATE_WORDS_TABLE = "CREATE TABLE "+TABLE_SOL+" (id INTEGER PRIMARY KEY , id_prob INTEGER , desc TEXT , note  INTEGER, date String)";
   	     db.execSQL(CREATE_WORDS_TABLE); 
    }
   	ContentValues values = new ContentValues();
   	values.put("id_prob", solution.getIdprob());
   	values.put("desc", solution.getTxtsol()); 
   	values.put("note", solution.getScore());
   	values.put("date", solution.getDate());
   	db.insert(TABLE_SOL, null, values);	
   	 
    }
    
    public  ArrayList<Solution> getSolutions(int id)
    { ArrayList<Solution> solutions=new ArrayList<Solution>();
   	 SQLiteDatabase db = this.getReadableDatabase();
    String selectQuery = "SELECT id , id_prob  , desc  , note  , date  FROM "+TABLE_SOL+" where id_prob="+id;
    try{
    Cursor cursor = db.rawQuery(selectQuery, null);
 
   if (cursor.moveToFirst()) {
   do {solutions.add(new  Solution(cursor.getInt(0),cursor.getString(2), cursor.getString(4), cursor.getInt(3), cursor.getInt(1)) );
   } while (cursor.moveToNext());
   }
   
    }catch(Exception e){
    }
    return solutions;
   } 

    public void deleteSolution(Solution solution) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_SOL,  "id = ?",
    	       new String[] { String.valueOf(solution.getId()) });
    	db.close();
    	} 
    
    public void AddCategorie(String cat)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
   	 if(!tableExists(TABLE_CAT))
    {
   	  String CREATE_WORDS_TABLE = "CREATE TABLE "+TABLE_CAT+" (id INTEGER PRIMARY KEY , title TEXT)";
   	     db.execSQL(CREATE_WORDS_TABLE); 
    }
   	ContentValues values = new ContentValues();
   	values.put("title", cat);
   	db.insert(TABLE_CAT, null, values);	
   	 
    }
    
    public ArrayList<Categorie> geCategories()
    { SQLiteDatabase db = this.getWritableDatabase();
	 if(!tableExists(TABLE_CAT))
	    {
	   	  String CREATE_WORDS_TABLE = "CREATE TABLE "+TABLE_CAT+" (id INTEGER PRIMARY KEY , title TEXT)";
	   	     db.execSQL(CREATE_WORDS_TABLE); 
	   	  for(int i=0;i<defaultCats.length;i++)
	   			AddCategorie(defaultCats[i]);
	    }
   	
    	
    	ArrayList<Categorie> cats=new ArrayList<Categorie>();
    String selectQuery = "SELECT  id, title FROM "+TABLE_CAT;
    try{
    Cursor cursor = db.rawQuery(selectQuery, null);
 
   if (cursor.moveToFirst()) {
   do {
	   cats.add(new Categorie(cursor.getInt(0), cursor.getString(1)));
   } while (cursor.moveToNext());
   }
   
    }catch(Exception e){
    }
    return cats;
   } 
    public String GetCategorie(int id)
    {if(id<0)
    	return null;
   	 SQLiteDatabase db = this.getReadableDatabase();
   	String selectQuery = "SELECT  title FROM "+TABLE_CAT+" WHERE id="+id;
   	try{
    Cursor cursor = db.rawQuery(selectQuery, null);
    cursor.moveToFirst();
    
    return cursor.getString(0);
   	}
   	catch(Exception e){
   		return null;	
   	}
   	}
    public void deleteCat(int id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_CAT,  "id = ?",
    	       new String[] { String.valueOf(id) });
    	db.close();
    	}  
    
 public void AddProblem(Problem problem)
 {SQLiteDatabase db = this.getWritableDatabase();
 if(!tableExists(TABLE_PROBS))
	 {
			 String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PROBS + "(id INTEGER PRIMARY KEY ,title TEXT , desc TEXT, id_cat INTEGER, type INTEGER, date TEXT , priority INTEGER)";
			     db.execSQL(CREATE_CONTACTS_TABLE); 
		
	 }

	ContentValues values = new ContentValues();
    values.put("title", problem.getResprob()); 
    values.put("desc", problem.getTextprob());
    values.put("id_cat", problem.getCat());
    values.put("type", problem.getTypeprob());
    values.put("date", problem.getDate());
    values.put("priority", problem.getPriority());
db.insert(TABLE_PROBS, null, values);	

 }
 public void editPriority(int id,int prio)
 {
	 SQLiteDatabase db = this.getWritableDatabase();
	 ContentValues values = new ContentValues();
	        values.put("priority", prio);
	         db.update(TABLE_PROBS, values,   "id = ?",
	                new String[] { String.valueOf(id) });
 }
 public void editProblem(Problem problem)
 {
	 SQLiteDatabase db = this.getWritableDatabase();
	 ContentValues values = new ContentValues();
	 values.put("title", problem.getResprob()); 
	    values.put("desc", problem.getTextprob());
	    values.put("id_cat", problem.getCat());
	    values.put("type", problem.getTypeprob());
	    values.put("date", problem.getDate());
	    values.put("priority", problem.getPriority());
	         db.update(TABLE_PROBS, values,   "id = ?",
	                new String[] { String.valueOf(problem.getIdprob()) });
 }
 public void EditSolution(Solution solution)
 {
 	SQLiteDatabase db = this.getWritableDatabase();

	ContentValues values = new ContentValues();
	values.put("id_prob", solution.getIdprob());
	values.put("desc", solution.getTxtsol()); 
	values.put("note", solution.getScore());
	values.put("date", solution.getDate());
	db.update(TABLE_SOL, values,   "id = ?",
            new String[] { String.valueOf(solution.getId()) });
	 
 }
 public void editType(int id,int type)
 {
	 SQLiteDatabase db = this.getWritableDatabase();
	 ContentValues values = new ContentValues();
	        values.put("type", type);
	         db.update(TABLE_PROBS, values,   "id = ?",
	                new String[] { String.valueOf(id) });
 }
 public void editDate(int id,String date)
 {
	 SQLiteDatabase db = this.getWritableDatabase();
	 ContentValues values = new ContentValues();
	        values.put("date", date);
	         db.update(TABLE_PROBS, values,   "id = ?",
	                new String[] { String.valueOf(id) });
 }
 public void editCat(int id,int cat)
 {
	 SQLiteDatabase db = this.getWritableDatabase();
	 ContentValues values = new ContentValues();
	        values.put("id_cat", cat);
	         db.update(TABLE_PROBS, values,   "id = ?",
	                new String[] { String.valueOf(id) });
 }
 
 public ArrayList<Problem> getProblemes()
 { ArrayList<Problem> problems=new ArrayList<Problem>();
	 SQLiteDatabase db = this.getReadableDatabase();
 String selectQuery = "SELECT  id, title, desc, id_cat, type, date, priority FROM "+TABLE_PROBS+"";
 try{
 Cursor cursor = db.rawQuery(selectQuery, null);


if (cursor.moveToFirst()) {
do {
problems.add(new Problem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getInt(3), cursor.getString(5), cursor.getInt(6)));
 
} while (cursor.moveToNext());
}
 }catch(Exception e){
 }
 return problems;
}
 
public void deleteProblem(int id) {
SQLiteDatabase db = this.getWritableDatabase();
if(!tableExists(TABLE_SOL))
{
	  String CREATE_WORDS_TABLE = "CREATE TABLE "+TABLE_SOL+" (id INTEGER PRIMARY KEY , id_prob INTEGER , desc TEXT , note  INTEGER, date String)";
	     db.execSQL(CREATE_WORDS_TABLE); 
}
db.delete(TABLE_SOL,  "id_prob = ?",
	       new String[] { String.valueOf(id) });
db.delete(TABLE_PROBS,  "id = ?",
       new String[] { String.valueOf(id) });
db.close();
} 
 
	public boolean tableExists(String tableName) {
		 SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.query("sqlite_master", new String[] {"name"}, "name = '" + tableName + "' AND type = 'table'", null, null, null, null);
		boolean result = cur.moveToFirst();
		cur.close();
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
 

//     
//    // Getting All Contacts
//    public List<Contact> getAllContacts() {
//        List<Contact> contactList = new ArrayList<Contact>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Contact contact = new Contact();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setName(cursor.getString(1));
//                contact.setPhoneNumber(cursor.getString(2));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
// 
//        // return contact list
//        return contactList;
//    }
// 
//    // Updating single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
// 
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }
// 
//    // Deleting single contact
//    public void deleteContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }
// 
// 
//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
// 
//        // return count
//        return cursor.getCount();
//    }
// 
}