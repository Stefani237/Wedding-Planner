package com.example.stefani.weddingplanner.Fragments.supplierlist;

import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;

import java.util.List;

public interface ISuppliersList {

    interface Presenter {

        void initializeViews();

        void deleteSupplier(String getmID);

        void onItemSortSelectedClick(int positionFilter);
    }


    interface View {

        void initializeViews(List<SupplierClass> mSuppliersClassList);

        void updateAdapter(List<SupplierClass> filteredSuppliers);
    }

}


