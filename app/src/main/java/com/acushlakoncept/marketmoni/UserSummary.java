package com.acushlakoncept.marketmoni;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.out;

public class UserSummary extends AppCompatActivity {

    private final AppCompatActivity myContext = UserSummary.this;

    Dialog myDialog;
    AppCompatTextView userName, userSex, userAddress, userPhone, userEmail,
            userType, userLocation, userArea, userDate;

    AppCompatButton saveButton, addButton;
    Utils myUtils;

    private AgentDatabaseHelper databaseHelper;
    private ImageView userImage;
    private HashMap<String, String> userInfo;
    String emailFromIntent;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    Bitmap bp = null;

    // Session Manager Class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_summary);


        initViews();
        initObjects();
        initListeners();

        //Toast.makeText(myContext, "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();




    }

    public void initViews(){
        userName = (AppCompatTextView)findViewById(R.id.userName);
        userSex = (AppCompatTextView)findViewById(R.id.userSex);
        userAddress = (AppCompatTextView)findViewById(R.id.userAddress);
        userPhone = (AppCompatTextView)findViewById(R.id.userPhone);
        userEmail = (AppCompatTextView)findViewById(R.id.userEmail);
        //userType = (AppCompatTextView)findViewById(R.id.userType);
       // userLocation = (AppCompatTextView)findViewById(R.id.userLocation);
        userDate = (AppCompatTextView)findViewById(R.id.userDate);
        //userArea = (AppCompatTextView)findViewById(R.id.userArea);
        userImage = (ImageView)findViewById(R.id.userImageFromDb);
        saveButton = (AppCompatButton)findViewById(R.id.appCompatButtonExportData);
        addButton = (AppCompatButton)findViewById(R.id.appCompatButtonAddBeneficiary);
    }

    public void initObjects(){

//        HashMap<String, String> user = session.getUserDetails();
//        // email
//        String userSessionEmail = user.get(session.KEY_EMAIL);

        emailFromIntent = getIntent().getStringExtra("EMAIL");
        databaseHelper = new AgentDatabaseHelper(myContext);
        userInfo = databaseHelper.getAllAgentInfo(emailFromIntent);

        userName.setText(Html.fromHtml(colorGreen("Name: ") + userInfo.get("Name")));
        userSex.setText(Html.fromHtml(colorGreen("Gender: ") + userInfo.get("Sex")));
        userAddress.setText(Html.fromHtml(colorGreen("Address: ") + userInfo.get("Address")));
        userPhone.setText(Html.fromHtml(colorGreen("Phone: ") + userInfo.get("Phone")));
//        userType.setText(Html.fromHtml(colorGreen("Business Type: ") + userInfo.get("Type")));
//        userLocation.setText(Html.fromHtml(colorGreen("Business Location: ") + userInfo.get("Location")));
        userEmail.setText(Html.fromHtml(colorGreen("Email: ") + userInfo.get("Email")));
//        userArea.setText(Html.fromHtml(colorGreen("Business Area: ") + userInfo.get("Area")));
        userDate.setText(Html.fromHtml(colorGreen("Registration Date: ") + userInfo.get("Date")));

        myDialog = new Dialog(myContext);
        myUtils = new Utils(myContext);

        Bitmap bitmap = databaseHelper.getBitmapImage(emailFromIntent);
        bp = bitmap;
        userImage.setImageBitmap(bitmap);

        String userName = userInfo.get("Name");
        String[] fn = userName.split(" ");

        getSupportActionBar().setTitle("Welcome " + fn[0]);

    }

   public void initListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToSDCard();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agentIntent = new Intent(myContext, BeneficialSignUp.class);
                agentIntent.putExtra("AgentEmail", userInfo.get("Email"));
                startActivity(agentIntent);
            }
        });
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        MenuItem aboutItem = menu.findItem(R.id.action_about);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                //Toast.makeText(myContext, "Logout Button", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(myContext, LoginActivity.class));
                finish();
                return true;
            case R.id.action_about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showAbout() {
        TextView txtclose;
        myDialog.setContentView(R.layout.about);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public String colorGreen(String text){
        return "<font color='#35b046'>" + text + "</font>";
    }

    public void saveDataToSDCard(){
        ActivityCompat.requestPermissions(UserSummary.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if (myUtils.isSDCardMounted()){
            // Path to sd card
            File path = Environment.getExternalStorageDirectory();

            //Toast.makeText(myContext, "You clicked save button", Toast.LENGTH_SHORT).show();

            //create a folder
            File dir = new File(path+"/marketmoni/");
            dir.mkdir();

            String[] str = emailFromIntent.split("@");
            String newName = str[0];

            File imageFile = new File(dir,newName+ "_moni.jpg");
            File textFile = new File(dir, newName+"_info.txt");

            OutputStream  outputStream = null;
            OutputStream txtOutputStream = null;
            Bitmap bitmapToExport = bp;

            try {
                //file.createNewFile();
                outputStream = new FileOutputStream(imageFile);

                textFile.createNewFile();
                txtOutputStream = new FileOutputStream(textFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(txtOutputStream);
                outputStreamWriter.append(userName.getText());
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append(userSex.getText());
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append(userAddress.getText());
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append(userPhone.getText());
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append(userEmail.getText());
                outputStreamWriter.append("\r\n");
                outputStreamWriter.append(userDate.getText());
                outputStreamWriter.close();
                txtOutputStream.close();

//                Bitmap bitmapToExport = ((BitmapDrawable)
//                        userImage.getDrawable()).getBitmap();
               // bm.compress(Bitmap.CompressFormat.JPEG, 0, out);
                bitmapToExport.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                alert.showAlertDialog(myContext, "Success", "Data has success fully been exported to " + dir, true);
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            alert.showAlertDialog(myContext, "Error", "Insert SDCard on Device to continue", false);
        }

    }
}
