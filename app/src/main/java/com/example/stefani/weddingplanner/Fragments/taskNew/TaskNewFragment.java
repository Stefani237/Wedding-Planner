package com.example.stefani.weddingplanner.Fragments.taskNew;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TaskNewFragment extends Fragment implements ITaskNew.View {


    @BindView(R.id.nameField)
    EditText mNameField;
    @BindView(R.id.descriptionField)
    EditText mDescriptionField;
    @BindView(R.id.deadlineExecutionField)
    EditText mDeadlineField;
    @BindView(R.id.budgetField)
    EditText mBudgetField;
    @BindView(R.id.new_task_empty_fields)
    TextView mEmptyFields;
    @BindView(R.id.addToTaskDB)
    TextView mAddButton;


    private View rootView;
    private ITaskNew.Presenter mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_new_task, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter = new TaskNewPresenter(this);

        return rootView;
    }


    @Override
    public void clearFields() {
        mNameField.setText("");
        mDescriptionField.setText("");
        mDeadlineField.setText("");
        mBudgetField.setText("");
    }


    @OnClick(R.id.addToTaskDB)
    public void addNewTaskClick() {
        String name = mNameField.getText().toString().trim();

        if (name.length() > 0) { // if name field not empty
            mEmptyFields.setVisibility(View.INVISIBLE);

            TaskClass task = new TaskClass(name);
            task.setmDescription(mDescriptionField.getText().toString().trim());
            task.setmEndDate(mDeadlineField.getText().toString().trim());
            task.setmMyBudget(mBudgetField.getText().toString().trim());

            mPresenter.addTaskToDB(task); // send task to presenter to add to db
            Toast.makeText(getContext(), R.string.str_added_task, Toast.LENGTH_SHORT).show();

        } else {
            mEmptyFields.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initializeViews() {

    }
}
