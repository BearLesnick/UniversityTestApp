package com.pet.lesnick.letterappwithfragments.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.pet.lesnick.letterappwithfragments.R;



public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private EditText urlLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_layout);

        //get views
        webView=(WebView)findViewById(R.id.web_view);
        urlLine = findViewById(R.id.address_line);
        urlLine.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
                return false;
            }
        });

        urlLine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(urlLine.getWindowToken(), 0);
                    webView.loadUrl(urlLine.getText().toString());// your action here
                }
            }
        });

        //prevent browser opening
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //initialize iterator,
        // webView url and button text - next searcher
        webView.loadUrl("https://google.com");
        urlLine.setText("https://google.com");
    }

    //
    public void changeURL(View view) {
        webView.loadUrl("https://"+urlLine.getText().toString());
    }}
