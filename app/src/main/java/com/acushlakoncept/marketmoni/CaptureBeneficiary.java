package com.acushlakoncept.marketmoni;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;

public class CaptureBeneficiary extends AppCompatActivity {

    private final AppCompatActivity activity = CaptureBeneficiary.this;

    private ImageView imageViewCaptured;
    private AppCompatButton btnCaptureImage;
    private AppCompatButton btnRegisterBeneficiary;

    private DatabaseHelper databaseHelper;
    private User user;
    private String nameIntent, sexIntent, addressIntent, phoneIntent, typeIntent, locationIntent, areaIntent, emailIntent;

    private final int SELECT_PHOTO = 101;
    private final int CAPTURE_PHOTO = 102;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    Uri uri;
    Uri selectedImagePath;
    byte[] storableImage;
    Utils myUtils;
    AlertDialogManager alert;
    Bitmap bp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_beneficiary);

        initViews();
        initObjects();
        initListeners();
    }

    private void initViews() {

        imageViewCaptured = (ImageView) findViewById(R.id.beneficiaryImageCaptured);
        btnCaptureImage = (AppCompatButton)findViewById(R.id.btnCaptureBeneficiaryImage);

        btnRegisterBeneficiary = (AppCompatButton) findViewById(R.id.btn_Register_Benefeciary);

    }

    private void initObjects(){

        user = new User();
        databaseHelper = new DatabaseHelper(activity);

        myUtils = new Utils(activity);

        getIntentsFromSignUp();
        alert = new AlertDialogManager();
    }

    private void getIntentsFromSignUp() {
        nameIntent = getIntent().getStringExtra("Name");
        sexIntent = getIntent().getStringExtra("Sex");
        addressIntent = getIntent().getStringExtra("Address");
        phoneIntent = getIntent().getStringExtra("Phone");
        typeIntent = getIntent().getStringExtra("Type");
        locationIntent = getIntent().getStringExtra("Location");
        areaIntent = getIntent().getStringExtra("Area");
        emailIntent = getIntent().getStringExtra("Email");
    }

    private void initListeners(){

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(activity, "You are clicking me! stop it!", Toast.LENGTH_SHORT).show();
                listDialogue();
                //postDataToSQLite();
                //saveDataToSDCard();
            }
        });

        btnRegisterBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToSQLite();
                saveDataToSDCard();
                finish();
            }
        });
    }

    private void postDataToSQLite() {


        if (!databaseHelper.isBeneficiaryRegistered(phoneIntent)) {

            user.setName(nameIntent);
            user.setSex(sexIntent);
            user.setAddress(addressIntent);
            user.setPhone(phoneIntent);
            user.setEmail(emailIntent);
            user.setImageUrl(storableImage);
            user.setBizType(typeIntent);
            user.setBizLocation(locationIntent);
            user.setBizArea(areaIntent);
            user.setDateTime(myUtils.getCurrentTime());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            //Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            Toast.makeText(activity, nameIntent+" "+sexIntent+" "+addressIntent+" "+phoneIntent+" "+typeIntent
                    +" "+locationIntent+" "+areaIntent+" "+myUtils.getCurrentTime(), Toast.LENGTH_SHORT).show();
//            emptyInputEditText();

            alert.showAlertDialog(activity, "Success", getString(R.string.success_message), true);


        } else {
            // Snack Bar to show error message that record already exists
            //Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
            //Toast.makeText(activity, getString(R.string.error_email_exists), Toast.LENGTH_SHORT).show();
            alert.showAlertDialog(activity, "Error", getString(R.string.success_message), false);
        }
    }

    public void listDialogue(){
        checkForPermissions();
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
                    bp = imageReturnedIntent.getExtras().getParcelable("data");
                    //bp = bmp;
                    storableImage = myUtils.processBlob(bp);
                    imageViewCaptured.setImageBitmap(bp);
                }
                break;        }
    }

    public void saveDataToSDCard(){
        ActivityCompat.requestPermissions(CaptureBeneficiary.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if (myUtils.isSDCardMounted()){
            // Path to sd card
            File path = Environment.getExternalStorageDirectory();

            //Toast.makeText(myContext, "You clicked save button", Toast.LENGTH_SHORT).show();

            //create a folder
            File dir = new File(path+"/marketmoni/");
            dir.mkdir();

            String[] str = nameIntent.split(" ");
            String newName = str[0];

            File imageFile = new File(dir,newName+myUtils.getToken(5)+ "_moni.jpg");
            File textFile = new File(dir, newName+myUtils.getToken(5)+"_info.txt");

            OutputStream outputStream = null;
            OutputStream txtOutputStream = null;
            Bitmap bitmapToExport = bp;

            try {
                //file.createNewFile();
                outputStream = new FileOutputStream(imageFile);

                textFile.createNewFile();
                txtOutputStream = new FileOutputStream(textFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(txtOutputStream);
                outputStreamWriter.append("Beneficiary Name: "+nameIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Gender: "+sexIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Address: "+ addressIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Phone: "+ phoneIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Business Type: "+ typeIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Business Location: "+ locationIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Business Area: "+ areaIntent);
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append("Registration Date/Time: "+ myUtils.getCurrentTime());
                outputStreamWriter.close();
                txtOutputStream.close();

                bitmapToExport.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                alert.showAlertDialog(activity, "Success", "Data has success fully been exported to " + dir, true);
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            alert.showAlertDialog(activity, "Error", "Insert SDCard on Device to continue", false);
        }

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
