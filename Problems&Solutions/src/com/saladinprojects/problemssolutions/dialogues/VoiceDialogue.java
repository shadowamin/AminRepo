package com.saladinprojects.problemssolutions.dialogues;

import java.util.ArrayList;

import com.saladinprojects.problemssolutions.Globals;
import com.saladinprojects.problemssolutions.R;
import com.saladinprojects.problemssolutions.SolutionsFrag;
import com.saladinprojects.problemssolutions.VoiceManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;

public class VoiceDialogue {
	AlertDialog.Builder b;
	VoiceManager vm;
	ArrayList<String> suggestedWords;
	boolean fromSol;
	EditText EditeProb;
	FragmentActivity activity;
	
	
	
	public VoiceDialogue( final VoiceManager vm,
			final ArrayList<String> suggestedWords, final boolean fromSol,
			EditText editeProb, final FragmentActivity activity) {
		super();
		this.b =  new AlertDialog.Builder(activity);
		this.vm = vm;
		this.suggestedWords = suggestedWords;
		this.fromSol = fromSol;
		EditeProb = editeProb;
		this.activity = activity;
		
		b.setCancelable(true);
		b.setNeutralButton(activity.getString(R.string.try_again),
				new DialogInterface.OnClickListener() {
			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						vm.listenToSpeech("", fromSol,EditeProb);
					}
				});
		b.setNegativeButton(activity.getString(R.string.a_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		if (suggestedWords.size() == 1) {
			b.setTitle(activity.getString(R.string.a_result));
			final String bestMatch = suggestedWords.get(0);
			b.setMessage(bestMatch);
			b.setPositiveButton(activity.getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
							if (fromSol) {
								FragManage(bestMatch);
							} else
								EditeProb.setText(bestMatch);
							// addTodoItem(bestMatch);
						}
					});
		} else {
			b.setTitle(activity.getString(R.string.a_result));
			b.setItems(suggestedWords
					.toArray(new CharSequence[suggestedWords.size()]),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
							if (fromSol) {
								FragManage(suggestedWords.get(which));
							} else
								EditeProb.setText(suggestedWords.get(which));
						}
					});
		}
		
	}
	public void showDialogue()
	{
		b.show();
	}
	private void FragManage(String text)
	{
		FragmentManager fm = activity.getSupportFragmentManager();
		Fragment frag = fm
				.findFragmentByTag(Globals.FRAG_TAG);
		if (frag != null)
			((SolutionsFrag) frag)
					.changeEdit(text);
	}

}
