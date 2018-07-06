package com.example.stefani.weddingplanner.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;

import java.util.ArrayList;
import java.util.List;


public class SuppliersBaseFirebaseAdapter extends RecyclerView.Adapter<SuppliersBaseFirebaseAdapter.ViewHolder> {

    private List<SupplierClass> mSupplierList;
    private OnItemClickListener mOnItemClickListener = null;

    public SuppliersBaseFirebaseAdapter(List<SupplierClass> supList) {
        mSupplierList = new ArrayList<>(supList);
    }

    public interface OnItemClickListener {
        void onItemLongListener(SupplierClass mSupplier);

        void onItemClickListener(SupplierClass mSupplier);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    // notifies that data has been changed and any View reflecting the data should refresh itself
    public void updateData(List<SupplierClass> supList) {
        mSupplierList = new ArrayList<>(supList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // init view of record
        ViewGroup rowView = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_list_items, parent, false);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(SuppliersBaseFirebaseAdapter.ViewHolder holder, int position) {
        // update data in holder (= record)
        holder.mSupplierName.setText(mSupplierList.get(position).getmCompanyName());
        holder.mPrice.setText(mSupplierList.get(position).getmPrice());
        holder.view.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mSupplierList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public TextView mSupplierName;
        public TextView mPrice;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            mSupplierName = itemView.findViewById(R.id.sup_list_name);
            mPrice = itemView.findViewById(R.id.sup_list_price);

            view.setOnLongClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {
            String position = itemView.getTag().toString();

            SupplierClass supplier = mSupplierList.get(Integer.parseInt(position));
            mOnItemClickListener.onItemLongListener(supplier);
            return false; // true if the callback consumed the long click, false otherwise
        }

        @Override
        public void onClick(View view) {

            String position = itemView.getTag().toString();

            SupplierClass supplier = mSupplierList.get(Integer.parseInt(position));
            mOnItemClickListener.onItemClickListener(supplier);
        }
    }
}
