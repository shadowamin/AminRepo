package com.saladinprojects.problemssolutions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.saladinprojects.problemssolutions.adapters.ExpandListAdapter;
import com.saladinprojects.problemssolutions.db.DBHandler;
import com.saladinprojects.problemssolutions.dialogues.DeleteDialogue;
import com.saladinprojects.problemssolutions.models.Categorie;
import com.saladinprojects.problemssolutions.models.GroupeProbs;
import com.saladinprojects.problemssolutions.models.Problem;
import com.saladinprojects.problemssolutions.models.ViewHolder;

public class MainActivity extends FragmentActivity implements
		AdapterView.OnItemClickListener, OnClickListener {
	DBHandler db;
	private EditText Search;
	private EditText EditeProb;
	private ArrayList<Problem> Problems;
	private ArrayList<Problem> Filtered;
	private ExpandListAdapter ExpAdapter;
	private ArrayList<GroupeProbs> ExpListItems;
	private ExpandableListView ExpandList;
	private View btnAddProblem;
	private TextView ModeProb;
	private ImageView priority;
	private ImageView date;
	private ImageView cat;
	private LinearLayout addproblemLayout;
	private LinearLayout OptionsLayout;
	private RelativeLayout SearchLayout;
	private RelativeLayout Noprobtext;
//	private TextView MsgNoprob;
	private ImageView dateItemimg;
	private TextView datetext;
	private Problem ItemClicked;
	public static final String FRAG_TAG_SETTINGS = "settings";
	public static final String FRAG_TAG = "frag";
	boolean withcat = true;
	private int isprio = 0;
	private String thedate;
	private int categorie = -1;
	Calendar myCalendar;
	boolean datechoose;
	View PressedView;
	boolean fromList;
	int choice;
	private Drawable originalDrawable;
	boolean fromSol = false;
	boolean editprob;
	private static final int VR_REQUEST = 999;
	int mode = -1;
	boolean notifs = true;
	ServiceManager sm;
	private VoiceManager Vm;
	DatePickerDialog.OnDateSetListener datep;
	DialogInterface.OnClickListener DialogueDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		db = new DBHandler(this);

		btnAddProblem = (View) findViewById(R.id.AddP);
		SearchLayout = (RelativeLayout) findViewById(R.id.SearchLayout);
		OptionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
		addproblemLayout = (LinearLayout) findViewById(R.id.addproblayout);
		ExpandList = (ExpandableListView) findViewById(R.id.ProblemsList);
		priority = (ImageView) findViewById(R.id.imgWarning);
		EditeProb = (EditText) findViewById(R.id.editProb);
		date = (ImageView) findViewById(R.id.imgDate);
		cat = (ImageView) findViewById(R.id.imgCat);
		Search = (EditText) findViewById(R.id.SearchField);
		ModeProb = (TextView) findViewById(R.id.ModeProb);
//		MsgNoprob = (TextView) findViewById(R.id.MsgNoprob);
		Noprobtext = (RelativeLayout) findViewById(R.id.textNoProb);
		
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		Problems = db.getProblemes();
		Filtered = db.getProblemes();
if(Problems.size()==0)
Noprobtext.setVisibility(View.VISIBLE);


		sm = new ServiceManager(withcat, notifs, this);
		sm.loadSavedPreferences();
		reloadList(Filtered);
		Vm = new VoiceManager(false, this);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment frags = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
		SettingsFrag sf = new SettingsFrag();
		sf.Setchecked(withcat, notifs);
		if (frags == null) {
			ft.add(R.id.layoutMainSettings, sf, FRAG_TAG_SETTINGS);
			ft.commit();
		}

		originalDrawable = EditeProb.getBackground();
		ExpandList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				click_item(v, groupPosition, childPosition, true);
				return false;
			}
		});

		ModeProb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showModeDialog();

			}
		});

		Search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				ArrayList<Problem> filter = new ArrayList<Problem>();

				for (int i = 0; i < Filtered.size(); i++) {
					if (Filtered.get(i).getResprob().toLowerCase()
							.contains(arg0.toString().toLowerCase()))
						filter.add(Filtered.get(i));
				}
				reloadList(filter);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
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
				if (datechoose) {
					date.setImageResource(R.drawable.calendaron);
					myCalendar.set(Calendar.YEAR, year);
					myCalendar.set(Calendar.MONTH, monthOfYear);
					myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					String myFormat = "yyyy-MM-dd"; // In which you need put
													// here
					SimpleDateFormat sdf = new SimpleDateFormat(myFormat,
							Locale.US);
					thedate = sdf.format(myCalendar.getTime());
					if (fromList) {
						dateItemimg.setImageResource(R.drawable.calendaron);
						db.editDate(ItemClicked.getIdprob(), thedate);
						datetext.setText(thedate);
						ItemClicked.setDate(thedate);
					}
				}
			}

		};

		DialogueDelete = (new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				db.deleteProblem(ItemClicked.getIdprob());
				Problems = db.getProblemes();
				Filtered.clear();
				managemode();
				reloadList(Filtered);
			}
		});

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VR_REQUEST && resultCode == RESULT_OK) {
	//		Log.w("lets count the choices", ""+suggestedWords.size());
			Vm.ActivityResultManager(requestCode, resultCode, data);

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void itemClicked(View v) {
		CheckBox checkBox = (CheckBox) v;
		switch (v.getId()) {
		case R.id.checkBox1:
			if (checkBox.isChecked())
				withcat = true;
			else
				withcat = false;
			sm.savePreferences("withcat", withcat);
			reloadList(Filtered);
			FragmentManager fm = getSupportFragmentManager();
			Fragment frag = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
			if (frag != null)
				((SettingsFrag) frag).setSettingsVisibility(false);
			break;
		case R.id.checkBoxNotif:
			if (checkBox.isChecked()) {
				sm.start_service(true);
				sm.savePreferences("notifs", true);
			} else {
				sm.stop_service(true);
				sm.savePreferences("notifs", false);
			}
			break;
		}

	}

	public void hideMenu() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment frag = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
		if (frag != null)
			((SettingsFrag) frag).setSettingsVisibility(false);

	}

	class CustomListener implements View.OnClickListener {
		private final AlertDialog dialog;
		private final EditText userInput;

		public CustomListener(AlertDialog dialog, EditText userInput) {
			this.dialog = dialog;
			this.userInput = userInput;
		}

		@Override
		public void onClick(View v) {
			if (userInput.getText().length() == 0) {
				userInput.setBackgroundResource(R.drawable.edit_warning);
				Animation shake = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.shake);
				userInput.startAnimation(shake);

			} else {
				db.AddCategorie(userInput.getText().toString());
//				Toast.makeText(getApplicationContext(), "New Categorie added",
//						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
			hideMenu();
		}
	}

	public void AddCat(View v) {
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton(getString(R.string.a_save),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						})
				.setNegativeButton(getString(R.string.a_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								hideMenu();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		Button theButton = alertDialog
				.getButton(DialogInterface.BUTTON_POSITIVE);
		theButton
				.setOnClickListener(new CustomListener(alertDialog, userInput));
	}

	@SuppressLint("NewApi")
	public void click_item(View view, int groupPosition, int childPosition,
			boolean withcat) {
		addproblemLayout.setVisibility(View.GONE);
		btnAddProblem.setEnabled(true);
		ExpandList.setSelection(childPosition - groupPosition);
		if (PressedView == view) {
			ViewHolder holder = (ViewHolder) PressedView.getTag();
			holder.LayoutInfo.setVisibility(View.GONE);
			holder.Arrow.setVisibility(View.GONE);
			PressedView = null;
			reloadList(Filtered);

			return;
		}
		if (PressedView != null) {
			ViewHolder holder = (ViewHolder) PressedView.getTag();
			holder.LayoutInfo.setVisibility(View.GONE);
			holder.Arrow.setVisibility(View.GONE);
			if(ItemClicked.getPriority()==1 && ItemClicked.getTypeprob()==0)
				PressedView.setBackgroundColor(getResources().getColor(R.color.redBeck));
			holder.titre.setMaxLines(1);
		}
		
		
		if (withcat) {
			ArrayList<Problem> chList = ExpListItems.get(groupPosition)
					.getItems();
			ItemClicked = (Problem) chList.get(childPosition);
		} else {
			ItemClicked = (Problem) Filtered.get(childPosition);
		}
		
		isprio = ItemClicked.getPriority();
		categorie = ItemClicked.getCat();

		
		final ViewHolder holder2 = (ViewHolder) view.getTag();
		holder2.titre.setMaxLines(Integer.MAX_VALUE);
		holder2.LayoutInfo.setVisibility(View.VISIBLE);
		holder2.Arrow.setVisibility(View.VISIBLE);
		PressedView = view;
		view.setBackgroundColor(Color.WHITE);
		holder2.Priority.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isprio == 1) {
					holder2.Priority.setImageResource(R.drawable.nopriority);

					isprio = 0;
				} else {
					holder2.Priority.setImageResource(R.drawable.priority);
					isprio = 1;
					Toast.makeText(getApplicationContext(), "Priority Set",
							Toast.LENGTH_SHORT).show();
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
				if (frag == null) {
					SolutionsFrag sf = new SolutionsFrag(ItemClicked);
					ft.setCustomAnimations(R.anim.object_animation_in,
							R.anim.object_animation_out);

					// ft.setCustomAnimations(R.anim.enter_from_right,
					// R.anim.exit_to_left, R.anim.enter_from_left,
					// R.anim.exit_to_right);
					ft.add(R.id.layoutMainSolution, sf, FRAG_TAG);
					ft.commit();
				}
			}
		});

		holder2.Cat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fromList = true;
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
				EditeProb.setText("" + ItemClicked.getResprob());
				OptionsLayout.setVisibility(View.GONE);
				editprob = true;
				fromList = false;
			}
		});

		holder2.DelImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DeleteDialogue dh = new DeleteDialogue();
				dh.Show(DialogueDelete, MainActivity.this);
			}

		});

		holder2.DateImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datetext = holder2.Date;
				fromList = true;
				dateItemimg = holder2.DateImage;
				showCalander(holder2.DateImage);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		click_item(view, 0, position, false);
	}

	public void RemoveFrag() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment frag = fm.findFragmentByTag(FRAG_TAG);
		if (frag != null) {
			ft.remove(frag);
			ft.commit();
		}
	}

	private void reloadList(ArrayList<Problem> filtre) {

		ExpListItems = new ArrayList<GroupeProbs>();
		ArrayList<Problem> list2 = new ArrayList<Problem>();
		GroupeProbs gp = null;

		Collections.sort(filtre);

		ArrayList<Problem> Filtered2 = new ArrayList<Problem>();
		ArrayList<Categorie> cats = db.geCategories(this);
		for (int i = 0; i < cats.size(); i++)
			for (int j = 0; j < filtre.size(); j++)
				if (filtre.get(j).getCat() == cats.get(i).getId())
					Filtered2.add(filtre.get(j));
		for (int j = 0; j < filtre.size(); j++)
			if (db.GetCategorie(filtre.get(j).getCat()) == null) {
				filtre.get(j).setCat(-1);
				Filtered2.add(filtre.get(j));
			}

		if (withcat) {
			for (int i = 0; i < Filtered2.size(); i++) {
				if (i == 0
						|| Filtered2.get(i).getCat() != Filtered2.get(i - 1)
								.getCat()) {
					if (gp != null) {
						gp.setItems(list2);
						ExpListItems.add(gp);
					}
					gp = new GroupeProbs();
					if (Filtered2.get(i).getCat() < 0)
						gp.setName(getString(R.string.a_others));
					else
						gp.setName(db.GetCategorie(Filtered2.get(i).getCat()));
					list2 = new ArrayList<Problem>();
					list2.add(Filtered2.get(i));

				} else
					list2.add(Filtered2.get(i));
				if(Filtered2.get(i).getPriority()==1)
					gp.setNbprioritys(gp.getNbprioritys()+1);
			}
			if (gp != null) {
				gp.setItems(list2);
				ExpListItems.add(gp);
			}
			Collections.sort(ExpListItems);
		} else {
			Collections.sort(Filtered2);
			for (int i = 0; i < Filtered2.size(); i++)
				list2.add(Filtered2.get(i));

			gp = new GroupeProbs();
			gp.setName(getString(R.string.a_all));
			gp.setItems(list2);
			
			ExpListItems.add(gp);
			
		}

		ExpAdapter = new ExpandListAdapter(MainActivity.this, ExpListItems);
		ExpandList.setAdapter(ExpAdapter);
		for (int i = 0; i < ExpListItems.size(); i++)
			ExpandList.expandGroup(i);
		// Filtered=Filtered2;
	}

	public void showModeDialog() {
		final CharSequence[] items = { getString(R.string.a_all), getString(R.string.a_unfixed), getString(R.string.a_fixed) };

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.a_problems));
		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mode = choice - 1;
				Filtered.clear();

				switch (mode) {
				case -1:
					ModeProb.setText(getString(R.string.a_all));
					for (int i = 0; i < Problems.size(); i++)
						Filtered.add(Problems.get(i));
					break;
				case 0:
					ModeProb.setText(getString(R.string.a_unfixed));
					for (int i = 0; i < Problems.size(); i++)
						if (Problems.get(i).getTypeprob() == 0)
							Filtered.add(Problems.get(i));

					break;

				case 1:
					ModeProb.setText(getString(R.string.a_fixed));
					for (int i = 0; i < Problems.size(); i++)
						if (Problems.get(i).getTypeprob() == 1)
							Filtered.add(Problems.get(i));
					break;
				}
				reloadList(Filtered);

			}
		});
		builder.setNegativeButton(getString(R.string.a_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		builder.setSingleChoiceItems(items, mode + 1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						choice = which;

						// dialog.dismiss();
					}
				});
		builder.show();

	}

	public void showDeleteDialog(View v) {
		ArrayList<Categorie> cats = db.geCategories(this);
		final HashMap<Integer, Categorie> CatKeys = new HashMap<Integer, Categorie>();
		final CharSequence[] items = new CharSequence[cats.size()];
		for (int i = 0; i < cats.size(); i++) {
			items[i] = cats.get(i).getTitle();
			CatKeys.put(i, cats.get(i));
		}
		final ArrayList<Integer> selList = new ArrayList();
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.delete_categories));
		builder.setPositiveButton(getString(R.string.a_delete),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						hideMenu();
						for (int i = 0; i < selList.size(); i++)
							db.deleteCat(CatKeys.get(selList.get(i)).getId());

						Toast.makeText(getApplicationContext(),
								selList.size() + getString(R.string.delete_categories),
								Toast.LENGTH_SHORT).show();
						Problems = db.getProblemes();
						Filtered.clear();
						managemode();
						reloadList(Filtered);
					}
				});
		builder.setNegativeButton(getString(R.string.a_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						hideMenu();
					}
				});

		builder.setMultiChoiceItems(items, new boolean[items.length],
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						if (isChecked)
							selList.add(which);
						else if (selList.contains(which))
							selList.remove(Integer.valueOf(which));
					}
				});

		builder.show();

	}

	private void managemode() {
		switch (mode) {
		case -1:
			for (int i = 0; i < Problems.size(); i++)
				Filtered.add(Problems.get(i));
			break;
		case 0:
			for (int i = 0; i < Problems.size(); i++)
				if (Problems.get(i).getTypeprob() == 0)
					Filtered.add(Problems.get(i));
			break;
		case 1:
			for (int i = 0; i < Problems.size(); i++)
				if (Problems.get(i).getTypeprob() == 1)
					Filtered.add(Problems.get(i));
			break;
		}
	}

	public void showDialog(final ImageView thecat) {
		ArrayList<Categorie> cats = db.geCategories(this);
		final HashMap<Integer, Categorie> CatKeys = new HashMap<Integer, Categorie>();
		final CharSequence[] items = new CharSequence[cats.size() + 1];
		items[0] = getString(R.string.a_others);
		int indexcat = 0;

		for (int i = 0; i < cats.size(); i++) {
			items[i + 1] = cats.get(i).getTitle();
			CatKeys.put(i + 1, cats.get(i));
			if ((cats.get(i).getId() == categorie))
				indexcat = i + 1;
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.a_categories));
		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				if (CatKeys.get(choice) == null) {
					thecat.setImageResource(R.drawable.tagoff);
					categorie = -1;
				} else {
					thecat.setImageResource(R.drawable.tagon);
					categorie = CatKeys.get(choice).getId();
				}
				if (fromList) {
					db.editCat(ItemClicked.getIdprob(), categorie);
					ItemClicked.setCat(categorie);
				}
			}
		});
		builder.setNegativeButton(getString(R.string.a_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		builder.setSingleChoiceItems(items, indexcat,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						choice = which;

						// dialog.dismiss();
					}
				});
		builder.show();

	}

	private void showCalander(final ImageView dateimg) {
		DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
				datep, myCalendar.get(Calendar.YEAR),
				myCalendar.get(Calendar.MONTH),
				myCalendar.get(Calendar.DAY_OF_MONTH));
		datechoose = true;

		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.a_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_NEUTRAL) {
							datechoose = false;
						}
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.nodate),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_NEGATIVE) {
							datechoose = false;
							dateimg.setImageResource(R.drawable.calendaroff);
							thedate = "";
							if (fromList) {
								db.editDate(ItemClicked.getIdprob(), thedate);
								datetext.setText(thedate);
								ItemClicked.setDate("");
							}
						}
					}
				});
		dialog.show();

	}

	public void hideKeybord(EditText edt) {
		InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
	}

	public void showKeyborad(final EditText ettext) {
		ettext.requestFocus();
		ettext.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(ettext, 0);
			}
		}, 200);
	}

	public void listenToSpeech(String lang, boolean froms, EditText et) {
		Vm.listenToSpeech("", false, et);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingBtn:
			FragmentManager fm = getSupportFragmentManager();
			Fragment frag = fm.findFragmentByTag(FRAG_TAG_SETTINGS);
			if (frag != null) {
				if (((SettingsFrag) frag).isSettingVisible())
					((SettingsFrag) frag).setSettingsVisibility(true);
				else
					((SettingsFrag) frag).setSettingsVisibility(false);
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
			categorie = -1;
			thedate = "";
			isprio = 0;
			fromList = false;
			editprob = false;
			Noprobtext.setVisibility(View.GONE);
			
			showKeyborad(EditeProb);
			break;
		case R.id.searchBtn:
			if (EditeProb.getText().length() == 0) {
				SearchLayout.setVisibility(View.VISIBLE);
				addproblemLayout.setVisibility(View.GONE);
				btnAddProblem.setEnabled(true);
				showKeyborad(Search);
			}
			break;
		case R.id.CancelSearch:
			SearchLayout.setVisibility(View.GONE);
			Search.setText("");
			hideKeybord(Search);
			btnAddProblem.setEnabled(true);
			reloadList(Filtered);
			break;
		case R.id.imgWarning:
			if (isprio == 1) {
				priority.setImageResource(R.drawable.nopriority);
				isprio = 0;

			} else {
				priority.setImageResource(R.drawable.priority);
				Toast.makeText(getApplicationContext(), getString(R.string.priority_set),
						Toast.LENGTH_SHORT).show();
				isprio = 1;
			}
			break;
		case R.id.imgDate:
			showCalander(date);

			break;
		case R.id.imgCat:

			showDialog(cat);
			break;
		case R.id.SaveBtn:
			if (EditeProb.getText().length() == 0) {
				EditeProb.setBackgroundResource(R.drawable.edit_warning);
				Animation shake = AnimationUtils.loadAnimation(this,
						R.anim.shake);
				EditeProb.startAnimation(shake);

			} else {

				EditeProb.setBackgroundResource(R.drawable.edit_text);
				if (editprob) {
					if (ItemClicked != null) {
						ItemClicked.setResprob(EditeProb.getText().toString());
						db.editProblem(ItemClicked);
					}
				} else
					db.AddProblem(new Problem(0, "" + EditeProb.getText(), "",
							0, categorie, thedate, isprio));

				Problems = db.getProblemes();
				Filtered.clear();
				managemode();
				reloadList(Filtered);
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
		// case R.id.ModeProb:
		case R.id.ArrowImg:
			showModeDialog();
			break;
		case R.id.imageBack:
			RemoveFrag();
			Problems = db.getProblemes();
			Filtered.clear();
			managemode();
			reloadList(Filtered);
			break;
		case R.id.Voice:
			listenToSpeech("", false, EditeProb);
			break;

		}

	}

}
