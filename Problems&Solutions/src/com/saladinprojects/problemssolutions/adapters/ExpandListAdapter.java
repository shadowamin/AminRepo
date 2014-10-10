package com.saladinprojects.problemssolutions.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saladinprojects.problemssolutions.MainActivity;
import com.saladinprojects.problemssolutions.R;
import com.saladinprojects.problemssolutions.SolutionsFrag;
import com.saladinprojects.problemssolutions.models.GroupeProbs;
import com.saladinprojects.problemssolutions.models.Problem;
import com.saladinprojects.problemssolutions.models.ViewHolder;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<GroupeProbs> groups;
	public ExpandListAdapter(Context context, ArrayList<GroupeProbs> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(Problem item, GroupeProbs group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<Problem> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<Problem> chList = groups.get(groupPosition).getItems();
		
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		final Problem item = (Problem) getChild(groupPosition, childPosition);
		
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.problem_item,
						parent, false);
			holder.titre = (TextView) view
					.findViewById(R.id.textProb);
			holder.Date= (TextView) view
					.findViewById(R.id.textItemDate);
//			holder.TextCat=(TextView) convertView
//					.findViewById(R.id.CatItemText);
			holder.LayoutInfo=(LinearLayout)view
					.findViewById(R.id.LayoutInfo);
//			holder.CatLayout=(LinearLayout)view
//					.findViewById(R.id.CatLayout);
			holder.problemPrio=(View)view.findViewById(R.id.problemPrio);
			holder.linearArrow=(LinearLayout)view
					.findViewById(R.id.linearArrow);
			holder.Cat=(ImageView)view
					.findViewById(R.id.imgItemCat);
			holder.Priority=(ImageView)view
					.findViewById(R.id.imgItemWarning);
			holder.DateImage=(ImageView)view
					.findViewById(R.id.imgItemDate);
			holder.DelImage=(ImageView)view
					.findViewById(R.id.imgItemDel);
			holder.Arrow=(ImageView)view
					.findViewById(R.id.arrow);
			holder.EditImage=(ImageView)view
					.findViewById(R.id.imgItemEdit);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();

		holder.titre.setText(item.getResprob().toString());
		
		holder.Date.setText(item.getDate());
//		if(item.getCat()<0)
//	holder.TextCat.setText("Others");
//		else
//			holder.TextCat.setText(db.GetCategorie(item.getCat()));	
//		if(item.isWithcat())
//			holder.CatLayout.setVisibility(View.VISIBLE);
//		else
//			holder.CatLayout.setVisibility(View.GONE);
		if(item.getPriority()==0)
			{holder.Priority.setImageResource(R.drawable.nopriority);
			holder.problemPrio.setVisibility(View.GONE);
			view.setBackgroundColor(Color.WHITE);
			}else
			{holder.Priority.setImageResource(R.drawable.priority);
			view.setBackgroundColor(context.getResources().getColor(R.color.redBeck));
			holder.problemPrio.setVisibility(View.VISIBLE);
			}
		if(item.getCat()>=0)
			holder.Cat.setImageResource(R.drawable.tagon);
		else
			holder.Cat.setImageResource(R.drawable.tagoff);
	
		if(item.getDate()!=null && !item.getDate().equals(""))
			holder.DateImage.setImageResource(R.drawable.calendaron);
		else
			holder.DateImage.setImageResource(R.drawable.calendaroff);
		holder.titre.setMaxLines(1);
	
		if(item.getTypeprob()>0)
		{	holder.titre.setTextColor(context.getResources().getColor(R.color.lightgreen));
		holder.problemPrio.setVisibility(View.GONE);
		view.setBackgroundColor(Color.WHITE);
		}
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<Problem> chList = groups.get(groupPosition).getItems();

		return chList.size();

	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		GroupeProbs group = (GroupeProbs) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandlist_group_item, null);
		}
		TextView title = (TextView) view.findViewById(R.id.groupeText);
		TextView count = (TextView) view.findViewById(R.id.textCount);
		title.setText(group.getName());
		count.setText("("+group.getItems().size()+")");
		
		
		// TODO Auto-generated method stub
		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
