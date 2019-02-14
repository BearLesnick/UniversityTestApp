package com.pet.lesnick.letterappwithfragments.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pet.lesnick.letterappwithfragments.activities.MainActivity;
import com.pet.lesnick.letterappwithfragments.R;
import com.pet.lesnick.letterappwithfragments.adapter.UserCardsAdapter;
import com.pet.lesnick.letterappwithfragments.model.UserContactCard;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class ContactFragment extends ListFragment implements AdapterView.OnItemClickListener {//TODO DEPRECATED
    @SuppressLint("ValidFragment")
    ContactFragment() {
    }

    public static final String TAG = "ContactFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.contacts_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        int permissionStatus = ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()), Manifest.permission.READ_CONTACTS);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            UserCardsAdapter adapter = new UserCardsAdapter(Objects.requireNonNull(getContext()), initContacts());
            setListAdapter(adapter);
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    private List<UserContactCard> initContacts() {
        List<UserContactCard> cards = new LinkedList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

        assert contactsCursor != null;
        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                @SuppressLint("Recycle") Cursor dataCursor = getContext().getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID
                String name;
                String email = "";
                String otherEmail = "";
                String photo = "";
                assert dataCursor != null;
                if (dataCursor.moveToFirst()) {
                    do {
                        name = dataCursor.getString(
                                dataCursor.getColumnIndex(
                                        ContactsContract.Contacts.DISPLAY_NAME));
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    email = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK: {
                                    otherEmail = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                }

                                break;

                            }

                        }
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                           byte[] photoByte = dataCursor.getBlob(dataCursor
                                    .getColumnIndex("data15")); // get photo in
                            // byte

                            if (photoByte != null) {

                                // Now make a cache folder in file manager to
                                // make cache of contacts images and save them
                                // n .png
                                Bitmap bitmap = BitmapFactory.decodeByteArray(
                                        photoByte, 0, photoByte.length);
                                File cacheDirectory = getContext()
                                        .getCacheDir();
                                File tmp = new File(cacheDirectory.getPath()
                                        + "/_androhub" + contctId + ".png");
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(
                                            tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG,
                                            100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                    photo = tmp.getPath();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                }
                while (dataCursor.moveToNext()) ;
                    if (!email.equals("")) {

                        cards.add(new UserContactCard(email, name, photo));
                    } else {
                        if (!otherEmail.equals("")) {
                            cards.add(new UserContactCard(otherEmail,name , photo));
                        }
                    }
            }
            } while (contactsCursor.moveToNext());
        }
        contactsCursor.close();

        return cards;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MainActivity activity = (MainActivity) getActivity();
        UserContactCard chosen = (UserContactCard) l.getItemAtPosition(position);
        assert activity != null;
        activity.onContactChoice(chosen.getEmail());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("Item clicked");  }
}
