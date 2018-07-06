package com.example.stefani.weddingplanner.Fragments.welcome;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stefani.weddingplanner.R;

import com.example.stefani.weddingplanner.Fragments.BaseFragmentClass;
import com.example.stefani.weddingplanner.Fragments.arrangment.SeatingArrangementFragment;
import com.example.stefani.weddingplanner.Fragments.tasklist.TaskListFragment;
import com.example.stefani.weddingplanner.Fragments.guestlist.GuestsListFragment;
import com.example.stefani.weddingplanner.Fragments.supplierlist.SuppliersListFragment;
import com.example.stefani.weddingplanner.Helpers.CountersHelper;
import com.example.stefani.weddingplanner.Helpers.SharedPreferencesHelper;
import com.example.stefani.weddingplanner.WeddingNotificationDetails;
import com.example.stefani.weddingplanner.BasicClasses.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WelcomeScreenFragment extends BaseFragmentClass implements IWelcomeScreen.View {


    @BindView(R.id.main_wedding_details)
    TextView mShowWeddingDetails;
    @BindView(R.id.main_wedding_counters)
    TextView mShowWeddingCounters;
    @BindView(R.id.headline)
    TextView mHeadline;
    @BindView(R.id.txtTimerHour)
    TextView txtTimerHour;
    @BindView(R.id.txtTimerMinute)
    TextView txtTimerMinute;
    @BindView(R.id.txtTimerSecond)
    TextView txtTimerSecond;
    @BindView(R.id.guests_btn)
    Button mGuestsBtn;
    @BindView(R.id.tasks_btn)
    Button mTasksBtn;
    @BindView(R.id.seating_btn)
    Button mSeatingBtn;
    @BindView(R.id.suppliers_btn)
    Button mSuppliersBtn;
    @BindView(R.id.txtTimerDay)
    TextView mTxtTimerDay;


    private View rootView;
    private IWelcomeScreen.Presenter mPresenter;
    private CountersHelper mCountersFragment;
    private WeddingNotificationDetails mWeddingNotificationDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.welcome_screen_frg, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter = new WelcomeScreenPresenter(this);

        rootView.getBackground().setAlpha(150);

        mWeddingNotificationDetails = new WeddingNotificationDetails();
        mCountersFragment = new CountersHelper();

        countDownTillWedding();

        return rootView;
    }


    @OnClick(R.id.main_wedding_details)
    public void showWeddingDetailsDialog() {
        mWeddingNotificationDetails.weddingDetailsDialog(getActivity());
    }


    @OnClick(R.id.main_wedding_counters)
    public void showWeddingCountersDialog() {
        mCountersFragment.weddingCountersDialog(getActivity());
    }

    private void addCoupleNames() {
        // read names of bride and groom from SharedPreferences:
        String groomName = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_GROOM_NAME);
        String brideName = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_BRIDE_NAME);

        if (!groomName.isEmpty() && !brideName.isEmpty()) {
            // get first name:
            String splitBrideName = brideName.split(" ", 2)[0];
            String splitGroomName = groomName.split(" ", 2)[0];
            mHeadline.setText(splitBrideName + " & " + splitGroomName);

        }
    }


    private void setTimeViability(boolean isVisible) {
        if (isVisible) {
            mTxtTimerDay.setVisibility(View.VISIBLE);
            txtTimerHour.setVisibility(View.VISIBLE);
            txtTimerMinute.setVisibility(View.VISIBLE);
            txtTimerSecond.setVisibility(View.VISIBLE);
        } else {
            mTxtTimerDay.setVisibility(View.INVISIBLE);
            txtTimerHour.setVisibility(View.INVISIBLE);
            txtTimerMinute.setVisibility(View.INVISIBLE);
            txtTimerSecond.setVisibility(View.INVISIBLE);
        }
    }


    private void countDownTillWedding() {
        Handler mHandler = new Handler(); // allows you to send and process Runnable objects associated with a single thread
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 1000); // add runnable to queue
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dateTime = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_DATE); // read wedding date from SharedPreferences
                    Date futureDate = dateFormat.parse(dateTime); // takes a string format and parses it to a date
                    Date currentDate = new Date();

                    setTimeViability(true);
                    addCoupleNames();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime() - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        mTxtTimerDay.setText("" + String.format("%02d", days));
                        txtTimerHour.setText("" + String.format("%02d", hours));
                        txtTimerMinute.setText("" + String.format("%02d", minutes));
                        txtTimerSecond.setText("" + String.format("%02d", seconds));
                    } else {
                        setTimeViability(false);
                    }
                } catch (Exception e) { // if there is no wedding date
                    setTimeViability(false);
                }
            }
        };
        mHandler.postDelayed(mRunnable, 1 * 1000); // run after the specified amount of time
    }


    @OnClick(R.id.tasks_btn)
    public void startTasksFragment() {
        TaskListFragment taskListFragment = new TaskListFragment();
        getmInteractFragments().setNewFragment(taskListFragment, taskListFragment.getClass().toString());
    }

    @OnClick(R.id.seating_btn)
    public void startSeatingArrangementFragment() {
        SeatingArrangementFragment seatingArrangementFragment = new SeatingArrangementFragment();
        getmInteractFragments().setNewFragment(seatingArrangementFragment, seatingArrangementFragment.getClass().toString());
    }

    @OnClick(R.id.guests_btn)
    public void startGuestsListFragment() {
        GuestsListFragment guestsListFragment = new GuestsListFragment();
        getmInteractFragments().setNewFragment(guestsListFragment, guestsListFragment.getClass().toString());
    }


    @OnClick(R.id.suppliers_btn)
    public void startSuppliersFragment() {
        SuppliersListFragment suppliersListFragment = new SuppliersListFragment();
        getmInteractFragments().setNewFragment(suppliersListFragment, suppliersListFragment.getClass().toString());
    }


    @Override
    public void initializeViews() {

    }
}
