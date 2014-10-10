package com.saladinprojects.problemssolutions.dialogues;



import com.saladinprojects.problemssolutions.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.FragmentActivity;

public class DeleteDialogue {

	public void Show(OnClickListener dialogueDelete ,FragmentActivity activity) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				activity);

		alertDialogBuilder.setTitle(activity.getString(R.string.delete_prob));

		alertDialogBuilder
				.setMessage(
						"")
				.setCancelable(false)
				.setPositiveButton(activity.getString(R.string.a_delete),
						dialogueDelete)
				.setNegativeButton(activity.getString(R.string.a_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}
	
}
