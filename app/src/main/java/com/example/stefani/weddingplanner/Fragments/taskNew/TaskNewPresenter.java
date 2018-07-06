package com.example.stefani.weddingplanner.Fragments.taskNew;

import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

public class TaskNewPresenter implements ITaskNew.Presenter {


   // private final StorageReference mStorageReference;
    private ITaskNew.View mView;
   // private SupplierClass supplierClass;

    public TaskNewPresenter(ITaskNew.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
   //     mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.DB_FIREBASE_STORAGE);
    }


    @Override
    public void initializeViews() {

    }

    @Override
    public void addTaskToDB(TaskClass task) {
        ConnectionToFirebase.getInstance().enterDataToDB(task, Constants.TASKS); // insert new task into firebase
        mView.clearFields();
    }

}
