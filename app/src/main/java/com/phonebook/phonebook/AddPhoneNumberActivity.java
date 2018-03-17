package com.phonebook.phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.phonebook.phonebook.db.DatabaseHelper;
import com.phonebook.phonebook.model.Contact;
import com.phonebook.phonebook.model.PhoneNumber;

/**
 * Created by nikola kosmajac on 13-Mar-18.
 */

public class AddPhoneNumberActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText etPhone;
    private String phoneType;
    private String phoneNum;
    private Button btnAdd;
    private DatabaseHelper databaseHelper;
    private int contactId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phone_number_activity);
        spinner = findViewById(R.id.spnChooseTypePhone);
        etPhone = findViewById(R.id.etEnterPhonenumber);
        btnAdd = findViewById(R.id.btnAddPhoneNumber);
        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        Intent intent = getIntent();
        contactId = intent.getIntExtra("id", 0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //btnAdd phone numbers to selected contact
                    Contact contact = databaseHelper.getContactDao().queryForId(contactId);
                    phoneNum = etPhone.getText().toString();
                    phoneType = spinner.getSelectedItem().toString();
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setPhoneNumber(phoneNum);
                    phoneNumber.setPhoneType(phoneType);
                    phoneNumber.setContact(contact);
                    databaseHelper.getPhoneDao().create(phoneNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(AddPhoneNumberActivity.this, "number inserted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
