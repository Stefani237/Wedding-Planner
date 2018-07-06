package com.example.stefani.weddingplanner.Fragments.guestlist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.WeddingPlannerApp;
import com.example.stefani.weddingplanner.Adapters.GuestsBaseFirebaseAdapter;

import com.example.stefani.weddingplanner.Fragments.BaseFragmentClass;
import com.example.stefani.weddingplanner.Fragments.guestDetails.GuestDetailsFragment;
import com.example.stefani.weddingplanner.Fragments.guestNew.GuestNewFragment;
import com.example.stefani.weddingplanner.SMS.ISmsResponse;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuestsListFragment extends BaseFragmentClass implements AdapterView.OnItemSelectedListener, IGuestsList.View {

    @BindView(R.id.list_filter)
    Spinner mListFilter;
    @BindView(R.id.addGuest)
    Button mAddBtn;
    @BindView(R.id.send_sms_btn)
    Button sendSmsBtn;
    @BindView(R.id.imageView)
    ImageView mLoader;
    @BindView(R.id.guestsListView)
    RecyclerView list;


    private GuestsBaseFirebaseAdapter mBaseFirebaseAdapter;
    private View rootView;
    private IGuestsList.Presenter mPresenter;
    private TextView receiverDetails;


    // Get permission from the user to access SMS.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_SEND_SMS:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showLoader();
                    mPresenter.sendSMS();
                } else {
                    toastSmsNotSent();
                    return;
                }
        }
    }

    @OnClick(R.id.send_sms_btn)
    public void onSendSmsClick() {
        if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            // if there is no permission
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS}, Constants.PERMISSION_SEND_SMS);
            //  ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS}, Constants.PERMISSION_SEND_SMS);
            this.requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, Constants.PERMISSION_SEND_SMS);
        } else {
            showLoader();
            mPresenter.sendSMS();
        }
    }

    private void showLoader() {
        mLoader.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoader.getDrawable();
        animationDrawable.start();

        Handler handler = new Handler(); // allows you to send and process Runnable objects associated with a single thread
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoader.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    private ISmsResponse iSmsResponse = new ISmsResponse() {
        @Override
        public void smsResponse(String phoneNumber, Integer numberOfGuests) {
            mPresenter.updateListAccordingSMS(phoneNumber, numberOfGuests);
        }

        @Override
        public void smsErrorResponse(String phoneNumber) {
            mPresenter.wrongMessageFromGuest(phoneNumber);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_guests_list, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter = new GuestsListPresenter(this);
        mPresenter.initializeViews();

        WeddingPlannerApp.getSmsBroadcastReceiver().setSmsListener(iSmsResponse);

        mListFilter.setOnItemSelectedListener(this);

        return rootView;
    }


    private void onRowClick() {
        mBaseFirebaseAdapter.setmOnItemClickListener(new GuestsBaseFirebaseAdapter.OnItemClickListener() {
            @Override
            public void onItemLongListener(GuestClass mGuest) {
                longPressDeleteDialog(mGuest);
            }

            @Override
            public void onItemClickListener(GuestClass mGuest) {
                mPresenter.startGuestDetailFragment(mGuest);

            }
        });
    }

    @OnClick(R.id.addGuest)
    public void addNewGuestClick() {
        GuestNewFragment newGuestFragment = new GuestNewFragment();
        getmInteractFragments().setNewFragment(newGuestFragment, newGuestFragment.getClass().toString());
    }


    private void createDropDown() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.guest_sort_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListFilter.setAdapter(myAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        int positionFilter = (int) id; // get selected value from filter
        mPresenter.onItemSelected(positionFilter); // update list
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    private void longPressDeleteDialog(final GuestClass guestClass) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.str_delete_title);
        alert.setMessage(R.string.str_delete_msg_guest);
        alert.setPositiveButton(R.string.str_delete_positive, (dialog, which) -> {

            mPresenter.removeGuest(guestClass.getmID());

            dialog.dismiss();

        });
        alert.setNegativeButton(R.string.str_delete_negative, (dialog, which) -> dialog.dismiss());
        alert.show();

    }


    @Override
    public void showWrongMessage(String senderName) {
        LayoutInflater li = LayoutInflater.from(getContext());
        // R.layout.sms_error_message - the XML layout file we want to inflate (convert).
        // the second value (optional)- a layout that would use as a parent for the promptsView in the ViewHierarchy.
        final View promptsView = li.inflate(R.layout.sms_error_message, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        alert.setCancelable(false);// doesn't disappear by click out side.
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();

        Button error_msg_sms = promptsView.findViewById(R.id.error_msg_sms);
        receiverDetails = promptsView.findViewById(R.id.receiver_details);
        receiverDetails.setText(senderName);

        error_msg_sms.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void initializeViews(List<GuestClass> mGuestClassList) {
        mLoader.setImageResource(R.drawable.loader_animation);

        mBaseFirebaseAdapter = new GuestsBaseFirebaseAdapter(mGuestClassList);
        list.setHasFixedSize(true); // performs several optimizations if recyclerView's size is not affected by the adapter contents.
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL)); // affects drawing of item views
        list.setItemAnimator(new DefaultItemAnimator()); // sets animator that will handle animations involving changes to the items in this recyclerView.
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(mBaseFirebaseAdapter);

        onRowClick();
        createDropDown();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void updateAdapterData(List<GuestClass> guestClassList) {
        mBaseFirebaseAdapter.updateData(guestClassList); // update list view
    }

    @Override
    public void setNewFragment(GuestDetailsFragment guestsListFragment, String string) { // switch to another fragment
        getmInteractFragments().setNewFragment(guestsListFragment, string);
    }

    @Override
    public void toastSmsSent() {
        Toast.makeText(getContext(), R.string.str_sms_succeed, Toast.LENGTH_LONG).show();

    }

    @Override
    public void toastSmsNotSent() {
        Toast.makeText(getContext(), R.string.str_sms_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void toastEmptyList() {
        Toast.makeText(getContext(), R.string.empty_list, Toast.LENGTH_LONG).show();
    }
}
