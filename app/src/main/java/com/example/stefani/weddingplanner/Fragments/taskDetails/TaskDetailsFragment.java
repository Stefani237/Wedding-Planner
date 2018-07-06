package com.example.stefani.weddingplanner.Fragments.taskDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TaskDetailsFragment extends Fragment implements ITaskDetails.View {

    @BindView(R.id.task_details_name)
    EditText mTaskDetailsName;
    @BindView(R.id.task_details_desc)
    EditText mTaskDetailsDesc;
    @BindView(R.id.task_details_deadline)
    EditText mTaskDetailsEndDate;
    @BindView(R.id.task_details_tips)
    EditText mTaskDetailsTips;
    @BindView(R.id.task_details_est_budget)
    EditText mTaskDetailsEstBudget;
    @BindView(R.id.task_details_my_budget)
    EditText mTaskDetailsMyBudget;
    @BindView(R.id.task_details_save_data)
    Button mSaveBtn;
    @BindView(R.id.task_details_edit)
    Button mEditBtn;
    @BindView(R.id.task_details_empty_fields)
    TextView mEmptyFields;

    private TaskClass mTaskDetails;
    private ITaskDetails.Presenter mPresenter;
    private View rootView;

    public void setTaskDetails(TaskClass taskDetails) {
        // set selected task's details
        this.mTaskDetails = taskDetails;
    }

    private void setDetails() {
        // insert data to fields
        mTaskDetailsName.setText(mTaskDetails.getmTaskName());
        mTaskDetailsDesc.setText(mTaskDetails.getmDescription());
        mTaskDetailsEndDate.setText(mTaskDetails.getmEndDate());
        mTaskDetailsTips.setText(mTaskDetails.getmTips());
        mTaskDetailsEstBudget.setText(mTaskDetails.getmEstimatedPrice());
        mTaskDetailsMyBudget.setText(mTaskDetails.getmMyBudget());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.task_details_layout, container, false);

        ButterKnife.bind(this, rootView);
        mPresenter = new TaskDetailsPresenter(this);
        mPresenter.setTaskDetailsClass(mTaskDetails);

        setDetails();
        Toast.makeText(getContext(), R.string.edit_mode, Toast.LENGTH_SHORT).show();

        return rootView;
    }


    @OnClick(R.id.task_details_edit)
    public void onEditClick() {
        // enable edit fields
        mTaskDetailsName.setEnabled(true);
        mTaskDetailsDesc.setEnabled(true);
        mTaskDetailsEndDate.setEnabled(true);
        mTaskDetailsTips.setEnabled(true);
        mTaskDetailsEstBudget.setEnabled(true);
        mTaskDetailsMyBudget.setEnabled(true);
        mSaveBtn.setEnabled(true);
    }


    @OnClick(R.id.task_details_save_data)
    public void saveTaskNewData() {
        // get data from fields:
        String taskName = mTaskDetailsName.getText().toString().trim();
        String taskDesc = mTaskDetailsDesc.getText().toString().trim();
        String taskDate = mTaskDetailsEndDate.getText().toString().trim();
        String taskTips = mTaskDetailsTips.getText().toString().trim();
        String taskEstBudget = mTaskDetailsEstBudget.getText().toString().trim();
        String taskMyBudget = mTaskDetailsMyBudget.getText().toString().trim();

        mPresenter.saveDataToDB(taskName, taskDesc, taskDate, taskTips, taskEstBudget, taskMyBudget); // sent to presenter to save in db
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void toastTaskDetailsSaved() {
        Toast.makeText(getContext(), R.string.str_changes_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyTaskFieldsSetVisibility(int visible) {
        mEmptyFields.setVisibility(visible);
    }
}
