package com.saladinprojects.problemssolutions;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saladinprojects.problemssolutions.adapters.SolutionsAdapater;
import com.saladinprojects.problemssolutions.db.DBHandler;
import com.saladinprojects.problemssolutions.models.Problem;
import com.saladinprojects.problemssolutions.models.Solution;
import com.saladinprojects.problemssolutions.models.ViewHolder2;



@SuppressLint({ "ValidFragment", "NewApi" }) public class SolutionsFrag extends Fragment implements OnClickListener, AdapterView.OnItemClickListener{
	TextView Categorie;
	ImageView Doneprob;
	ImageView VoiceSol;
	View Priority;
	TextView Date;
	ImageView AddSol;
	Button SaveBtn;
	Button CancelBtn;
	ListView SolutionsList;
	TextView TheProblem;
	EditText NewSolution;
	Problem problem;
	Drawable originalDrawable;
	DBHandler db;
	LinearLayout addSolLay;
	 View PressedView;
	 Solution ItemClicked;
	SolutionsAdapater Sol_Adapter;
	boolean editsol;
	  private ArrayList<Solution> Solutions;
	  private ArrayList<Solution> FilteredS;
	
	public SolutionsFrag(Problem problem) {
		super();
		this.problem = problem;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	View result = inflater.inflate(R.layout.solutionsfrag, container, false);
//init
	Categorie = (TextView) result.findViewById(R.id.CategorieName);
	TheProblem = (TextView) result.findViewById(R.id.Probemtext);
	Doneprob=(ImageView) result.findViewById(R.id.DoneProb);
	VoiceSol=(ImageView) result.findViewById(R.id.VoiceSol);
	Priority=(View) result.findViewById(R.id.prio);
	Date=(TextView) result.findViewById(R.id.textDate);
	AddSol=(ImageView) result.findViewById(R.id.AddSol);
	SaveBtn=(Button) result.findViewById(R.id.SaveSol);
	CancelBtn=(Button) result.findViewById(R.id.CancelSol);
	SolutionsList=(ListView) result.findViewById(R.id.SolutionsList);
	NewSolution=(EditText) result.findViewById(R.id.editSolution);
	addSolLay=(LinearLayout) result.findViewById(R.id.addSollayout);
	Doneprob.setOnClickListener(this);
	CancelBtn.setOnClickListener(this);
	AddSol.setOnClickListener(this);
	SaveBtn.setOnClickListener(this);
	VoiceSol.setOnClickListener(this);
	
	db = new DBHandler((MainActivity)getActivity());
	  originalDrawable = NewSolution.getBackground();
	  
	SolutionsList.setOnItemClickListener(this);
	Solutions=db.getSolutions(problem.getIdprob());
    FilteredS=db.getSolutions(problem.getIdprob());
	reloadSolList();
	
		 TheProblem.setText(""+problem.getResprob());
		 Categorie.setText(""+db.GetCategorie(problem.getCat()));
		 if(problem.getPriority()==0)
			 Priority.setVisibility(View.GONE);
			else
				Priority.setVisibility(View.VISIBLE);
	
		 Date.setText(""+problem.getDate());
		 
		 if(problem.getTypeprob()==0)
			 Doneprob.setImageResource(R.drawable.notedone);
			else
				Doneprob.setImageResource(R.drawable.done);
		 
		 return result;
	}

	private void reloadSolList()
	{
		Sol_Adapter = new SolutionsAdapater((MainActivity)getActivity(), FilteredS);
		SolutionsList.setAdapter(Sol_Adapter);	
	}
	
	public void AlertMessage(final boolean done)
	{String Msg1;
	String Msg2;
	final int drawable;
		if(done)
		{Msg1="Save as resolved";
		Msg2="This will save the problem as resolved";
		drawable=R.drawable.done;
		}
		else
		{Msg1="Save as not resolved";
		Msg2="This will save the problem as not resolved";
		drawable=R.drawable.notedone;
		}
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				(MainActivity)getActivity());

			alertDialogBuilder.setTitle(Msg1);

	 
			alertDialogBuilder
				.setMessage(Msg2)
				.setCancelable(false)
				.setPositiveButton("Save",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					Doneprob.setImageResource(drawable);
					if(done)
					{	db.editType(problem.getIdprob(), 1);
					problem.setTypeprob(1);
					}else
					{	db.editType(problem.getIdprob(), 0);	
					problem.setTypeprob(0);
					}}
				  })
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
 
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				alertDialog.show();
	}
public void changeEdit(String sentence)
{
	if(NewSolution!=null)
	{
		NewSolution.setText(sentence);
	}
}
	@Override
	public void onClick(View v) {
	switch(v.getId())
	{
	case R.id.AddSol:
		addSolLay.setVisibility(View.VISIBLE);
		AddSol.setEnabled(false);
		NewSolution.setBackground(originalDrawable);
		NewSolution.setText("");
		editsol=false;
		((MainActivity)getActivity()).showKeyborad(NewSolution);
		break;
	case R.id.CancelSol:
		addSolLay.setVisibility(View.GONE);
		AddSol.setEnabled(true);
		((MainActivity)getActivity()).hideKeybord(NewSolution);
		break;
	case R.id.DoneProb:
		if(problem.getTypeprob()==0)
			AlertMessage(true);
		else
			AlertMessage(false);
	break;
	case R.id.VoiceSol:
		((MainActivity)getActivity()).listenToSpeech("",true);
		 break;
	case R.id.SaveSol:
		if(NewSolution.getText().length()==0)
		{  	NewSolution.setBackgroundResource(R.drawable.edit_warning);
			Animation shake = AnimationUtils.loadAnimation((MainActivity)getActivity(), R.anim.shake);
			NewSolution.startAnimation(shake);
		
		}
		else
		{
			NewSolution.setBackgroundResource(R.drawable.edit_text);
			AddSol.setEnabled(true);
			if(editsol)
			{if(ItemClicked!=null)
			{	ItemClicked.setTxtsol(NewSolution.getText().toString());
			
				db.EditSolution(ItemClicked);
			
			}
			}
			else
			{
				db.AddSolution(new Solution(0,NewSolution.getText().toString(),"",5,problem.getIdprob()));	
				problem.setTypeprob(1);
				db.editType(problem.getIdprob(), 1);
				Doneprob.setImageResource(R.drawable.done);
			}
			Solutions=db.getSolutions(problem.getIdprob());
		    FilteredS=db.getSolutions(problem.getIdprob());
			reloadSolList();
			
		addSolLay.setVisibility(View.GONE);	
		}
		break;
	}
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		addSolLay.setVisibility(View.GONE);	
		if(PressedView==view)
		{ 
			ViewHolder2 holder= (ViewHolder2) PressedView.getTag();
			holder.LayoutInfo.setVisibility(View.GONE);
			PressedView=null;
			reloadSolList();
			return;
		}
		
		  ItemClicked = (Solution) FilteredS.get(position);
		if(PressedView!=null)
		{
			ViewHolder2 holder= (ViewHolder2) PressedView.getTag();
			holder.LayoutInfo.setVisibility(View.GONE);
			
		}
		final ViewHolder2 holder2= (ViewHolder2) view.getTag();
		holder2.titre.setMaxLines(Integer.MAX_VALUE);
		holder2.LayoutInfo.setVisibility(View.VISIBLE);
		PressedView=view;
		holder2.DelImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						(MainActivity)getActivity());
		 
					alertDialogBuilder.setTitle("Delete Problem");
		 
					alertDialogBuilder
						.setMessage("This will delete the solution")
						.setCancelable(false)
						.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								db.deleteSolution(ItemClicked);
								Solutions=db.getSolutions(problem.getIdprob());
							    FilteredS=db.getSolutions(problem.getIdprob());
								reloadSolList();
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
		
		holder2.EditSol.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				addSolLay.setVisibility(View.VISIBLE);
				AddSol.setEnabled(false);
						
				NewSolution.setBackgroundResource(R.drawable.edit_text);
				NewSolution.setText(""+ItemClicked.getTxtsol());
						editsol=true;
				
			}
		});
		
	}

	
}
