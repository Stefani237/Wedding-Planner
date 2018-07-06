package com.example.stefani.weddingplanner.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class TasksBaseFirebaseAdapter extends RecyclerView.Adapter<TasksBaseFirebaseAdapter.ViewHolder> {

    private List<TaskClass> mTaskList;
    private OnItemClickListener mOnItemClickListener = null;

    public TasksBaseFirebaseAdapter(List<TaskClass> taskList) {
        mTaskList = new ArrayList<>(taskList);
    }

    public interface OnItemClickListener {
        void onItemLongListener(TaskClass mTask);

        void onItemClickListener(TaskClass mTask);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    // notifies that data has been changed and any View reflecting the data should refresh itself
    public void updateData(List<TaskClass> taskList) {
        mTaskList = new ArrayList<>(taskList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // init view of record
        ViewGroup rowView = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_items, parent, false);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // update data in holder (= record)
        holder.mTaskListName.setText(mTaskList.get(position).getmTaskName());
        holder.view.setTag(position);
        TaskClass task = mTaskList.get(position);

        holder.mIsDone.setTag(position); // update view
        if (task.getmIsDone().equalsIgnoreCase("true")) {
            holder.mIsDone.setChecked(true);
        } else {
            holder.mIsDone.setChecked(false);
        }

        holder.mIsDone.setOnClickListener(new View.OnClickListener() { // update db from view
            @Override
            public void onClick(View view) {
                CheckBox chk = (CheckBox) view;
                String position = chk.getTag().toString();

                TaskClass task = mTaskList.get(Integer.parseInt(position));
                if (chk.isChecked()) {
                    mTaskList.get(Integer.parseInt(position)).setmIsDone("TRUE");
                } else {
                    mTaskList.get(Integer.parseInt(position)).setmIsDone("FALSE");
                }
                updateDB(mTaskList.get(Integer.parseInt(position)));
            }
        });
    }


    private void updateDB(TaskClass taskClass) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.TASKS_DB_URL);
        DatabaseReference task = databaseReference.child(taskClass.getmID());
        task.child("mIsDone").setValue(taskClass.getmIsDone());
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public TextView mTaskListName;
        public CheckBox mIsDone;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            mTaskListName = itemView.findViewById(R.id.task_name);
            mIsDone = itemView.findViewById(R.id.task_checkBox);

            view.setOnLongClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {
            String position = itemView.getTag().toString();

            TaskClass task = mTaskList.get(Integer.parseInt(position));
            mOnItemClickListener.onItemLongListener(task);
            return false; // true if the callback consumed the long click, false otherwise
        }

        @Override
        public void onClick(View view) {
            String position = itemView.getTag().toString();

            TaskClass task = mTaskList.get(Integer.parseInt(position));
            mOnItemClickListener.onItemClickListener(task);
        }


    }
}
