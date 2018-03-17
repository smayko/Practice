package com.phonebook.phonebook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.phonebook.phonebook.db.DatabaseHelper;
import com.phonebook.phonebook.model.Contact;
import com.phonebook.phonebook.model.PhoneNumber;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikola kosmajac on 12-Mar-18.
 */

public class ContactDetailsActivity extends AppCompatActivity {


    private TextView name;
    private TextView lastname;
    private TextView adress;
    private ImageView image;

    private ListView listViewNum;

    private List<PhoneNumber> phones;
    private ListAdapter adapter;

    private String imagePath;

    int contactID;
    DatabaseHelper mDatabaseHelper;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        name = findViewById(R.id.tvContactName);
        lastname = findViewById(R.id.tvContactLastname);
        adress = findViewById(R.id.tvContactAddress);
        image = findViewById(R.id.ivContactImage);
        listViewNum = findViewById(R.id.listViewNumbers);

        Intent i = getIntent();
        contactID = i.getIntExtra("id", 0);

        mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        verifyStoragePermissions(ContactDetailsActivity.this);
        getContactInfo();
        getPhoneNums();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("nas glupi tag", "Permission is granted");
                return true;
            } else {

                Log.v("nas glupi tag", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("nas glupi tag", "Permission is granted");
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                //add phone number

                Intent intent = new Intent(ContactDetailsActivity.this, AddPhoneNumberActivity.class);
                intent.putExtra("id", contactID);
                startActivity(intent);
                break;

            case R.id.action_edit:

                Intent i = new Intent(ContactDetailsActivity.this, EditContactActivity.class);
                i.putExtra("id", contactID);
                startActivity(i);

                break;

            case R.id.action_delete:

                try {
                    DeleteBuilder<PhoneNumber, Integer> deleteBuilder = mDatabaseHelper.getPhoneDao().deleteBuilder();
                    deleteBuilder.where().eq("contact_id", contactID);
                    deleteBuilder.delete();
                    mDatabaseHelper.getContactDao().deleteById(contactID);
                    Toast.makeText(ContactDetailsActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getContactInfo() {
        Contact contact = new Contact();
        String storage = "storage";
        File imageFile = null;

        try {
            contact = mDatabaseHelper.getContactDao().queryForId(contactID);

            name.setText(contact.getContactName());
            lastname.setText(contact.getContactLastName());
            adress.setText(contact.getContactAdress());

            imagePath = (contact.getContactImage());

            //Check if image comes from Camera or from Gallery
            if (imagePath != null){
                if (imagePath.contains(storage)) {
                    imageFile = new File(imagePath);
                    imageFile.getAbsolutePath();
                    Picasso.with(ContactDetailsActivity.this).load(imageFile).fit().centerCrop().into(image);
                    //image.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                } else {
                    Picasso.with(ContactDetailsActivity.this).load(imagePath).fit().centerCrop().into(image);
                }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPhoneNums() {
        PhoneNumber phoneNumber = new PhoneNumber();
        try {

            //get phone numbers for selected contact
            phones = mDatabaseHelper.getPhoneDao().queryBuilder().where().eq("contact_id", contactID).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String> names = new ArrayList();
        for (PhoneNumber a : phones) {
            names.add(a.getPhoneType().concat(": ").concat(a.getPhoneNumber()));
        }


        listViewNum = findViewById(R.id.listViewNumbers);
        adapter = new ArrayAdapter<String>(this, R.layout.single_contact, names);
        listViewNum.setAdapter(adapter);
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("nas glupi tag", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            getContactInfo();
        }
    }

}
