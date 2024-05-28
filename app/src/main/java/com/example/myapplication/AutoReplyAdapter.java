package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class AutoReplyAdapter extends ArrayAdapter<String> {
    private final SharedViewModel sharedViewModel;
    private int selectedAutoReplyPosition = -1;
    private int selectedSpamPosition = -1;

    public AutoReplyAdapter(Context context, List<String> autoReplies, SharedViewModel sharedViewModel) {
        super(context, 0, autoReplies);
        this.sharedViewModel = sharedViewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate la vue si elle n'est pas déjà existante
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_reply_item, parent, false);
        }

        // Inflate la vue si elle n'est pas déjà existante
        TextView autoReplyText = convertView.findViewById(R.id.auto_reply_text);
        CheckBox autoReplyCheckBox = convertView.findViewById(R.id.autoreply_checkbox);
        CheckBox spamCheckBox = convertView.findViewById(R.id.spam_checkbox);

        // Récupération du message de réponse automatique
        String autoReply = getItem(position);
        autoReplyText.setText(autoReply);

        // Configuration des checkBox pour la réponse automatique et le spam
        autoReplyCheckBox.setOnCheckedChangeListener(null);
        autoReplyCheckBox.setChecked(position == selectedAutoReplyPosition);
        autoReplyCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (selectedAutoReplyPosition != -1 && selectedAutoReplyPosition != position) {
                    notifyDataSetChanged();
                }
                selectedAutoReplyPosition = position;
                sharedViewModel.setSelectedAutoReplyMessage(autoReply);
            } else if (selectedAutoReplyPosition == position) {
                selectedAutoReplyPosition = -1;
                sharedViewModel.setSelectedAutoReplyMessage(null);
            }
        });

        spamCheckBox.setOnCheckedChangeListener(null);
        spamCheckBox.setChecked(position == selectedSpamPosition);
        spamCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (selectedSpamPosition != -1 && selectedSpamPosition != position) {
                    notifyDataSetChanged();
                }
                selectedSpamPosition = position;
                sharedViewModel.setSelectedSpamMessage(autoReply);
            } else if (selectedSpamPosition == position) {
                selectedSpamPosition = -1;
                sharedViewModel.setSelectedSpamMessage(null);
            }
        });

        return convertView;
    }
}