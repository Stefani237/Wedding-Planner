package com.example.stefani.weddingplanner.Helpers;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.Fragments.supplierlist.SuppliersListPresenter;
import com.example.stefani.weddingplanner.Fragments.tasklist.TaskListPresenter;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.GuestListClass;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;

import java.util.List;


public class CountersHelper {

    public void weddingCountersDialog(Context context) {

        // LayoutInflater convert XML into view object in code.
        LayoutInflater li = LayoutInflater.from(context);

        // R.layout.wedding_details_layout - the XML layout file we want to inflate (convert).
        // the second value (optional)- a layout that would use as a parent for the promptsView in the ViewHierarchy.
        final View promptsView = li.inflate(R.layout.counters_layout, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alert.setCancelable(false); // doesn't disappear by click out side.
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();

        TextView comingGuestsTxt = promptsView.findViewById(R.id.coming_guest_txt);
        TextView totalGuestsTxt = promptsView.findViewById(R.id.total_guest_txt);
        TextView doneTasksTxt = promptsView.findViewById(R.id.done_tasks_txt);
        TextView totalTasksTxt = promptsView.findViewById(R.id.total_tasks_txt);
        TextView expensesTxt = promptsView.findViewById(R.id.expenses_txt);
        TextView totalBudgetTxt = promptsView.findViewById(R.id.total_budget_txt);

        // guests counters:
        List<GuestClass> guestsList = GuestListClass.getmArrGuest();

        int[] result = getStatusGuests(guestsList);
        comingGuestsTxt.setText("" + result[0]);
        totalGuestsTxt.setText("" + result[1]);

        // tasks counters:
        List<TaskClass> tasksList = TaskListPresenter.mTaskClassList;

        doneTasksTxt.setText("" + getStatusTasks(tasksList));
        totalTasksTxt.setText("" + tasksList.size());

        // suppliers counters:
        List<SupplierClass> suppliersList = SuppliersListPresenter.mSuppliersClassList;

        result = getStatusSuppliers(suppliersList, tasksList);
        expensesTxt.setText("" + result[0]);
        totalBudgetTxt.setText("" + result[1]);


        // positive_btn is the ok button.
        Button wedding_details_positive_btn = (Button) promptsView.findViewById(R.id.wedding_counters_positive_btn);
        wedding_details_positive_btn.setOnClickListener(view -> {
            dialog.dismiss(); // close dialog.
        });

        dialog.show();
    }


    public int[] getStatusGuests(List<GuestClass> guestsList) {
        int comingCounter = 0;
        int totalCounter = 0;
        int[] counters = new int[Constants.COUNTERS_ARR_SIZE];


        for (int i = 0; i < guestsList.size(); i++) {
            GuestClass guest = guestsList.get(i);
            if (guest.ismIsComing().equalsIgnoreCase("true")) {
                comingCounter += Integer.parseInt(guest.getmNumOfGuest());
            }
            totalCounter += Integer.parseInt(guest.getmNumOfGuest());
        }
        counters[0] = comingCounter;
        counters[1] = totalCounter;

        return counters;
    }


    public int getStatusTasks(List<TaskClass> tasksList) {
        int counter = 0;

        for (int i = 0; i < tasksList.size(); i++) {
            String done = tasksList.get(i).getmIsDone();
            if (done.equalsIgnoreCase("TRUE")) {
                counter++;
            }
        }
        return counter;
    }

    public int[] getStatusSuppliers(List<SupplierClass> suppList, List<TaskClass> tasksList) {
        int expensesCounter = 0;
        int budgetCounter = 0;
        int[] counters = new int[Constants.COUNTERS_ARR_SIZE];

        for (int i = 0; i < suppList.size(); i++) {
            SupplierClass supp = suppList.get(i);
            expensesCounter += Integer.parseInt(supp.getmPrice());
        }

        for (int i = 0; i < tasksList.size(); i++) {
            TaskClass task = tasksList.get(i);
            if (task.getmMyBudget() != null && task.getmMyBudget().length() > 0) {
                budgetCounter += Integer.parseInt(task.getmMyBudget());
            }
        }
        counters[0] = expensesCounter;
        counters[1] = budgetCounter;

        return counters;
    }
}
