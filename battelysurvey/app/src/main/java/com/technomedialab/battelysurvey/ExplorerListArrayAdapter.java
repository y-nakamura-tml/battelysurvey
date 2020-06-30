package com.technomedialab.battelysurvey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExplorerListArrayAdapter extends ArrayAdapter
{
    private int layoutId;
    private List<ExplorerItem> items;

    public ExplorerListArrayAdapter(Context context, int resourceId,
                                    List<ExplorerItem> objects)
    {
        super(context, resourceId, objects);

        layoutId = resourceId;
        items = objects;
    }

    public ExplorerItem getItem(int i)
    {
        return items.get(i);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(layoutId, null);
        }

        ExplorerItem item = items.get(position);
        if(item != null){
            TextView nameTextView = (TextView) convertView.findViewById(
                    R.id.fileNameTextView);
            TextView dateTextView = (TextView) convertView.findViewById(
                    R.id.fileDateTextView);

            if(nameTextView != null)
                nameTextView.setText(item.name);
            if(dateTextView != null)
                dateTextView.setText(item.date);
        }

        return convertView;
    }
}