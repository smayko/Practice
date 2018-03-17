package com.phonebook.phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.phonebook.phonebook.db.DatabaseHelper;
import com.phonebook.phonebook.model.Contact;

import java.sql.SQLException;

/**
 * Created by nikola kosmajac on 13-Mar-18.
 */

public class EditContactActivity extends AppCompatActivity {

    EditText editName;
    EditText editLast;
    EditText editadress;

    Button btnEdit;
    DatabaseHelper mDatabasehelper;

    int contactId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contact_activity);

        Intent i = getIntent();
        contactId = i.getIntExtra("id", 0);

        editLast = findViewById(R.id.etEditActorName);
        editName = findViewById(R.id.etEditActorLastName);
        editadress = findViewById(R.id.etEditAdress);

        mDatabasehelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        editActor();
    }

    public void editActor() {
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lastName = editLast.getText().toString();
                String name = editName.getText().toString();
                String adress = editadress.getText().toString();

                UpdateBuilder<Contact, Integer> updateBuilder = null;
                try {
                    updateBuilder = mDatabasehelper.getContactDao().updateBuilder();
                    updateBuilder.where().eq("id", contactId);
                    updateBuilder.updateColumnValue("name", name);
                    updateBuilder.updateColumnValue("last_name", lastName);
                    updateBuilder.updateColumnValue("adress", adress);
                    updateBuilder.update();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(EditContactActivity.this, "Contact edited", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
