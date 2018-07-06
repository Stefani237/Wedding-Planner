package com.example.stefani.weddingplanner;

import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stefani.weddingplanner.Fragments.tasklist.TaskListPresenter;
import com.example.stefani.weddingplanner.Fragments.welcome.WelcomeScreenFragment;
import com.example.stefani.weddingplanner.Fragments.supplierlist.SuppliersListPresenter;
import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements InteractFragments{

    private void addFragmentToActivity(@NonNull Fragment fragment, String fragmentTag) {
        // FragmentManager is responsible for interacting with fragments associated with this activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); //  enables adding or removing fragments
        transaction.add(R.id.contentFrame, fragment, fragmentTag); // add a fragment to the activity state
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());  // the transaction will be remembered after it is committed,
                                                                            // and will reverse its operation when later popped off the stack.
        }
        transaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromDB();
        addFragmentToActivity(new WelcomeScreenFragment(), WelcomeScreenFragment.class.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getDataFromDB(){
        ConnectionToFirebase.getInstance().getAllGuestDetailsFromDB(guestList -> {

        });

        ConnectionToFirebase.getInstance().getAllTasksDetailsFromDB(taskList -> {
            TaskListPresenter.mTaskClassList = new ArrayList<>(taskList);
        });

        ConnectionToFirebase.getInstance().getAllSupplierDetailsFromDB(supList -> {
            SuppliersListPresenter.mSuppliersClassList = new ArrayList<>(supList);
        });
    }


    @Override
    public void setNewFragment(Fragment fragment, String fragmentTag) {
        addFragmentToActivity(fragment, fragmentTag);
    }
}
