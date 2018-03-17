package com.phonebook.phonebook;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.phonebook.phonebook.db.DatabaseHelper;
import com.phonebook.phonebook.model.Contact;
import com.phonebook.phonebook.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nikola kosmajac on 12-Mar-18.
 */

public class AddContactActivity extends AppCompatActivity {

    private EditText etAddName;
    private EditText etAddLastName;
    private EditText etAddAddress;
    private Button btnAddPhoto;
    private Button btnAddContact;
    private Button btnAddPhotoFromGallery;

    private String name;
    private String lastName;
    private String adress;
    private String imageAdress;

    private ImageView imagePreview;

    private DatabaseHelper mDatabaseHelper;

    public static final int RESULT_GALLERY = 1;

    Contact contact;
    private Uri file;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);

        etAddName = findViewById(R.id.etAddName);
        etAddLastName = findViewById(R.id.etAddLastName);
        etAddAddress = findViewById(R.id.etAddAddress);
        btnAddPhoto = findViewById(R.id.btnAddImage);
        btnAddPhotoFromGallery = findViewById(R.id.btnAddImageFromGallery);
        btnAddContact = findViewById(R.id.btnAddContact);
        imagePreview = findViewById(R.id.ivImagePreview);

        mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

    }


    @Override
    protected void onResume() {
        super.onResume();

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contact = new Contact();
                contact.setContactAdress(etAddAddress.getText().toString());
                contact.setContactLastName(etAddLastName.getText().toString());
                contact.setContactName(etAddName.getText().toString());
                contact.setContactImage(imageAdress);

                try {
                    mDatabaseHelper.getContactDao().create(contact);

                    //display Toast message only if user approved it
                    if (Utils.isShowToast(AddContactActivity.this)) {
                        Toast.makeText(AddContactActivity.this, "Contact inserted", Toast.LENGTH_SHORT).show();
                    }
                    if (Utils.isShowNotifications(AddContactActivity.this)) {
                        Utils.messageNotification(AddContactActivity.this, "Contact inserted", 1, R.drawable.ic_action_add);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(AddContactActivity.this, "Something went terribly wrong...", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //todo REQUEST PERMISSION for camera
                ActivityCompat.requestPermissions(AddContactActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                if (ContextCompat.checkSelfPermission(AddContactActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePicture(view);
                    ActivityCompat.requestPermissions(AddContactActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        btnAddPhotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_GALLERY);*/

                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK).setType("image/jpg");
                startActivityForResult(pickPhotoIntent, 1);
            }
        });
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            file = FileProvider.getUriForFile(this, "com.phonebook.phonebook.fileprovider", createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
        imageAdress = file.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //load from gallery
            if (requestCode == 1) {

                //get image uri
                Uri imageUri = data.getData();

                String realPathFromURI = getRealPathFromURI(imageUri);
                imageAdress = realPathFromURI;

                imagePreview.setImageBitmap(BitmapFactory.decodeFile(realPathFromURI));

            }
            //load from camera
            if (requestCode == 100) {
                Picasso.with(AddContactActivity.this)
                        .load(file).fit().centerCrop()
                        .into(imagePreview);
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Creating image file for taking photos and storing to specific file dir
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PHONE_" + timeStamp + "_";
        File storageDir = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + ".");
        File image = null;

        if (storageDir.mkdirs() || storageDir.isDirectory()) {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            image.mkdirs();
        }
        return image;
    }
}
