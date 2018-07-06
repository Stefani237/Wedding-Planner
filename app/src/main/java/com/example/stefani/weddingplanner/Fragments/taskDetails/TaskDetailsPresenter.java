package com.example.stefani.weddingplanner.Fragments.taskDetails;

import android.view.View;

import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskDetailsPresenter implements ITaskDetails.Presenter {

    private ITaskDetails.View mView;
    private TaskClass mTaskDetails;

    public TaskDetailsPresenter(ITaskDetails.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
    }


    @Override
    public void initializeViews() {

    }

    @Override
    public void saveDataToDB(String taskName, String taskDesc, String taskDate, String taskTips, String taskEstBudget, String taskMyBudget) {
        if (taskName.length() > 0) { // if required field is fill
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.TASKS_DB_URL); // get reference to location in db
            DatabaseReference task = databaseReference.child(mTaskDetails.getmID()); // get selected task in db
            task.child(Constants.TASK_NAME).setValue(taskName);
            task.child(Constants.TASK_DESC).setValue(taskDesc);
            task.child(Constants.TASK_DATE).setValue(taskDate);
            task.child(Constants.TASK_TIPS).setValue(taskTips);
            task.child(Constants.TASK_EST_BUDGET).setValue(taskEstBudget);
            task.child(Constants.TASK_MY_BUDGET).setValue(taskMyBudget);
            mView.toastTaskDetailsSaved();
        } else {
            mView.emptyTaskFieldsSetVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTaskDetailsClass(TaskClass taskDetails) {
        // set selected task's details
        mTaskDetails = taskDetails;
    }


}
