package com.saladinprojects.problemssolutions.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saladinprojects.problemssolutions.R;
import com.saladinprojects.problemssolutions.models.Solution;
import com.saladinprojects.problemssolutions.models.ViewHolder2;

public  class SolutionsAdapater extends ArrayAdapter<Solution> {
Context ctxt;
ArrayList<Solution> FilteredS;
	public SolutionsAdapater(Context context, ArrayList<Solution> items) {
		
		super(context, R.layout.solution_item, items);
		ctxt=context;
		FilteredS=items;
	}

	@Override
	public int getCount() {
		if (FilteredS != null) {
			return FilteredS.size();
		}
		return 0;
	}

	@Override
	public Solution getItem(int position) {
		return (Solution) FilteredS.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 holder;
		final Solution item = (Solution) FilteredS.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) ctxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder2();
				convertView = inflater.inflate(R.layout.solution_item,
						parent, false);
			holder.titre = (TextView) convertView
					.findViewById(R.id.textSol);
			holder.LayoutInfo=(LinearLayout)convertView
					.findViewById(R.id.LayoutInfoSol);
			holder.DelImage=(ImageView)convertView
					.findViewById(R.id.imgSolDel);
			holder.EditSol=(ImageView)convertView
					.findViewById(R.id.imgSolEdit);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder2) convertView.getTag();
		holder.titre.setMaxLines(1);
		holder.titre.setText(item.getTxtsol());
		
	
		return convertView;
	}
	}
