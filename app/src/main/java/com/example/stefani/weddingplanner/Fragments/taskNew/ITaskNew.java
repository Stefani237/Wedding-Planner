package com.example.stefani.weddingplanner.Fragments.taskNew;

import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

public interface ITaskNew {

    interface Presenter {

        void initializeViews();


        void addTaskToDB(TaskClass task);
    }


    interface View {
        void initializeViews();

        void clearFields();
    }

}
