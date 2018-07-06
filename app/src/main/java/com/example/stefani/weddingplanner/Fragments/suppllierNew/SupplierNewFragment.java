package com.example.stefani.weddingplanner.Fragments.suppllierNew;

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

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.Fragments.TakePictureInterface;
import com.example.stefani.weddingplanner.Helpers.ImageChooseHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SupplierNewFragment extends Fragment implements ISupplierNew.View {

    @BindView(R.id.supplier_company_name)
    EditText mCompanyName;
    @BindView(R.id.supplier_contact_name)
    EditText mContactName;
    @BindView(R.id.supplier_phone)
    EditText mPhone;
    @BindView(R.id.supplier_address)
    EditText mAddress;
    @BindView(R.id.supplier_price)
    EditText mPrice;
    @BindView(R.id.supplier_remarks)
    EditText mRemarks;
    @BindView(R.id.new_task_empty_fields)
    TextView mEmptyFields;
    @BindView(R.id.supplier_addDB)
    Button mAddToBtn;
    @BindView(R.id.supplier_image)
    ImageView supplierImage;
    @BindView(R.id.radio_group_paid)
    RadioGroup mRadioGroup;


    private ImageChooseHelper mImageChooserHelper;
    private Uri mSelectedImageUri;
    private Bitmap mBitmap;
    private ISupplierNew.Presenter mPresenter;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_new_supplier, container, false);
        ButterKnife.bind(this, mRootView);
        mPresenter = new SupplierNewPresenter(this);

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
        return mRootView;
    }

    private RadioButton getSelectedRadioButton() {
        int selected = mRadioGroup.getCheckedRadioButtonId();
        return (RadioButton) mRootView.findViewById(selected);
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
        }
    }


    private void onSelectFromGalleryResult(Intent data) {
        mBitmap = mImageChooserHelper.onSelectFromGalleryResult(data); // convert to bitmap - representation of an image
        Drawable drawable = new BitmapDrawable(getResources(), mBitmap); // change it into a drawable object so we can use as background
        supplierImage.setBackground(drawable);
    }


    private void onCaptureImageResult(Intent data) {
        mBitmap = mImageChooserHelper.onCaptureImageResult(data); // convert to bitmap - representation of an image
        getActivity().sendBroadcast(mImageChooserHelper.getPictureTakenDestination()); // send broadcast request - request the media scanner to scan a file and add it to the media database.
        mSelectedImageUri = mImageChooserHelper.mContentUri; // get uri of the file's location
        Drawable drawable = new BitmapDrawable(getResources(), mBitmap); // change it into a drawable object so we can use as background
        supplierImage.setBackground(drawable);
    }


    @OnClick(R.id.supplier_image)
    public void onSupplierImageClick() {
        checkPermissions();
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
    public void clearFields() {
        mCompanyName.setText("");
        mContactName.setText("");
        mPhone.setText("");
        mAddress.setText("");
        mPrice.setText("");
        mRemarks.setText("");
        supplierImage.setBackgroundResource(R.drawable.placeholder_image);
    }


    @OnClick(R.id.supplier_addDB)
    public void addSupplierClick() {
        // get required fields from edit fields:
        String companyName = mCompanyName.getText().toString().trim();
        String price = mPrice.getText().toString().trim();

        // if company name and price are not empty:
        if (companyName.length() > 0 && price.length() > 0) {
            mEmptyFields.setVisibility(View.INVISIBLE);
            String paid = getSelectedRadioButton().getText().toString();
            String contactName = mContactName.getText().toString().trim();
            String phone = mPhone.getText().toString().trim();
            String address = mAddress.getText().toString().trim();
            String remarks = mRemarks.getText().toString().trim();

            mPresenter.addSupplierToDB(companyName, price, paid, contactName, phone, address, remarks);

            Toast.makeText(getContext(), R.string.str_added_supplier, Toast.LENGTH_SHORT).show();
        } else {
            mEmptyFields.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void saveImageToStorage() { // insert image to storage
        if (mSelectedImageUri != null) {
            mPresenter.saveImageToStorage(mSelectedImageUri);
        }
    }


    @Override
    public void initializeViews() {

    }

    @Override
    public void insertImageToMedia(String getmID) {
        // content resolver defines read write permission for URI.
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, getmID, getmID); // save image to media, image name and description are both the id
    }
}
