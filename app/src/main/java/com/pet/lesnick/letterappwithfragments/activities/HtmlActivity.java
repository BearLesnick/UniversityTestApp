package com.pet.lesnick.letterappwithfragments.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pet.lesnick.letterappwithfragments.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Iterator;

public class HtmlActivity extends Activity {

    EditText urlInput;
    TextView resultOutput;

    @Override
    protected void onStart() {
        super.onStart();
        urlInput = findViewById(R.id.html_search_url);
        resultOutput = findViewById(R.id.html_result_output);
        urlInput.setText("https://www.google.com/");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_parse_activity_layout);
    }

    public void startAnalise(View view) {
        HtmlTask task = new HtmlTask();
        task.execute();

    }

    @SuppressLint("StaticFieldLeak")
    class HtmlTask extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;//Здесь хранится будет разобранный html документ
            try {
                //Считываем заглавную страницу http://harrix.org
                doc = Jsoup.connect(urlInput.getText().toString()).get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            if (doc != null) {
                title = "" + doc.title() + "\n";
                Log.d("debugLog",title);
                Iterator<Element> iterator = doc.select("a[href]").iterator();

                for (int i = 0; i < 20; i++) {

                    if (iterator.hasNext()) {
                        String text = iterator.next().text();
                        Log.d("debugLog",text);
                        title = title + text + "\n";
                    }
                }
            } else
                title = "Ошибка";
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            resultOutput.setText(title);
            super.onPostExecute(result);

            //Тут выводим итоговые данные
        }
    }
}
