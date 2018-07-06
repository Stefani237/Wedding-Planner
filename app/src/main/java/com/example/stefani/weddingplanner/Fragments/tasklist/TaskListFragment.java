package com.example.stefani.weddingplanner.Fragments.tasklist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.Adapters.TasksBaseFirebaseAdapter;
import com.example.stefani.weddingplanner.Fragments.BaseFragmentClass;
import com.example.stefani.weddingplanner.Fragments.taskNew.TaskNewFragment;
import com.example.stefani.weddingplanner.Fragments.taskDetails.TaskDetailsFragment;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TaskListFragment extends BaseFragmentClass implements AdapterView.OnItemSelectedListener,
        ITaskList.View {

    @BindView(R.id.addTask)
    Button mAddBtn;
    @BindView(R.id.task_sort)
    Spinner mListFilter;
    @BindView(R.id.tasksListView)
    RecyclerView list;

    private TasksBaseFirebaseAdapter mBaseFirebaseAdapter;
    private ITaskList.Presenter mPresenter;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter = new TaskListPresenter(this);
        mPresenter.initializeViews();

        mListFilter.setOnItemSelectedListener(this);

        onRowClick();

        return rootView;
    }

    private void onRowClick() {
        mBaseFirebaseAdapter.setmOnItemClickListener(new TasksBaseFirebaseAdapter.OnItemClickListener() {

            @Override
            public void onItemLongListener(TaskClass mTask) {
                longPressDeleteDialog(mTask);
            }

            @Override
            public void onItemClickListener(TaskClass mTask) {
                startTaskDetailFragment(mTask);
            }
        });
    }

    @OnClick(R.id.addTask)
    public void createNewTask() { // go to new task fragment
        TaskNewFragment newTaskFragment = new TaskNewFragment();
        getmInteractFragments().setNewFragment(newTaskFragment, newTaskFragment.getClass().toString());
    }

    private void createDropDown() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.task_sort_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListFilter.setAdapter(myAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        int positionFilter = (int) id; // get selected value from filter
        mPresenter.onItemSelected(positionFilter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void startTaskDetailFragment(TaskClass task) {
        //go to task details fragment
        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        taskDetailsFragment.setTaskDetails(task);
        getmInteractFragments().setNewFragment(taskDetailsFragment, taskDetailsFragment.getClass().toString());
    }

    private void longPressDeleteDialog(final TaskClass task) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.str_delete_title);
        alert.setMessage(R.string.str_delete_msg_task);
        alert.setPositiveButton(R.string.str_delete_positive, (dialog, which) -> {

            mPresenter.removeFromDB(task.getmID()); // delete selected task from list
            dialog.dismiss();

        });
        alert.setNegativeButton(R.string.str_delete_negative, (dialog, which) -> dialog.dismiss());
        alert.show();

    }

    @Override
    public void initializeViews(List<TaskClass> mTaskClassList) {
        mBaseFirebaseAdapter = new TasksBaseFirebaseAdapter(mTaskClassList);
        list.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(mBaseFirebaseAdapter);
        createDropDown();
    }

    @Override
    public void updateAdapter(List<TaskClass> taskList) {
        mBaseFirebaseAdapter.updateData(taskList); // update list view
    }
}
