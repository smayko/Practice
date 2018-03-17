package com.phonebook.phonebook.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by nikola kosmajac on 12-Mar-18.
 */

@DatabaseTable(tableName = Contact.TABLE_NAME)
public class Contact {

    public static final String TABLE_NAME = "Contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONTACT_NAME = "name";
    public static final String COLUMN_CONTACT_LAST_NAME = "last_name";
    public static final String COLUMN_CONTACT_IMAGE = "image";
    public static final String COLUMN_CONTACT_ADRESS = "adress";
    public static final String COLUMN_CONTACT_PHONE = "phone";

    @DatabaseField (columnName = COLUMN_ID, generatedId = true)
    private int id;
    @DatabaseField( columnName =COLUMN_CONTACT_NAME)
    private String contactName;
    @DatabaseField( columnName =COLUMN_CONTACT_LAST_NAME)
    private String contactLastName;
    @DatabaseField( columnName =COLUMN_CONTACT_IMAGE)
    private String contactImage;
    @DatabaseField( columnName =COLUMN_CONTACT_ADRESS)
    private String contactAdress;
    @ForeignCollectionField
    private Collection<PhoneNumber> phoneNumberType;


    public Contact (){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactAdress() {
        return contactAdress;
    }

    public void setContactAdress(String contactAdress) {
        this.contactAdress = contactAdress;
    }

    public Collection<PhoneNumber> getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(Collection<PhoneNumber> phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }
}
