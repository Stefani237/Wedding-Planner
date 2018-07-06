package com.example.stefani.weddingplanner.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.R;

import java.util.ArrayList;
import java.util.List;


public class GuestsBaseFirebaseAdapter extends RecyclerView.Adapter<GuestsBaseFirebaseAdapter.ViewHolder> {

    private List<GuestClass> mGuestList;
    private OnItemClickListener mOnItemClickListener = null;


    public GuestsBaseFirebaseAdapter(List<GuestClass> userList) {
        mGuestList = new ArrayList<>(userList);
    }

    public interface OnItemClickListener {
        void onItemLongListener(GuestClass mGuest);

        void onItemClickListener(GuestClass mGuest);
    }


    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    // notifies that data has been changed and any View reflecting the data should refresh itself
    public void updateData(List<GuestClass> userList) {
        mGuestList = new ArrayList<>(userList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // init view of record
        ViewGroup rowView = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_list_items, parent, false);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // update data in holder (= record)
        holder.mGuestListFullname.setText(mGuestList.get(position).getmFullName());
        holder.mGuestListPhoneNumber.setText(mGuestList.get(position).getmPhone());
        holder.view.setTag(position); // set record id according to it's position

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mGuestList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public TextView mGuestListFullname;
        public TextView mGuestListPhoneNumber;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            mGuestListFullname = itemView.findViewById(R.id.guest_list_fullname);
            mGuestListPhoneNumber = itemView.findViewById(R.id.guest_list_phoneNumber);

            view.setOnLongClickListener(this);
            view.setOnClickListener(this);

        }


        @Override
        public boolean onLongClick(View view) {
            String position = itemView.getTag().toString();

            GuestClass guest = mGuestList.get(Integer.parseInt(position));
            mOnItemClickListener.onItemLongListener(guest);
            return false; // true if the callback consumed the long click, false otherwise
    }

        @Override
        public void onClick(View view) {
            String position = itemView.getTag().toString();

            GuestClass guest = mGuestList.get(Integer.parseInt(position));
            mOnItemClickListener.onItemClickListener(guest);
        }
    }
}
