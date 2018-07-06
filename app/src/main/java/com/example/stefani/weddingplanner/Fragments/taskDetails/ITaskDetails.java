package com.example.stefani.weddingplanner.Fragments.taskDetails;

import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

public interface ITaskDetails {

    interface Presenter {

        void initializeViews();

        void saveDataToDB(String taskName, String taskDesc, String taskDate, String taskTips, String taskEstBudget, String taskMyBudget);

        void setTaskDetailsClass(TaskClass taskDetails);

    }


    interface View {
        void initializeViews();

        void toastTaskDetailsSaved();

        void emptyTaskFieldsSetVisibility(int visible);
    }

}
