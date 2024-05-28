package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> contacts;
    private ContactsFragment contactsFragment;

    public ContactsAdapter(Context context, List<String> contacts, ContactsFragment contactsFragment) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
        this.contactsFragment = contactsFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Inflate la vue si elle n'est pas déjà existante
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
            holder = new ViewHolder();
            holder.contactName = convertView.findViewById(R.id.contact_name);
            holder.checkBox = convertView.findViewById(R.id.contact_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String contact = getItem(position);
        holder.contactName.setText(contact);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(contactsFragment.isItemChecked(position));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contactsFragment.setItemChecked(position, isChecked);
            contactsFragment.updateSelectedContacts();
        });

        return convertView;
    }

    static class ViewHolder {
        TextView contactName;
        CheckBox checkBox;
    }
}