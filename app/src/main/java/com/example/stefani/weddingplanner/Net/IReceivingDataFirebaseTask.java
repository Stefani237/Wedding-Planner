package com.example.stefani.weddingplanner.Net;

import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

import java.util.List;


public interface IReceivingDataFirebaseTask {
    void getTaskList(List<TaskClass> taskList);
}
