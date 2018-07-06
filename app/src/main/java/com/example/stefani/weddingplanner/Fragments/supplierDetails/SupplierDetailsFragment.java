package com.example.stefani.weddingplanner.Fragments.supplierDetails;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.Fragments.TakePictureInterface;
import com.example.stefani.weddingplanner.Helpers.ImageChooseHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SupplierDetailsFragment extends Fragment implements ISupplierDetails.View {
    @BindView(R.id.supplier_company_name)
    EditText mSuppCompanyName;
    @BindView(R.id.supplier_contact_name)
    EditText mSuppContactName;
    @BindView(R.id.supplier_phone)
    EditText mSuppPhone;
    @BindView(R.id.supplier_address)
    EditText mSuppAddress;
    @BindView(R.id.supplier_price)
    EditText mSuppPrice;
    @BindView(R.id.supplier_remarks)
    EditText mSuppRemarks;
    @BindView(R.id.supplier_image)
    ImageView mSupplierImage;
    @BindView(R.id.sup_img_icon)
    ImageView mSupplierImgIcon;
    @BindView(R.id.supplier_details_save)
    Button mSaveBtn;
    @BindView(R.id.supplier_details_edit)
    Button mEditBtn;
    @BindView(R.id.task_details_empty_fields)
    TextView mEmptyFields;
    @BindView(R.id.radio_group_paid)
    RadioGroup mSuppPaid;
    @BindView(R.id.radio_btn_yes)
    RadioButton mPaid;
    @BindView(R.id.radio_btn_no)
    RadioButton mNotPaid;


    private ISupplierDetails.Presenter mPresenter;
    private SupplierClass mSupplierDetails;
    private ImageChooseHelper mImageChooserHelper;
    private Uri mSelectedImageUri;
    private Bitmap mBitmap;
    private View mRootView;


    public void setSupplierDetails(SupplierClass suppDetails) {
        this.mSupplierDetails = suppDetails;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.supplier_detailes_layout, container, false);

        ButterKnife.bind(this, mRootView);
        mPresenter = new SupplierDetailsPresenter(this);
        mPresenter.setSupplier(mSupplierDetails);
        mPresenter.initializeViews();

        mImageChooserHelper = new ImageChooseHelper(getContext(), new TakePictureInterface() {
            @Override
            public void createGalleryIntent(Intent intent) {
                // getting a result from an activity for intent select from gallery
                startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
            }

            @Override
            public void createCameraIntent(Intent intent) {
                // getting a result from an activity for intent take a picture
                startActivityForResult(intent, Constants.REQUEST_CAMERA);
            }
        });

        mPresenter.getSupplierImageFromDB();
        Toast.makeText(getContext(), R.string.edit_mode, Toast.LENGTH_SHORT).show();

        return mRootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // no permission
          // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            mImageChooserHelper.selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: // my request
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission allowed
                    mImageChooserHelper.selectImage();
                } else {
                    return;
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // select what to do when receiving a result
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) { // if result were successfully received
            mSelectedImageUri = data.getData(); // get uri from the data result
            if (requestCode == Constants.SELECT_FILE) // if action is select from gallery
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA) // if action is take a picture
                onCaptureImageResult(data);

            setSupplierImage(mSelectedImageUri); // update new image
        }
    }


    private void onSelectFromGalleryResult(Intent data) {
        mBitmap = mImageChooserHelper.onSelectFromGalleryResult(data); // convert to bitmap - representation of an image
        Drawable drawable = new BitmapDrawable(getResources(), mBitmap); // change it into a drawable object so we can use as background
        mSupplierImage.setBackground(drawable);
    }


    private void onCaptureImageResult(Intent data) {
        mBitmap = mImageChooserHelper.onCaptureImageResult(data); // convert to bitmap - representation of an image
        mSelectedImageUri = mImageChooserHelper.mContentUri; // get uri of the file's location
        Drawable drawable = new BitmapDrawable(getResources(), mBitmap); // change it into a drawable object so we can use as background
        mSupplierImage.setBackground(drawable);
    }


    @OnClick(R.id.supplier_image)
    public void onSupplierImageClicked() {
        checkPermissions();
    }


    @OnClick(R.id.supplier_details_edit)
    public void supplierEditClick() {
        mSuppCompanyName.setEnabled(true);
        mSuppContactName.setEnabled(true);
        mSuppPhone.setEnabled(true);
        mSuppAddress.setEnabled(true);
        mSuppPrice.setEnabled(true);
        mSuppRemarks.setEnabled(true);
        mSuppPaid.setEnabled(true);
        mSaveBtn.setEnabled(true);
        mSupplierImgIcon.setEnabled(true);
        mSupplierImage.setEnabled(true);
    }


    @OnClick(R.id.supplier_details_save)
    public void saveButtonClick() {
        saveSupplierNewData();
    }


    private void saveSupplierNewData() {

        // get new data from fields:
        String suppCompanyName = mSuppCompanyName.getText().toString().trim();
        String suppSuppContactName = mSuppContactName.getText().toString().trim();
        String shone = mSuppPhone.getText().toString().trim();
        String suppAddress = mSuppAddress.getText().toString().trim();
        String suppPrice = mSuppPrice.getText().toString().trim();
        String suppRemarks = mSuppRemarks.getText().toString().trim();
        String btmUrl = mImageChooserHelper.encodeBitmap(mBitmap);
        String paid = getSelectedRadioButton().getText().toString();

        // create new supplier with new data
        SupplierClass supplierClass = new SupplierClass(suppCompanyName, suppPrice);
        supplierClass.setImgUrl(btmUrl);
        supplierClass.setmPhone(shone);
        supplierClass.setmContactName(suppSuppContactName);
        supplierClass.setmAddress(suppAddress);
        supplierClass.setmRemarks(suppRemarks);
        supplierClass.setmPaid(paid);

        mPresenter.saveSupplierToDB(supplierClass);
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, supplierClass.getmID(), supplierClass.getmID());

        mPresenter.saveImageToStorage(mSelectedImageUri);
    }

    private RadioButton getSelectedRadioButton() {
        int selected = mSuppPaid.getCheckedRadioButtonId();
        return (RadioButton) mRootView.findViewById(selected);
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void setSupplierImage(Uri uri) {
        Glide // image Loader Library
                .with(getContext())
                .load(uri) // the uri received from Firebase
                .centerCrop()
                .into(mSupplierImage); // insert to image view holder
    }

    @Override
    public void setSuppCompanyName(String companyName) {
        mSuppCompanyName.setText(companyName);

    }

    @Override
    public void setContactName(String contactName) {
        mSuppContactName.setText(contactName);

    }

    @Override
    public void setSuppPhone(String suppPhone) {
        mSuppPhone.setText(suppPhone);

    }

    @Override
    public void setSuppAddress(String address) {
        mSuppAddress.setText(address);
    }

    @Override
    public void setSuppPrice(String price) {
        mSuppPrice.setText(price);
    }

    @Override
    public void setSuppRemarks(String remarks) {
        mSuppRemarks.setText(remarks);
    }

    @Override
    public void setPaidChecked(boolean isPaid) {
        mPaid.setChecked(isPaid);
    }

    @Override
    public void setNotPaid(boolean isNotPaid) {
        mNotPaid.setChecked(isNotPaid);
    }

    @Override
    public void setEmptyFieldVisibility(int visible) {
        mEmptyFields.setVisibility(visible);
    }

    @Override
    public void toastSupplierDetailsSaved() {
        Toast.makeText(getContext(), R.string.str_changes_saved, Toast.LENGTH_SHORT).show();
    }
}
