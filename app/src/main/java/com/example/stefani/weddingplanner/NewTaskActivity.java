package com.example.stefani.weddingplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class NewTaskActivity extends AppCompatActivity {

    private EditText mTaskNameField;
    private EditText mTaskDescriptionField;
    private EditText mExecutionDateField;
    private EditText mMyBudgetField;
    private Button mAddButton;
    private Button mCancelButton;
    private Firebase mRootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mTaskNameField = (EditText) findViewById(R.id.nameField);
        mTaskDescriptionField = (EditText) findViewById(R.id.descriptionField);
        mExecutionDateField = (EditText) findViewById(R.id.deadlineExecutionField);
        mMyBudgetField = (EditText) findViewById(R.id.budgetField);

        mAddButton = (Button) findViewById(R.id.addToTaskDB);
        mCancelButton = (Button) findViewById(R.id.cancelTaskDB);

        mRootRef = new Firebase("https://weddingplanner-61cad.firebaseio.com/Tasks");

        cancelAddingDB();
        addNewTask();
    }


    public void clearFields(){
        mTaskNameField.setText("");
        mTaskDescriptionField.setText("");
        mExecutionDateField.setText("");
        mMyBudgetField.setText("");
    }

    public void addNewTask() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = mTaskNameField.getText().toString();
                String description = mTaskDescriptionField.getText().toString();
                String lastDate = mExecutionDateField.getText().toString();
                String budget = mMyBudgetField.getText().toString();

                TaskClass task = new TaskClass(""+ ++TasksListClass.mTaskCounter, name, description, lastDate, " ", " ", budget);

                TasksListClass.addTask(task);

                Firebase userRef = mRootRef.child("" + TasksListClass.mTaskCounter);
                userRef.setValue(task);

                clearFields();
            }
        });
    }

    public void cancelAddingDB(){
        mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTaskActivity.this, TasksListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TasksListClass.mTaskCounter = 0;
    }
}
