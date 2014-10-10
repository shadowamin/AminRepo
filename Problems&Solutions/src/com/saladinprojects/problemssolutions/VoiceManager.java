package com.saladinprojects.problemssolutions;

import java.util.ArrayList;

import com.saladinprojects.problemssolutions.dialogues.VoiceDialogue;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;

public class VoiceManager {
boolean fromSol;
EditText editeProb;
private static final int VR_REQUEST = 999;
FragmentActivity activity;
private int MY_DATA_CHECK_CODE = 0;
private TextToSpeech repeatTTS;
	
	public VoiceManager(boolean fromSol, 
		FragmentActivity activity) {
	super();
	this.fromSol = fromSol;
	
	this.activity = activity;
}

	public void listenToSpeech(String lang, boolean froms,EditText editeProb) {
		fromSol = froms;
		this.editeProb = editeProb;
		Intent listenIntent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				getClass().getPackage().getName());
		listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		listenIntent.putExtra(
				RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		activity.startActivityForResult(listenIntent, VR_REQUEST);
	}
	
	public void ActivityResultManager(int requestCode, int resultCode, Intent data)
	{
	if (requestCode == VR_REQUEST && resultCode == activity.RESULT_OK) {
		final ArrayList<String> suggestedWords = data
				.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		
(new VoiceDialogue( this,suggestedWords, fromSol,editeProb, activity)).showDialogue();
	}

	if (requestCode == MY_DATA_CHECK_CODE) {
		if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			repeatTTS = new TextToSpeech(activity, (OnInitListener) activity);
		else {
			Intent installTTSIntent = new Intent();
			installTTSIntent
					.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
			activity.startActivity(installTTSIntent);
		}
	}
	}
}
