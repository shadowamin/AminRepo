package com.saladinprojects.problemssolutions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.saladinprojects.problemssolutions.adapters.ExpandListAdapter;
import com.saladinprojects.problemssolutions.db.DBHandler;
import com.saladinprojects.problemssolutions.models.Categorie;
import com.saladinprojects.problemssolutions.models.GroupeProbs;
import com.saladinprojects.problemssolutions.models.Problem;
import com.saladinprojects.problemssolutions.models.ViewHolder;

public class MainActivity extends FragmentActivity  implements
AdapterView.OnItemClickListener , OnClickListener{
	DBHandler db;
  ListView listProblems;
  EditText Search;
  EditText EditeProb;
  Drawable originalDrawable;
  boolean fromSol=false;
  private ArrayList<Problem> Problems;
  private ArrayList<Problem> Filtered;
  private ExpandListAdapter ExpAdapter;
	private ArrayList<GroupeProbs> ExpListItems;
	private ExpandableListView ExpandList;
  private View btnAddProblem;
  public static final String FRAG_TAG_SETTINGS = "settings";
  public static final String FRAG_TAG = "frag";
  private TextView ModeProb;
  boolean withcat=true;
  private ImageView priority;
  private ImageView date;
  private ImageView cat;
  private int isprio=0;
  private LinearLayout addproblemLayout;
  private LinearLayout OptionsLayout;
  private RelativeLayout SearchLayout;
  private String thedate;
  private int categorie=-1;
  Calendar myCalendar;
  boolean datechoose;
  View PressedView;
  boolean fromList;
  ImageView SettingBtn;
  int choice;
  private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech repeatTTS; 
  ImageView dateItemimg;
  TextView datetext;
  Problem ItemClicked;
  boolean editprob;
	private static final int VR_REQUEST = 999;
  int mode=-1;
  DatePickerDialog.OnDateSetListener datep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
       
        db = new DBHandler(this);
        
        btnAddProblem=(View) findViewById(R.id.AddP);
//        btnSave=(Button) findViewById(R.id.SaveBtn);
//        btnCancel=(Button) findViewById(R.id.CancelBtn);
        SearchLayout=(RelativeLayout) findViewById(R.id.SearchLayout);
        OptionsLayout=(LinearLayout) findViewById(R.id.layoutOptions);
        addproblemLayout=(LinearLayout) findViewById(R.id.addproblayout);
   //     listProblems=(ListView) findViewById(R.id.ProblemsList);
        
        ExpandList = (ExpandableListView) findViewById(R.id.ProblemsList);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       
        
        priority=(ImageView) findViewById(R.id.imgWarning);
        EditeProb=(EditText) findViewById(R.id.editProb);
        date=(ImageView) findViewById(R.id.imgDate);
        cat=(ImageView) findViewById(R.id.imgCat);
        Search=(EditText) findViewById(R.id.SearchField);
        SettingBtn=(ImageView) findViewById(R.id.settingBtn);
        ModeProb=(TextView) findViewById(R.id.ModeProb);
        Problems=db.getProblemes();
	    Filtered=db.getProblemes();
       reloadList();
       
        originalDrawable = EditeProb.getBackground();
       ExpandList.setOnChildClickListener(
    		   new OnChildClickListener() {
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			click_item(v, groupPosition,  childPosition, true);	
			return false;
		}
	});
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
		Fragment frags = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
		if (frags == null) {
			ft.add(R.id.layoutMainSettings, new SettingsFrag(), FRAG_TAG_SETTINGS);
			ft.commit();
		}
	
        ModeProb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showModeDialog();
				
			}
		});
        
        Search.addTextChangedListener(new TextWatcher() {
        	
        	@Override
        	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        		
        		Filtered.clear();
        		for(int i=0;i<Problems.size();i++)
        		{
        				if(Problems.get(i).getResprob().toLowerCase().contains(arg0.toString().toLowerCase()))
        					Filtered.add(Problems.get(i));	
        		}
        		reloadList();
        		
        	}
        	
        	@Override
        	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
        			int arg3) {
        	}
        	
        	@Override
        	public void afterTextChanged(Editable arg0) {

        	}
        });
     
        
        
        
        myCalendar = Calendar.getInstance();
        datep = new DatePickerDialog.OnDateSetListener() {
        	
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
            	if(datechoose)
                	  {
                	  date.setImageResource(R.drawable.calendaron);
                	  myCalendar.set(Calendar.YEAR, year);
                      myCalendar.set(Calendar.MONTH, monthOfYear);
                      myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                      String myFormat = "yyyy-MM-dd"; //In which you need put here
         		     SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
         		    thedate=sdf.format(myCalendar.getTime());
         		    if(fromList)
         		    	{
         		    	dateItemimg.setImageResource(R.drawable.calendaron);
         		    	db.editDate(ItemClicked.getIdprob(), thedate);
         		    	datetext.setText(thedate);
         		    	ItemClicked.setDate(thedate);
         		    	} } 
            }
            
        };
        
        
    }
    
    
  //voice problem  
	  public void listenToSpeech(String lang , boolean froms) {
		  fromSol=froms;
			Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
			listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
			listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); 
			listenIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		startActivityForResult(listenIntent, VR_REQUEST);
	    }
	  
	   @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == VR_REQUEST && resultCode == RESULT_OK) 
	        {
	            final ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	         
	            final AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setCancelable(true);
				b.setNeutralButton("try again beash", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listenToSpeech("",fromSol);
					}
				});
				b.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				if (suggestedWords.size() == 1) {
					b.setTitle("bla bla");
					final String bestMatch = suggestedWords.get(0);
					b.setMessage(bestMatch);
					b.setPositiveButton("yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if(fromSol)
							{
								FragmentManager fm = getSupportFragmentManager();
								Fragment frag = fm.findFragmentByTag(FRAG_TAG);
								if (frag != null)
										((SolutionsFrag)frag).changeEdit(bestMatch);
							}
							else
								EditeProb.setText(bestMatch);
							//addTodoItem(bestMatch);
						}
					});
				} else {
					b.setTitle("title");
//					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches);
//					b.setAdapter(adapter, new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							addTodoItem(matches.get(which));
//						}
//					});
					b.setItems(suggestedWords.toArray(new CharSequence[suggestedWords.size()]), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							EditeProb.setText(suggestedWords.get(which));
						}
					});
				}
				b.show();
	          
	        }
	       
	        if (requestCode == MY_DATA_CHECK_CODE) 
	        {  
		        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)  
		        	repeatTTS = new TextToSpeech(this, (OnInitListener) this);   
		        else 
		        {  
		        	Intent installTTSIntent = new Intent();  
		        	installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);  
		        	startActivity(installTTSIntent);  
		        }  
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	  
   
    public void itemClicked(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked())
        	withcat=true;
        else
        	withcat=false;
        reloadList();
    	FragmentManager fm = getSupportFragmentManager();
		Fragment frag = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
		if (frag != null)
				((SettingsFrag)frag).setSettingsVisibility(false);
    }
    public void hideMenu()
    {
    	FragmentManager fm = getSupportFragmentManager();
		Fragment frag = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
		if (frag != null)
				((SettingsFrag)frag).setSettingsVisibility(false);
    }
    
    class CustomListener implements View.OnClickListener {
        private final AlertDialog dialog;
        private final EditText userInput;
        public CustomListener(AlertDialog dialog,EditText userInput) {
            this.dialog = dialog;
            this.userInput=userInput;
        }
        @Override
        public void onClick(View v) {
        	if(userInput.getText().length()==0)
			{  	userInput.setBackgroundResource(R.drawable.edit_warning);
				Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
				userInput.startAnimation(shake);
			
			}
	    	else
	    	{
	    		db.AddCategorie(userInput.getText().toString());
	    		Toast.makeText(getApplicationContext(), "New Categorie added", Toast.LENGTH_SHORT).show();
	    		dialog.dismiss();
	    	}
	    	hideMenu();
        }
    }
    public void AddCat(View v)
    {
    	LayoutInflater li = LayoutInflater.from(this);
    	View promptsView = li.inflate(R.layout.prompts, null);

    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
    			this);

    	alertDialogBuilder.setView(promptsView);

    	final EditText userInput = (EditText) promptsView
    			.findViewById(R.id.editTextDialogUserInput);

    	alertDialogBuilder
    		.setCancelable(false)
    		.setPositiveButton("ADD",
    		  new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog,int id) {
    		    	
    		    }
    		  })
    		.setNegativeButton("Cancel",
    		  new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog,int id) {
    			dialog.cancel();
    			hideMenu();
    		    }
    		  });
    	AlertDialog alertDialog = alertDialogBuilder.create();
    	alertDialog.show();
    	Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
    	theButton.setOnClickListener(new CustomListener(alertDialog,userInput));
    }
    

    
    @SuppressLint("NewApi") public void click_item(View view, int  groupPosition, int childPosition,boolean withcat)
    {
    	addproblemLayout.setVisibility(View.GONE);
    	btnAddProblem.setEnabled(true);
		if(PressedView==view)
		{ 
			ViewHolder holder= (ViewHolder) PressedView.getTag();
			holder.LayoutInfo.setVisibility(View.GONE);
			holder.Arrow.setVisibility(View.GONE);
			PressedView=null;
			reloadList();
			return;
		}
		if(withcat)
		{ArrayList<Problem> chList = ExpListItems.get(groupPosition).getItems();
		ItemClicked = (Problem)chList.get(childPosition);
		}
		else
		{
			ItemClicked=(Problem) Filtered.get(childPosition);
		}
		isprio=ItemClicked.getPriority();
		categorie=ItemClicked.getCat();
		
		if(PressedView!=null)
		{
			ViewHolder holder= (ViewHolder) PressedView.getTag();
			holder.LayoutInfo.setVisibility(View.GONE);
			holder.Arrow.setVisibility(View.GONE);
		}
		final ViewHolder holder2= (ViewHolder) view.getTag();
		holder2.titre.setMaxLines(Integer.MAX_VALUE);
		holder2.LayoutInfo.setVisibility(View.VISIBLE);
		holder2.Arrow.setVisibility(View.VISIBLE);
		PressedView=view;
		holder2.Priority.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isprio==1)
				{	holder2.Priority.setImageResource(R.drawable.nopriority);
				
				isprio=0;
				}else
				{	holder2.Priority.setImageResource(R.drawable.priority);
				isprio=1;
				Toast.makeText(getApplicationContext(), "Priority Set", Toast.LENGTH_SHORT).show();
				}
				db.editPriority(ItemClicked.getIdprob(), isprio);
				ItemClicked.setPriority(isprio);
			}
		});
		
	holder2.Arrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
				Fragment frag = fm.findFragmentByTag(FRAG_TAG);
				if (frag == null ) {
					SolutionsFrag sf=new SolutionsFrag(ItemClicked);
					ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
					ft.add(R.id.layoutMainSolution, sf,FRAG_TAG);
					ft.commit();
			}
			}
		});
		
		holder2.Cat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fromList=true;
				showDialog(holder2.Cat);
			}
		});

holder2.EditImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addproblemLayout.setVisibility(View.VISIBLE);
				SearchLayout.setVisibility(View.GONE);
				btnAddProblem.setEnabled(false);
				EditeProb.setBackground(originalDrawable);
				EditeProb.setText(""+ItemClicked.getResprob());
				OptionsLayout.setVisibility(View.GONE);
				editprob=true;
				fromList=false;
			}
		});
		


		holder2.DelImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);
		 
					alertDialogBuilder.setTitle("Delete Problem");
		 
					alertDialogBuilder
						.setMessage("This will delete problem and all it's solutions")
						.setCancelable(false)
						.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								db.deleteProblem(ItemClicked.getIdprob());
								Problems=db.getProblemes();
							    Filtered=db.getProblemes();
								reloadList();
							}
						  })
						.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
		 
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						alertDialog.show();
					}
			
		});
		
		holder2.DateImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				datetext=holder2.Date;
				fromList=true;
				dateItemimg=holder2.DateImage;
				showCalander(holder2.DateImage);
			}
		});	
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		click_item(view,  0,  position, false);			 
	}

	

	


	public void RemoveFrag()
	{
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
		Fragment frag = fm.findFragmentByTag(FRAG_TAG);
		if (frag != null ) {
			ft.remove(frag);
			ft.commit();
	}
	}
	private void reloadList()
	{
		
		ExpListItems= new ArrayList<GroupeProbs>();
		 ArrayList<Problem> list2 = new ArrayList<Problem>();
		 GroupeProbs gp=null;
		
	    Collections.sort(Filtered);
	   
		  
		   ArrayList<Problem>  Filtered2=new ArrayList<Problem>();
		   ArrayList<Categorie> cats=db.geCategories();
		   for(int i=0;i<cats.size();i++)
		   for(int j=0;j<Filtered.size();j++)
			  if(Filtered.get(j).getCat()==cats.get(i).getId())
				  Filtered2.add(Filtered.get(j));
		   for(int j=0;j<Filtered.size();j++)
				  if(db.GetCategorie(Filtered.get(j).getCat())==null)
					  { Filtered.get(j).setCat(-1);
					  Filtered2.add(Filtered.get(j));
					 }
		   
		   if(withcat)
		   {
		   for (int i = 0; i < Filtered2.size(); i++) {
			   if(i==0 || Filtered2.get(i).getCat()!= Filtered2.get(i-1).getCat())
			   { if(gp!=null)
				   {gp.setItems(list2);
				   ExpListItems.add(gp);
				   }
			   		gp=new GroupeProbs();
			   		if(Filtered2.get(i).getCat()<0)
						   gp.setName("Others");
			   		else
				   gp.setName(db.GetCategorie(Filtered2.get(i).getCat()));
				   list2 = new ArrayList<Problem>();
				   list2.add(Filtered2.get(i));
				 
			   }
			   else
				   list2.add(Filtered2.get(i)); 
			}	
		   if(gp!=null)
		   {gp.setItems(list2);
		   ExpListItems.add(gp);
		   }
		   }
		   else
		   {Collections.sort(Filtered2);
			for (int i = 0; i < Filtered2.size(); i++) 
			list2.add(Filtered2.get(i)); 
				
			   gp=new GroupeProbs();
			   gp.setName("All");
			   gp.setItems(list2);
			   ExpListItems.add(gp);
			}
		   
		   ExpAdapter = new ExpandListAdapter(MainActivity.this, ExpListItems);
	        ExpandList.setAdapter(ExpAdapter);
	        for(int i=0;i<ExpListItems.size();i++)
	        ExpandList.expandGroup(i);
		   Filtered=Filtered2;   
	}
	
	
	public void showModeDialog()
	{
		final CharSequence[] items={"ALL","Note Done","Done"} ;
	
		final AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Problemes");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mode=choice-1;
				Filtered.clear();
        		
        			switch(mode)
        			{
        			case -1:
        				ModeProb.setText("ALL");
        				for(int i=0;i<Problems.size();i++)
        				Filtered.add(Problems.get(i));
        				break;
        			case 0:
        				ModeProb.setText("Not Done");
        				for(int i=0;i<Problems.size();i++)
        				if(Problems.get(i).getTypeprob()==0)
        				Filtered.add(Problems.get(i));	
        				
        				break;
        				
        			case 1:
        				ModeProb.setText("Done");
        				for(int i=0;i<Problems.size();i++)
        				if(Problems.get(i).getTypeprob()==1)
        				Filtered.add(Problems.get(i));	
        				break;
        			}
        			 reloadList();
				
			}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {}
			});
	 	builder.setSingleChoiceItems(items,mode+1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				choice=which;
				
     		 //   	dialog.dismiss();
			}
		});
		builder.show();

	}
	public void showDeleteDialog(View v)
	{
		ArrayList<Categorie> cats=db.geCategories();
		final HashMap<Integer,Categorie> CatKeys=new HashMap<Integer,Categorie>();
		final CharSequence[] items=new CharSequence[cats.size()] ;
		for(int i=0;i<cats.size();i++)
			{items[i]=cats.get(i).getTitle();
			CatKeys.put(i,cats.get(i));
			}
		final ArrayList<Integer> selList=new ArrayList();
		final AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Choose a category to delete");
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				hideMenu();
				for(int i=0;i<selList.size();i++)
				db.deleteCat(CatKeys.get(selList.get(i)).getId());
			
			Toast.makeText(getApplicationContext(), selList.size()+" Categories Deleted", Toast.LENGTH_SHORT).show();
			 Problems=db.getProblemes();
			    Filtered=db.getProblemes();
			    reloadList();
			}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				hideMenu();
			}
			});
			
			builder.setMultiChoiceItems(items, new boolean[items.length],new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					   if(isChecked)
					   selList.add(which);
					    else if (selList.contains(which))
					    selList.remove(Integer.valueOf(which));
				}
			} );
	 
		builder.show();

	}
	public void showDialog(final ImageView thecat)
	{
		ArrayList<Categorie> cats=db.geCategories();
		final HashMap<Integer,Categorie> CatKeys=new HashMap<Integer,Categorie>();
		final CharSequence[] items=new CharSequence[cats.size()+1] ;
		items[0]="Others";
		int indexcat=0;
		
		for(int i=0;i<cats.size();i++)
			{items[i+1]=cats.get(i).getTitle();
			CatKeys.put(i+1,cats.get(i));
			if((cats.get(i).getId()==categorie))
				indexcat=i+1;
			}
		
		final AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Choose a category");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
			//	Toast.makeText(getApplicationContext(), ""+id, Toast.LENGTH_SHORT).show();
				if(CatKeys.get(choice)==null)
					{thecat.setImageResource(R.drawable.tagoff);
					categorie=-1;
					}else
					{thecat.setImageResource(R.drawable.tagon);
					categorie=CatKeys.get(choice).getId();
					}
				if(fromList)
     		    	{
					db.editCat(ItemClicked.getIdprob(), categorie);
     		    	ItemClicked.setCat(categorie);
     		    	}
			}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

			}
			});
			
	 	builder.setSingleChoiceItems(items,indexcat, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				choice=which;
				
     		 //   	dialog.dismiss();
			}
		});
		builder.show();

	}
	private void showCalander(final ImageView dateimg)
	{
		 DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, datep, myCalendar
	                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                myCalendar.get(Calendar.DAY_OF_MONTH));
		 datechoose=true;

	  dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	       if (which == DialogInterface.BUTTON_NEUTRAL) {
	    	   datechoose=false;
	       }
	    }
	  });
	  dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No Date", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		       if (which == DialogInterface.BUTTON_NEGATIVE) {
		    	   datechoose=false;
		    	   dateimg.setImageResource(R.drawable.calendaroff);
		    	   thedate="";
		    	   if(fromList)
    		    	{db.editDate(ItemClicked.getIdprob(), thedate);
    		    	datetext.setText(thedate);
    		    	ItemClicked.setDate("");
    		    	}
		       }
		    }
		  });
	  dialog.show();
		
	}
	public void hideKeybord(EditText edt)
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(
				MainActivity.this.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
	}
	public void showKeyborad(final EditText ettext)
	{
		ettext.requestFocus();
        ettext.postDelayed(new Runnable() {
           @Override
           public void run() {
               InputMethodManager keyboard = (InputMethodManager)
               getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
               keyboard.showSoftInput(ettext, 0);
           }
       },200);
	}
	
	
	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
	switch(v.getId())
	{
	case R.id.settingBtn:
		FragmentManager fm = getSupportFragmentManager();
		Fragment frag = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
		if (frag != null)
		{
			//Toast.makeText(getApplicationContext(), ""+((SettingsFrag)frag).isSettingVisible(), Toast.LENGTH_SHORT).show();
			if(((SettingsFrag)frag).isSettingVisible())
				((SettingsFrag)frag).setSettingsVisibility(true);
			else
				((SettingsFrag)frag).setSettingsVisibility(false);	
		}
		
		
		break;
	case R.id.AddP:
		addproblemLayout.setVisibility(View.VISIBLE);
		SearchLayout.setVisibility(View.GONE);
		OptionsLayout.setVisibility(View.VISIBLE);
		btnAddProblem.setEnabled(false);
		priority.setImageResource(R.drawable.nopriority);
		date.setImageResource(R.drawable.calendaroff);
		EditeProb.setBackground(originalDrawable);
		cat.setImageResource(R.drawable.tagoff);
		EditeProb.setText("");
		categorie=-1;
		thedate="";
		isprio=0;
		fromList=false;
		editprob=false;
		showKeyborad(EditeProb);
		break;
	case R.id.searchBtn:
		if(EditeProb.getText().length()==0)
		{
			SearchLayout.setVisibility(View.VISIBLE);
		addproblemLayout.setVisibility(View.GONE);
		btnAddProblem.setEnabled(true);
		showKeyborad(Search);
		}
		break;
	case R.id.CancelSearch:
		SearchLayout.setVisibility(View.GONE);
		Search.setText("");
		Problems=db.getProblemes();
	    Filtered=db.getProblemes();
	    hideKeybord(Search);
	    btnAddProblem.setEnabled(true);
		reloadList();
		break;
	case R.id.imgWarning:
		if(isprio==1)
		{	priority.setImageResource(R.drawable.nopriority);
		isprio=0;
		
		}else
		{	priority.setImageResource(R.drawable.priority);
		Toast.makeText(getApplicationContext(), "Priority Set", Toast.LENGTH_SHORT).show();
		isprio=1;
		}break;
	case R.id.imgDate:
		showCalander(date);
	  
	break;
	case R.id.imgCat:
	
		showDialog(cat);
		break;
	case R.id.SaveBtn:
		if(EditeProb.getText().length()==0)
		{  	EditeProb.setBackgroundResource(R.drawable.edit_warning);
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		EditeProb.startAnimation(shake);
		
		}
		else
		{
		
			EditeProb.setBackgroundResource(R.drawable.edit_text);
		if(editprob)
		{if(ItemClicked!=null)
			{ItemClicked.setResprob(EditeProb.getText().toString());
			db.editProblem(ItemClicked);}
		}else
		db.AddProblem(new Problem(0,""+EditeProb.getText(), "", 0, categorie, thedate, isprio));
		
		
		Problems=db.getProblemes();
	    Filtered=db.getProblemes();
		reloadList();
		addproblemLayout.setVisibility(View.GONE);	
		btnAddProblem.setEnabled(true);
		EditeProb.setText("");
		hideKeybord(EditeProb);
		}
		break;
	case R.id.CancelBtn:
		addproblemLayout.setVisibility(View.GONE);	
		btnAddProblem.setEnabled(true);
		EditeProb.setText("");
		hideKeybord(EditeProb);
		break;
//	case R.id.ModeProb:
	case R.id.ArrowImg:
	showModeDialog();
		break;
	case R.id.imageBack:
		RemoveFrag();
		Problems=db.getProblemes();
	    Filtered=db.getProblemes();
		reloadList();
		break;
	case R.id.Voice:
		 listenToSpeech("",false);
		break;
	
	}
		
	}

}
