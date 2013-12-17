package com.test.usermanager;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.usermanager.zuserservice_srv.v1.entitytypes.User;

/**
 * List adapter.
 */
public class Page1ListAdapter extends BaseAdapter
{	
	private List<User> entries;
	private List<User> filteredEntries;

	private Context mContext;

	/**
	 * Constructs a new list adapter with the given context.
	 * @param c - application context.
	 */
	public Page1ListAdapter(Context c)
	{
		mContext = c;
	}

	/**
	 * Returns the list of entries.
	 * @return - list of entries.
	 */
	public List<User> getEntries()
	{
		return entries;
	}

	/**
	 * Sets the given entries.
	 * @param entries
	 */
	public void setEntries(List<User> entries)
	{
		this.entries = entries;
		this.filteredEntries = entries;
	}

	/**
	 * Returns the number of entries.
	 * @return - the number of entries.
	 */
	public int getCount()
	{
		return filteredEntries.size();
	}

	/**
	 * Returns the item in the given position.
	 * @param position - the position of the desired item.
	 * @return - the item in the given position.
	 */
	public Object getItem(int position)
	{
		return filteredEntries.get(position);
	}

	/**
	 * Returns the id of the item in the given position.
	 * @param position - the position of the item.
	 * @return - the id of the item in the given position.
	 */
	public long getItemId(int position)
	{
		return position;
	}
	
	private static class ViewHolder 
	{
		public LinearLayout ll;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		
		if (rowView == null) 
		{
			LayoutInflater mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate a view template
			rowView = mInflater.inflate(com.test.usermanager.R.layout.item_entry, parent, false);
			
			ViewHolder holder = new ViewHolder();
			holder.ll = (LinearLayout) rowView.findViewById(com.test.usermanager.R.id.linearLayout1);
			holder.ll.setPadding(10, 10, 10, 10);
			holder.ll.setOrientation(LinearLayout.VERTICAL);
			
			rowView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.ll.removeAllViews();
		
		User entry = filteredEntries.get(position);
		
		TextView UsernameTextView = new TextView(mContext);
		//set the first property to bigger font
		UsernameTextView.setTextSize(22); 
		UsernameTextView.setText(getPropertyValue(String.valueOf(entry.getUsername())));
		holder.ll.addView(UsernameTextView);	
		
		TextView FirstnameTextView = new TextView(mContext);
		FirstnameTextView.setText(getPropertyValue(String.valueOf(entry.getFirstname())));
		holder.ll.addView(FirstnameTextView);	
		
		TextView LastnameTextView = new TextView(mContext);
		LastnameTextView.setText(getPropertyValue(String.valueOf(entry.getLastname())));
		holder.ll.addView(LastnameTextView);	
		return rowView;
	}
	
	/**
	 * Returns the property value.
	 * @param value
	 * @return - property value.
	 */
	public String getPropertyValue(String value)
	{
		if (value.equalsIgnoreCase("null"))
		{
			return mContext.getString(com.test.usermanager.R.string.no_value);
		}

		return value;
	}

	/**
	 * Filters the items by the given constraint.
	 * @param constraint
	 */
	public void filter(CharSequence constraint)
	{
		if (constraint != null)
		{
			constraint = constraint.toString().toLowerCase();
			this.filteredEntries = new ArrayList<User>();
			for (User entry : entries)
			{
				ArrayList<String> values = new ArrayList<String>();

				values.add(String.valueOf(entry.getUsername()).toLowerCase());
				values.add(String.valueOf(entry.getFirstname()).toLowerCase());
				values.add(String.valueOf(entry.getLastname()).toLowerCase());
				boolean found = false;
				for (String s : values)
				{
					if (s != null && s.contains(constraint))
					{
						found = true;
						break;
					}
				}
				
				if(found)
					filteredEntries.add(entry);
			}
		} 
		else
		{
			this.filteredEntries = entries;
		}
		
		notifyDataSetChanged();
	}
}