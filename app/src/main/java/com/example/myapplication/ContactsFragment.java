package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private ListView contactsListView;
    private ContactsAdapter adapter;
    private ArrayList<String> contactsList;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // Vérifiez les permissions et chargez le contenu initial.
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            contactsListView = view.findViewById(R.id.contacts_list_view);
            contactsList = getContacts();
            adapter = new ContactsAdapter(getActivity(), contactsList, this);
            contactsListView.setAdapter(adapter);
            contactsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            // Configuration de l'écouteur de clics pour mettre à jour les contacts sélectionnés
            contactsListView.setOnItemClickListener((parent, view1, position, id) -> {
                updateSelectedContacts();
            });

            List<String> selectedContacts = sharedViewModel.getSelectedContactNumbersTab1();
            for (int i = 0; i < contactsList.size(); i++) {
                if (selectedContacts.contains(contactsList.get(i))) {
                    contactsListView.setItemChecked(i, true);
                }
            }
        } else {
            Log.d("ContactsFragment", "Permission not granted to read contacts");
        }

        return view;
    }

    private ArrayList<String> getContacts() {
        ArrayList<String> contacts = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(phoneNumber);
                } catch (IllegalArgumentException e) {
                    Log.e("ContactsFragment", "Error reading contact", e);
                }
            }
            cursor.close();
        }
        return contacts;
    }

    public void updateSelectedContacts() {
        ArrayList<String> selectedContacts = new ArrayList<>();
        for (int i = 0; i < contactsListView.getCount(); i++) {
            if (contactsListView.isItemChecked(i)) {
                selectedContacts.add(contactsList.get(i));
            }
        }
        sharedViewModel.setSelectedContactNumbersTab1(selectedContacts);
    }

    public boolean isItemChecked(int position) {
        return contactsListView.isItemChecked(position);
    }

    public void setItemChecked(int position, boolean isChecked) {
        contactsListView.setItemChecked(position, isChecked);
    }
}
