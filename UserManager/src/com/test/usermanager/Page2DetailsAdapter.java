package com.test.usermanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.usermanager.zuserservice_srv.v1.entitytypes.User;
	
/**
 * Details adapter.
 */
public class Page2DetailsAdapter extends BaseAdapter
{
	public static enum SapSemantics {tel, email, url};
	

	private Context mContext;
	private User entry;

	private List<String> propertiesValues = new ArrayList<String>();
	private List<String> labels = new ArrayList<String>();
	private List<String> sapSemanticsList = new ArrayList<String>();

	/**
	 * Constructs a new details adapter with the given parameters.
	 * @param c - application context.
	 * @param e - entry.
	 */
	public Page2DetailsAdapter(Context c, User e)
	{
		
		mContext = c;
		entry = e;
		propertiesValues.add(String.valueOf(entry.getEmail()));
		labels.add(User.getLabelForProperty("Email"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getFirstname()));
		labels.add(User.getLabelForProperty("Firstname"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getLastname()));
		labels.add(User.getLabelForProperty("Lastname"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getTelephone()));
		labels.add(User.getLabelForProperty("Telephone"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getUsername()));
		labels.add(User.getLabelForProperty("Username"));
		sapSemanticsList.add(null);
	}

	/**
	 * Returns the amount of entries.
	 * @return - the amount of entries.
	 */
	public int getCount()
	{
		return propertiesValues.size();
	}

	/**
	 * Returns the item in the given position.
	 * @param position - the position of the desired item.
	 * @return - the item in the given position.
	 */
	public Object getItem(int position)
	{
		return position;
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
	
	private class ViewHolder 
	{
		public ImageView imageView;
		public TextView textView1;
		public TextView textView2;
	}
		
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		
		if (rowView == null || position == propertiesValues.size()) 
		{			
			LayoutInflater mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Inflate a view template
			rowView = mInflater.inflate(com.test.usermanager.R.layout.item_detail_row, parent, false);
			
			ViewHolder holder = new ViewHolder();
			
			holder.textView1 = (TextView) rowView.findViewById(com.test.usermanager.R.id.label_username);
			holder.textView1.setTextSize(22);
			holder.textView2 = (TextView) rowView.findViewById(com.test.usermanager.R.id.label_firstname);
			holder.imageView = (ImageView) rowView.findViewById(com.test.usermanager.R.id.imageView1);
			rowView.setTag(holder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		

		String value = getPropertyValue(position);
		String label = labels.get(position);

		holder.textView1.setText(label);
		holder.textView2.setText(value);
		holder.imageView.setVisibility(View.INVISIBLE);
		
		SapSemantics sapSemantics = getSapSemantics(position);
		if (sapSemantics != null)
		{
			switch (sapSemantics)
			{
				case tel:   holder.imageView.setImageResource(com.test.usermanager.R.drawable.tel);
							holder.imageView.setVisibility(View.VISIBLE);
						    break;
						    
				case email: holder.imageView.setImageResource(com.test.usermanager.R.drawable.email);
							holder.imageView.setVisibility(View.VISIBLE);
					        break;
					        
				case url:   holder.imageView.setImageResource(com.test.usermanager.R.drawable.url);
							holder.imageView.setVisibility(View.VISIBLE);
					        break;
			}
		}

		return rowView;
	}
	
	/**
	 * Returns the SAP semantics in the given position.
	 * @param position
	 * @return - SAP semantics in the given position.
	 */
	public SapSemantics getSapSemantics(int position)
	{
		if (sapSemanticsList == null || sapSemanticsList.isEmpty() || position >= sapSemanticsList.size())
		{
			return null;
		}
		
		String value = this.sapSemanticsList.get(position);
		if (value == null)
		{
			return null;
		}
		
		value = value.toLowerCase();
		
		SapSemantics[] values = SapSemantics.values();
		for (SapSemantics sapSemantics : values) 
		{
			String semanticName = sapSemantics.name();
			if (semanticName.equals(value) || value.contains(semanticName + ";"))
			{
				return sapSemantics;
			}
		}
		return null;
	}
	
	/**
	 * Returns the property value.
	 * @param value
	 * @return - property value.
	 */
	public String getPropertyValue(int position)
	{
		if (propertiesValues == null || propertiesValues.isEmpty() || position >= propertiesValues.size())
		{
			return mContext.getString(com.test.usermanager.R.string.no_value);
		}

		String value = propertiesValues.get(position);
		if (value == null || value.length() == 0 || value.equalsIgnoreCase("null"))
		{
			return mContext.getString(com.test.usermanager.R.string.no_value);
		}
		
		return value;
	}
}
