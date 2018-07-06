
package com.example.stefani.weddingplanner.Fragments.supplierlist;

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
import com.example.stefani.weddingplanner.Adapters.SuppliersBaseFirebaseAdapter;

import com.example.stefani.weddingplanner.Fragments.BaseFragmentClass;
import com.example.stefani.weddingplanner.Fragments.suppllierNew.SupplierNewFragment;
import com.example.stefani.weddingplanner.Fragments.supplierDetails.SupplierDetailsFragment;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SuppliersListFragment extends BaseFragmentClass implements AdapterView.OnItemSelectedListener, ISuppliersList.View {

    @BindView(R.id.supplier_sort)
    Spinner mListFilter;
    @BindView(R.id.addSupplier)
    Button mAddBtn;
    @BindView(R.id.suppliersListView)
    RecyclerView list;

    private SuppliersBaseFirebaseAdapter mBaseFirebaseAdapter;
    private ISuppliersList.Presenter mPresenter;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_suppliers_list, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter = new SuppliersListPresenter(this);
        mPresenter.initializeViews();


        mListFilter.setOnItemSelectedListener(this);

        return rootView;
    }

    @OnClick(R.id.addSupplier)
    public void addNewSupplier() { // go to new supplier fragment
        SupplierNewFragment newSupplierFragment = new SupplierNewFragment();
        getmInteractFragments().setNewFragment(newSupplierFragment, newSupplierFragment.getClass().toString());

    }

    private void createDropDown() { // set adapter to the view drop down filter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supplier_sort_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListFilter.setAdapter(myAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        int positionFilter = (int) id; // get id of the selected item
        mPresenter.onItemSortSelectedClick(positionFilter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void startSupplierDetailFragment(SupplierClass supplier) {
        //go to supplier details fragment
        SupplierDetailsFragment taskDetailsFragment = new SupplierDetailsFragment();
        taskDetailsFragment.setSupplierDetails(supplier); // pass selected supplier's details to details fragment
        getmInteractFragments().setNewFragment(taskDetailsFragment, taskDetailsFragment.getClass().toString());
    }

    private void longPressDeleteDialog(final SupplierClass supplier) {
        // show delete dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.str_delete_title);
        alert.setMessage(R.string.str_delete_msg_supplier);
        alert.setPositiveButton(R.string.str_delete_positive, (dialog, which) -> {
            mPresenter.deleteSupplier(supplier.getmID()); // delete supplier from firebase and storage
            dialog.dismiss();

        });
        alert.setNegativeButton(R.string.str_delete_negative, (dialog, which) -> dialog.dismiss());
        alert.show();

    }

    @Override
    public void initializeViews(List<SupplierClass> mSuppliersClassList) {
        mBaseFirebaseAdapter = new SuppliersBaseFirebaseAdapter(mSuppliersClassList);
        list.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(mBaseFirebaseAdapter);
        createDropDown();
        onRowClick();
    }

    @Override
    public void updateAdapter(List<SupplierClass> filteredSuppliers) {
        mBaseFirebaseAdapter.updateData(filteredSuppliers); // update list view according to adapter
    }

    private void onRowClick() {
        mBaseFirebaseAdapter.setmOnItemClickListener(new SuppliersBaseFirebaseAdapter.OnItemClickListener() {

            @Override
            public void onItemLongListener(SupplierClass mSupplier) { // on long click - delete record
                longPressDeleteDialog(mSupplier);
            }

            @Override
            public void onItemClickListener(SupplierClass mSupplier) { // on click - go to details fragment
                startSupplierDetailFragment(mSupplier);
            }
        });
    }
}


