package com.pet.lesnick.letterappwithfragments.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pet.lesnick.letterappwithfragments.activities.DraftActivity;
import com.pet.lesnick.letterappwithfragments.R;
import com.pet.lesnick.letterappwithfragments.model.Draft;

import java.util.List;

public class DraftAdapter extends RecyclerView.Adapter {
    private List<Draft> items;
    private DraftActivity context;
    private void deleteDraft(Draft draft, int index)
    {
        items.remove(draft);
        context.deleteDraft(draft);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, items.size());
        if (items.size() == 0)
            context.sendResponse(new Draft());
    }

    public DraftAdapter(List<Draft> drafts, DraftActivity context) {
        this.items = drafts;
        this.context = context;
    }

    static class ContactsHolder extends RecyclerView.ViewHolder {
        DraftAdapter adapter;
        DraftActivity context;
        TextView headerTextView;
        TextView contentTextView;
        int itemId;
        ImageButton deleteButton;

        void setContext (@NonNull DraftActivity context, DraftAdapter adapter) {
            this.adapter = adapter;
            this.context = context;
        }

        ContactsHolder(@NonNull View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.draftHeader);
            contentTextView = itemView.findViewById(R.id.draftContent);
            deleteButton = itemView.findViewById(R.id.delete_draft_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Draft draft = new Draft();
                    draft.setHeader(headerTextView.getText().toString());
                    draft.setContent(contentTextView.getText().toString());
                    draft.setId(itemId);
                    context.deleteDraft(draft);
                    adapter.deleteDraft(draft,getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Draft draft = new Draft();
                    draft.setHeader(headerTextView.getText().toString());
                    draft.setContent(contentTextView.getText().toString());
                    draft.setId(itemId);
                    context.sendResponse(draft);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drafts_list_item_layout, viewGroup, false);
        ContactsHolder holder = new ContactsHolder(v);
        holder.setContext(context, this);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) { //This method describes what do we need to do to appear list element and how should we fill it with data
        ContactsHolder holder = (ContactsHolder) viewHolder;
        Draft pickedItem = items.get(i);
        holder.headerTextView.setText(pickedItem.getHeader());
        holder.contentTextView.setText(pickedItem.getContent());
        holder.itemId = pickedItem.getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
