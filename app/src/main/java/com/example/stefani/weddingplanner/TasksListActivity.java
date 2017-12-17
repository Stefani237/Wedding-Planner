package com.example.stefani.weddingplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TasksListActivity extends AppCompatActivity {

    private ListView mListView;
    private Button mAddTask;
    private Spinner mSortList;
  /*  private String[] mBasicTasks = {"Choose a wedding hall",
            "Buy a wedding dress",
            "Buy bridal shoes",
            "Buy jewelry",
            "Choose a bridal bouquet",
            "Buy a groom's suit",
            "Choose a place to organize on the wedding day",
            "Choose Rabbi",
            "Open a file in the rabbinate",
            "Pass bridal guidance",
            "Immerse in the mikvah",
            "Choose rings",
            "Choose a DJ",
            "Select songs",
            "Choose a photographer",
            "Rent a car",
            "Design wedding invitations",
            "Give out invitations",
            "Check for arrival confirmation",
            "Make seating arrangements"};*/
   // private ExpandableListAdapter mListAdapter;
   // private List<String> mListDataHeader;
   // private HashMap<String,List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        mListView = (ListView) findViewById(R.id.tasksListView);
        mListView.setLongClickable(true);

        mAddTask = (Button) findViewById(R.id.addTask);
        mSortList = (Spinner) findViewById(R.id.task_sort);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://weddingplanner-61cad.firebaseio.com/Tasks");

        FirebaseListAdapter<Object> firbaseListAdaper = new FirebaseListAdapter<Object>(
                this,
                Object.class,
                R.layout.tasks_list_items,
                databaseReference
        ) {
            @Override
            protected void populateView(View v, Object object, int position) {
                HashMap<String, String> tasksHashMap =  (HashMap<String, String>)object;
                TasksListClass.mTaskCounter++;
                TextView taskName = v.findViewById(R.id.task_name);
                taskName.setText(tasksHashMap.get("mTaskName"));
                taskName.setTag(tasksHashMap.get("mID"));

                CheckBox isDone = v.findViewById(R.id.task_checkBox);
            }
        };
        mListView.setAdapter(firbaseListAdaper);
        addNewGuest();
        createDropDown();
        longPressDialog();
    }

    public void addNewGuest(){
        mAddTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksListActivity.this, NewTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createDropDown(){
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(TasksListActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.task_sort_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortList.setAdapter(myAdapter);
    }

    public void longPressDialog(){
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(final AdapterView<?> parent,final View v, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        TasksListActivity.this);
                alert.setTitle("Confirm delete");
                alert.setMessage("Are you sure you want to delete the task from the list?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TextView taskName = v.findViewById(R.id.task_name);
                        String taskID = taskName.getTag().toString();
                        FirebaseDatabase.getInstance().getReference("Tasks").child(taskID).removeValue();

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
    }
}
