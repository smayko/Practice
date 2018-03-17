package com.phonebook.phonebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.phonebook.phonebook.model.Contact;
import com.phonebook.phonebook.model.PhoneNumber;

import java.sql.SQLException;

/**
 * Created by nikola kosmajac on 12-Mar-18.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "my_phonebook.db";

    private static final int DATABASE_VERSION = 5;

    private Dao<Contact, Integer> contactDao = null;
    private Dao<PhoneNumber, Integer> phoneNumberDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, PhoneNumber.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectionSource connectionSource = null;
    @Override
    public ConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            connectionSource = super.getConnectionSource();
        }
        return connectionSource;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, PhoneNumber.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    //create Dao Object
    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if (contactDao == null) {
            contactDao = getDao(Contact.class);
        }
        return contactDao;
    }

    public Dao<PhoneNumber, Integer> getPhoneDao() throws SQLException {
        if (phoneNumberDao == null) {
            phoneNumberDao = getDao(PhoneNumber.class);
        }
        return phoneNumberDao;
    }

    //close resources when done with db!
    @Override
    public void close() {

        contactDao = null;
        super.close();
    }
}
