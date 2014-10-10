package com.saladinprojects.problemssolutions;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

 public class SettingsFrag extends Fragment{
	
	private View m_SettingsMenu;
	View m_RelativeCalque ;
	CheckBox checkBoxNotif;
	 CheckBox checkBoxCat;
	 boolean notifchecked;
	boolean catchacked;

	public SettingsFrag() {
		super();

	}
	public void Setchecked(boolean cat,boolean notif)
	{
		  this.notifchecked=notif;
		  this.catchacked=cat;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	View result = inflater.inflate(R.layout.settings, container, false);

		m_SettingsMenu = result.findViewById(R.id.layoutSettingsMenu);
		checkBoxNotif = (CheckBox)result.findViewById(R.id.checkBoxNotif);
		checkBoxCat = (CheckBox)result.findViewById(R.id.checkBox1);
		checkBoxNotif.setChecked(notifchecked);
		checkBoxCat.setChecked(catchacked);
		result.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setSettingsVisibility(false);
				return false;
			}
		});
		return result;
	}
	
	
public boolean isSettingVisible()
{
	if(m_SettingsMenu.getVisibility()==View.VISIBLE)
		return false;
	else
		return true;
}
	public void setSettingsVisibility(boolean visible) {
		
		m_SettingsMenu.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}
	

}
