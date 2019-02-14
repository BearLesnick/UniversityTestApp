package com.pet.lesnick.letterappwithfragments.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pet.lesnick.letterappwithfragments.R;
import com.pet.lesnick.letterappwithfragments.adapter.RecyclerAdapter;
import com.pet.lesnick.letterappwithfragments.datahelper.DraftsHelper;
import com.pet.lesnick.letterappwithfragments.fragments.ContactFragment;
import com.pet.lesnick.letterappwithfragments.fragments.LetterContentFragment;
import com.pet.lesnick.letterappwithfragments.fragments.RecyclerConctactFragment;
import com.pet.lesnick.letterappwithfragments.model.Draft;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int DRAFTS_INTENT_CODE = 1;
    private String currentEmail;
    private String currentContent;
    private String currentHeader;
    private String CurrentFragmentTag = LetterContentFragment.TAG;
    TextView contentTextView;
    TextView headerTextView;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment letterFragment;
    private Fragment contactsFragment;
    private DraftsHelper DBHelper;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        manager = getSupportFragmentManager();
        letterFragment = new LetterContentFragment();
        contactsFragment = new RecyclerConctactFragment();


        onStartScreenShow();
        DBHelper = new DraftsHelper(getApplicationContext());

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.baseline_menu_black_24);
        Log.e("actionbar", "" + actionbar.getHeight());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStartScreenShow() {
        transaction = manager.beginTransaction();
        if (CurrentFragmentTag.equals(LetterContentFragment.TAG)) {

            if (manager.findFragmentByTag(LetterContentFragment.TAG) == null) {
                transaction.add(R.id.fragmentLayout, letterFragment, LetterContentFragment.TAG);
            }
            if (manager.findFragmentByTag(RecyclerConctactFragment.TAG) != null) {
                transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(RecyclerConctactFragment.TAG)));
            }

        } else {
            if (manager.findFragmentByTag(RecyclerConctactFragment.TAG) == null) {
                transaction.add(contactsFragment, RecyclerConctactFragment.TAG);
            }
            if (manager.findFragmentByTag(LetterContentFragment.TAG) != null) {
                transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(LetterContentFragment.TAG)));
            }
        }
        transaction.commit();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            TextView emailTextView = findViewById(R.id.emailAddressView);
            currentEmail = emailTextView.getText().toString();
            TextView headerTextView = findViewById(R.id.letterHeaderView);
            currentHeader = headerTextView.getText().toString();
            TextView contentTextView = findViewById(R.id.letterContentView);
            currentContent = contentTextView.getText().toString();
            outState.putString("LetterFragmentEmail", currentEmail);
            outState.putString("LetterFragmentHeader", currentHeader);
            outState.putString("LetterFragmentContent", currentContent);
            outState.putString("CurrentFragmentTag", CurrentFragmentTag);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.letter_options_menu, menu);
        return true;
    }

    public void onAddToDraft(MenuItem item) {
        Draft draft = new Draft();
        TextView content = findViewById(R.id.letterContentView);
        TextView header = findViewById(R.id.letterHeaderView);
        draft.setHeader(header.getText().toString());
        draft.setContent(content.getText().toString());
        if (!header.getText().toString().equals("") || !content.getText().toString().equals("")) { 
            DBHelper.addData(draft);
            Toast.makeText(this, "Draft saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Draft is empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRandomContent(MenuItem item)
    {
        int permissionStatus = ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.INTERNET);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onOpenDraftsList(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), DraftActivity.class);
        startActivityForResult(intent, DRAFTS_INTENT_CODE);
    }
    public void startSensorActivity(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), SensorTestActivity.class);
        startActivityForResult(intent, DRAFTS_INTENT_CODE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        headerTextView.setText(currentHeader);
        contentTextView.setText(currentContent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        headerTextView = findViewById(R.id.letterHeaderView);
        contentTextView = findViewById(R.id.letterContentView);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView emailTextView = findViewById(R.id.emailAddressView);
        emailTextView.setText(currentEmail);
        TextView headerTextView = findViewById(R.id.letterHeaderView);
        headerTextView.setText(currentHeader);
        TextView contentTextView = findViewById(R.id.letterContentView);
        contentTextView.setText(currentContent);
    }

    public void onContactsShow(View view) {
        CurrentFragmentTag = ContactFragment.TAG;
        transaction = manager.beginTransaction();
        transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(LetterContentFragment.TAG)));
        transaction.add(R.id.fragmentLayout, contactsFragment, RecyclerConctactFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commit();
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void startWebActivity(MenuItem item){
        Intent intent = new Intent(this,WebActivity.class);
        startActivity(intent);
    }
    public void startHtmlActivity(MenuItem item){
        Intent intent = new Intent(this,HtmlActivity.class);
        startActivity(intent);
    }
    public void startNavigationActivity(MenuItem item){
        Intent intent = new Intent(this,StyledMapDemoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DRAFTS_INTENT_CODE: {
                    assert data != null;
                       String Header = data.getStringExtra("header");
                       String Content = data.getStringExtra("content");
                       if(Header != null||Content!= null)
                       {
                           currentHeader = Header;
                           currentContent = Content;
                       }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onContactChoice(String contact) {
        CurrentFragmentTag = LetterContentFragment.TAG;
        currentEmail = contact;
        transaction = manager.beginTransaction();
        transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(RecyclerConctactFragment.TAG)));
        Bundle bundle = new Bundle();
        bundle.putString("LetterFragmentEmail", currentEmail);
        transaction.add(letterFragment, LetterContentFragment.TAG);
        letterFragment.setArguments(bundle);
        transaction.commit();
        Objects.requireNonNull(getSupportActionBar()).show();
    }

    public void onLetterSend(View view) {
        EditText emailAddressView = findViewById(R.id.emailAddressView);
        EditText letterHeaderView = findViewById(R.id.letterHeaderView);
        EditText letterContentView = findViewById(R.id.letterContentView);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddressView.getText().toString()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, letterHeaderView.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, letterContentView.getText().toString());

        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }

    public void onMenuContactsItemClick(MenuItem item) {
        if (CurrentFragmentTag.equals(LetterContentFragment.TAG)) {
            onContactsShow(null);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (CurrentFragmentTag.equals(ContactFragment.TAG)) {
            Objects.requireNonNull(getSupportActionBar()).show();
            CurrentFragmentTag = LetterContentFragment.TAG;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
