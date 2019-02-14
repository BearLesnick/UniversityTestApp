package com.pet.lesnick.letterappwithfragments.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pet.lesnick.letterappwithfragments.R;
import com.pet.lesnick.letterappwithfragments.adapter.DraftAdapter;
import com.pet.lesnick.letterappwithfragments.datahelper.DraftsHelper;
import com.pet.lesnick.letterappwithfragments.model.Draft;

import java.util.ArrayList;
import java.util.List;

public class DraftActivity extends Activity {
    private DraftsHelper DBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drafts_activity_layout);
        DBHelper = new DraftsHelper(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.draftsRecyclerView);
        List<Draft> drafts = getDrafts();
        DraftAdapter adapter = new DraftAdapter(drafts, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void deleteDraft(Draft draft) {
        DBHelper.deleteData(draft);
    }

    public List<Draft> getDrafts() {
        Cursor data = DBHelper.getData();
        ArrayList<Draft> drafts = new ArrayList<>();
        while (data.moveToNext()) {
            Draft draft = new Draft();
            draft.setContent(data.getString(2));
            draft.setHeader(data.getString(1));
            draft.setId(data.getInt(0));
            drafts.add(draft);
        }
        if (drafts.size() == 0) {
            Draft draft = new Draft();
            draft.setContent("Test Content");
            draft.setHeader("Test Header");
            drafts.add(draft);
        }
        return drafts;
    }

    public void sendResponse(Draft draft) {
        Intent intent = new Intent();
        intent.putExtra("header", draft.getHeader());
        intent.putExtra("content", draft.getContent());
        setResult(RESULT_OK, intent);
        finish();
    }
}
