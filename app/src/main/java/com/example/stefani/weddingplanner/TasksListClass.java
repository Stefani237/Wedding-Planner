package com.example.stefani.weddingplanner;

import java.util.ArrayList;

/**
 * Created by Stefani on 16/12/2017.
 */

public class TasksListClass {
    public static int mTaskCounter = 0;
    private static ArrayList<TaskClass> mArrTask = new ArrayList<>();


    public static ArrayList<TaskClass> getmArrTask() {
        return mArrTask;
    }

    public static void setmArrTask(ArrayList<TaskClass> task) {
        mArrTask = task;
    }

    public static void addTask(TaskClass task) {
        mArrTask.add(task);
    }
}
