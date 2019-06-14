package com.acushlakoncept.marketmoni;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private RadioGroup radioGroupSex;
    private RadioButton radioSexButton;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutSex;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
   // private TextInputLayout textInputLayoutTypeOfBiz;

    //private Spinner spinnerMarketLocation, spinnerMarketArea;
    private String userBusinessType, userSelectedMarketLocation, userSelectedMarketArea, emailFromIntent;

    private ImageView imageViewCaptured;
    private AppCompatButton imageCaptureButton;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextAddress;
    private TextInputEditText textInputEditTextPhone;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    //private TextInputEditText textInputEditTextBizType;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private AgentDatabaseHelper databaseHelper;
    private User user;

    private final int SELECT_PHOTO = 101;
    private final int CAPTURE_PHOTO = 102;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    Uri uri;
    Uri selectedImagePath;
    byte[] storableImage;
    Utils myUtils;
    AlertDialogManager alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        imageCaptureButton = (AppCompatButton)findViewById(R.id.appCompatButtonCaptureImage);
        imageViewCaptured = (ImageView) findViewById(R.id.imgCaptured);
        radioGroupSex = (RadioGroup) findViewById(R.id.radioSex);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutSex = (TextInputLayout) findViewById(R.id.textInputLayoutSex);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.textInputLayoutAddress);
        textInputLayoutPhone = (TextInputLayout) findViewById(R.id.textInputLayoutPhone);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
       // textInputLayoutTypeOfBiz = (TextInputLayout)findViewById(R.id.textInputLayoutTypeOfBiz);
       // spinnerMarketLocation = (Spinner)findViewById(R.id.spinnerMarketLocation);
       // spinnerMarketArea = (Spinner)findViewById(R.id.spinnerMarketArea);
       // textInputEditTextBizType = (TextInputEditText)findViewById(R.id.textInputEditTextTypeOfBiz);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextPhone = (TextInputEditText) findViewById(R.id.textInputEditTextPhone);
        textInputEditTextAddress = (TextInputEditText) findViewById(R.id.textInputEditTextAddress);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    //This method is to initialize listeners
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        imageCaptureButton.setOnClickListener(this);
    }

    //This method is to initialize objects to be used
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new AgentDatabaseHelper(activity);
        user = new User();
        myUtils = new Utils(activity);
        alert = new AlertDialogManager();

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;
            case R.id.appCompatButtonCaptureImage:
                checkForPermissions();
                listDialogue();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }


    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutPhone, getString(R.string.error_phone))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextAddress, textInputLayoutAddress, getString(R.string.error_address))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if(storableImage != null) {

            if (!databaseHelper.checkAgent(textInputEditTextEmail.getText().toString().trim())) {

                // get selected radio button from radioGroup
                int selectedId = radioGroupSex.getCheckedRadioButtonId();
                // userSelectedMarketArea = spinnerMarketArea.getSelectedItem().toString();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);

                user.setName(textInputEditTextName.getText().toString().trim());
                user.setSex(radioSexButton.getText().toString());
                user.setAddress(textInputEditTextAddress.getText().toString().trim());
                user.setPhone(textInputEditTextPhone.getText().toString().trim());
                user.setEmail(textInputEditTextEmail.getText().toString().trim());
                user.setPassword(textInputEditTextPassword.getText().toString().trim());
                user.setImageUrl(storableImage);
                user.setDateTime(myUtils.getCurrentTime());

                databaseHelper.addAgent(user);

                // Snack Bar to show success message that record saved successfully
                //Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                alert.showAlertDialog(activity, "Success", getString(R.string.success_message), true);
                //Toast.makeText(activity, getString(R.string.success_message), Toast.LENGTH_SHORT).show();
                myUtils.emptyInputEditText(textInputEditTextName, textInputEditTextAddress, textInputEditTextEmail,
                        textInputEditTextPhone, textInputEditTextPassword, textInputEditTextConfirmPassword);


            } else {
                // Snack Bar to show error message that record already exists
                //Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
                //Toast.makeText(activity, getString(R.string.error_email_exists), Toast.LENGTH_SHORT).show();
                alert.showAlertDialog(activity, "Error", getString(R.string.error_email_exists), false);

            }

        } else {
            alert.showAlertDialog(activity, "Error", "Please Upload Photo", false);
        }



    }

    public void listDialogue(){
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter.add("Take Photo");
        //arrayAdapter.add("Select Gallery");

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setBackgroundColor(activity.getResources().getColor(R.color.colorBackground));
        listView.setAdapter(arrayAdapter);

        final MaterialDialog alert = new MaterialDialog(this).setContentView(listView);

        alert.setPositiveButton("Cancel", new View.OnClickListener() {
            @Override public void onClick(View v) {
                alert.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                    alert.dismiss();
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + UUID.randomUUID() + "marketmoni.jpg";
                    uri = Uri.parse(root);
                    //i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    selectedImagePath = uri;
                    startActivityForResult(i, CAPTURE_PHOTO);

                }else {

                    alert.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                }
            }
        });

        alert.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = imageReturnedIntent.getData();
                    //selectedImagePath = getPath(imageUri);
                    File newImageFile = new File(getPath(imageUri));
                    Picasso.with(activity).load(Uri.fromFile(newImageFile)).into(imageViewCaptured);
                }
                break;

            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = imageReturnedIntent.getExtras().getParcelable("data");
                    storableImage = myUtils.processBlob(bmp);
                    imageViewCaptured.setImageBitmap(bmp);
                }
                break;        }
    }


    private void checkForPermissions() {
        int hasWriteStoragePermission = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasWriteStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_STORAGE);
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

}

