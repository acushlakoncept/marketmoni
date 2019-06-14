package com.acushlakoncept.marketmoni;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class BeneficialSignUp extends AppCompatActivity {

    private final AppCompatActivity activity = BeneficialSignUp.this;


    private RadioGroup radioGroupSex;
    private RadioButton radioSexButton;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutSex;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutTypeOfBiz;

    private Spinner spinnerMarketLocation, spinnerMarketArea;
    private String userBusinessType, userSelectedMarketLocation, userSelectedMarketArea, emailFromIntent;

    private ImageView imageViewCaptured;
    private AppCompatButton imageCaptureButton;
    private AppCompatButton appCompatButtonNext;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextAddress;
    private TextInputEditText textInputEditTextPhone;
    private TextInputEditText textInputEditTextBizType;

    private InputValidation inputValidation;

    Utils myUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficial_sign_up);

        initViews();
        initListeners();
        initObjects();
    }



    private void initViews() {

        imageCaptureButton = (AppCompatButton)findViewById(R.id.appCompatButtonCaptureImage);
        imageViewCaptured = (ImageView) findViewById(R.id.imgCaptured);
        radioGroupSex = (RadioGroup) findViewById(R.id.radioSex);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutSex = (TextInputLayout) findViewById(R.id.textInputLayoutSex);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.textInputLayoutAddress);
        textInputLayoutPhone = (TextInputLayout) findViewById(R.id.textInputLayoutPhone);
        textInputLayoutTypeOfBiz = (TextInputLayout)findViewById(R.id.textInputLayoutTypeOfBiz);
        spinnerMarketLocation = (Spinner)findViewById(R.id.spinnerMarketLocation);
        spinnerMarketArea = (Spinner)findViewById(R.id.spinnerMarketArea);
        textInputEditTextBizType = (TextInputEditText)findViewById(R.id.textInputEditTextTypeOfBiz);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextPhone = (TextInputEditText) findViewById(R.id.textInputEditTextPhone);
        textInputEditTextAddress = (TextInputEditText) findViewById(R.id.textInputEditTextAddress);

        appCompatButtonNext = (AppCompatButton) findViewById(R.id.appCompatButtonNext);

    }

    private void initListeners(){
        spinnerMarketLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedValue = adapterView.getSelectedItem().toString();

                if (selectedValue.equalsIgnoreCase("Uyo")) {

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                            R.array.area_uyo, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerMarketArea.setEnabled(true);
                    spinnerMarketArea.setAdapter(adapter);

                    //Store the selected values
                    userSelectedMarketLocation = "Uyo";
                    //userSelectedMarketArea = (String) spinnerMarketArea.getSelectedItem();

                } else if (selectedValue.equalsIgnoreCase("eket")){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                            R.array.area_eket, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarketArea.setEnabled(true);
                    // Apply the adapter to the spinner
                    spinnerMarketArea.setAdapter(adapter);

                    //Store the selected values
                    userSelectedMarketLocation = "Eket";
                    //userSelectedMarketArea = spinnerMarketArea.getSelectedItem().toString();

                } else if (selectedValue.equalsIgnoreCase("oron")){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                            R.array.area_oron, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarketArea.setEnabled(true);
                    // Apply the adapter to the spinner
                    spinnerMarketArea.setAdapter(adapter);

                    //Store the selected values
                    userSelectedMarketLocation = "Oron";
                    //userSelectedMarketArea = spinnerMarketArea.getSelectedItem().toString();

                } else if (selectedValue.equalsIgnoreCase("ikot ekpene")){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                            R.array.area_ikotekpene, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarketArea.setEnabled(true);
                    // Apply the adapter to the spinner
                    spinnerMarketArea.setAdapter(adapter);

                    //Store the selected values
                    userSelectedMarketLocation = "Ikot Ekpenne";


                } else {
                    spinnerMarketArea.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        appCompatButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushDataToNextActivity();
            }
        });
    }

    //This method is to initialize objects to be used
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        myUtils = new Utils(activity);
    }

    private void pushDataToNextActivity(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPhone, textInputLayoutPhone, getString(R.string.error_phone))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextAddress, textInputLayoutAddress, getString(R.string.error_address))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextBizType, textInputLayoutTypeOfBiz, getString(R.string.error_business_type))) {
            return;
        }

        // get selected radio button from radioGroup
        int selectedId = radioGroupSex.getCheckedRadioButtonId();
        // userSelectedMarketArea = spinnerMarketArea.getSelectedItem().toString();

        // find the radiobutton by returned id  spinnerMarketArea.getSelectedItem().toString()
        radioSexButton = (RadioButton) findViewById(selectedId);

        String intentFromAgent =  getIntent().getStringExtra("AgentEmail");

        Intent beneficiaryIntents = new Intent(activity, CaptureBeneficiary.class);
        beneficiaryIntents.putExtra("Name", textInputEditTextName.getText().toString().trim());
        beneficiaryIntents.putExtra("Sex", radioSexButton.getText().toString().trim());
        beneficiaryIntents.putExtra("Address", textInputEditTextAddress.getText().toString().trim());
        beneficiaryIntents.putExtra("Phone", textInputEditTextPhone.getText().toString().trim());
        beneficiaryIntents.putExtra("Type", textInputEditTextBizType.getText().toString().trim());
        beneficiaryIntents.putExtra("Location", spinnerMarketLocation.getSelectedItem().toString());
        beneficiaryIntents.putExtra("Area", spinnerMarketArea.getSelectedItem().toString());
        beneficiaryIntents.putExtra("Email", intentFromAgent);

        startActivity(beneficiaryIntents);
        myUtils.emptyInputEditText(textInputEditTextName, textInputEditTextAddress, textInputEditTextPhone, textInputEditTextBizType);
        spinnerMarketLocation.setSelection(0);
        spinnerMarketArea.setSelection(0);
    }

}
