package com.example.stefani.weddingplanner.Fragments.tasklist;

import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

import java.util.List;

public interface ITaskList {

    interface Presenter {

        void initializeViews();


        void removeFromDB(String getmID);

        void onItemSelected(int positionFilter);
    }


    interface View {
        void initializeViews(List<TaskClass> mTaskClassList);

        void updateAdapter(List<TaskClass> taskList);
    }

}
