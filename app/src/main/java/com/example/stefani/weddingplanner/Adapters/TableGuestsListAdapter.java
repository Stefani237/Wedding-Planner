package com.example.stefani.weddingplanner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.BasicClasses.TableGuestsListClass;

import java.util.ArrayList;


public class TableGuestsListAdapter extends ArrayAdapter<TableGuestsListClass> {
    private ArrayList<TableGuestsListClass> mGuestsList;
    private Context mContext;

    private static class ViewHolder {
        TextView guestName;
        TextView numOfGuests;
    }

    public TableGuestsListAdapter(ArrayList<TableGuestsListClass> guestsList, Context context) {
        // init view
        super(context, R.layout.table_details_list_items, guestsList);
        this.mGuestsList = guestsList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TableGuestsListClass tableGuestsListClass = getItem(position);

        TableGuestsListAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new TableGuestsListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.table_details_list_items, parent, false);
            viewHolder.guestName = convertView.findViewById(R.id.table_details_item_name_of_guest);
            viewHolder.numOfGuests = convertView.findViewById(R.id.table_details_item_amount_of_guest);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TableGuestsListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.guestName.setText(tableGuestsListClass.getmGuestName());
        viewHolder.numOfGuests.setText("" + tableGuestsListClass.getmGuestsAmount());

        return convertView;
    }
}
