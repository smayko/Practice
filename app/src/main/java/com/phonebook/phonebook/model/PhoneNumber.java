package com.phonebook.phonebook.model;

import android.provider.ContactsContract;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by nikola kosmajac on 12-Mar-18.
 */

@DatabaseTable (tableName = PhoneNumber.TABLE_NAME)
public class PhoneNumber {

    public static final String TABLE_NAME = "phone_numbers";
    public static final String COLUMN_PHONE_NUMBER = "user_phone_number";
    public static final String COLUMN_PHONE_TYPE = "phone_type";


    @DatabaseField(columnName = COLUMN_PHONE_NUMBER)
    private String phoneNumber;

    @DatabaseField(columnName =COLUMN_PHONE_TYPE )
    private String phoneType;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "contact_id")
    private Contact contact;

    public PhoneNumber(){

    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
