package com.phonebook.phonebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.phonebook.phonebook.db.DatabaseHelper;
import com.phonebook.phonebook.model.Contact;
import com.phonebook.phonebook.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewContacts;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper mDatabaseHelper;
    private List<Contact> contacts;

    private DrawerLayout mDrawer;

    public static final String YELLOW_PAGES = "https://www.yellowpages.rs/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawer = findViewById(R.id.navigation_drawer);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        switch (menuItem.getItemId()) {
                            case R.id.nav_search:
                                if(Utils.isShowToast(MainActivity.this))
                                Toast.makeText(MainActivity.this, "Performing Search", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.nav_add:
                                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                                startActivity(intent);
                                return true;

                            case R.id.nav_yellow_pages:
                                String url = YELLOW_PAGES;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                return true;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                //open add Contact
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, MyPreferences.class);
                startActivity(i);
                break;

            case R.id.action_about :
                //display About Dialog
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("About");
                dialog.setMessage(getResources().getString(R.string.about_app));
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContact();
    }

    private void getContact() {
        try {
            contacts = mDatabaseHelper.getContactDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> names = new ArrayList();
        if (contacts != null) {
            for (Contact c : contacts) {
                names.add(c.getContactName() + " " + c.getContactLastName());
            }
            listViewContacts = findViewById(R.id.listViewContacts);
            adapter = new ArrayAdapter<String>(this, R.layout.single_contact, names);
            listViewContacts.setAdapter(adapter);
            listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int id = contacts.get(i).getId();
                    Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }
    }
}
