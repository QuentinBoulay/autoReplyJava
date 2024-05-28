package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class SendSpamFragment extends Fragment {

    private Spinner spinnerContacts;
    private Button buttonSendSpam, buttonActivateAutoReply;
    private SharedViewModel sharedViewModel;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sendspam, container, false);

        // Configuration des CheckBox pour la réponse automatique et le spam
        spinnerContacts = view.findViewById(R.id.spinner_contacts);
        buttonSendSpam = view.findViewById(R.id.button_send_spam);
        buttonActivateAutoReply = view.findViewById(R.id.button_activate_auto_reply);

        // Configuration du spinner avec les contacts sélectionnés
        List<String> contacts = sharedViewModel.getSelectedContactNumbersTab1(); // Utilise les contacts sélectionnés
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, contacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContacts.setAdapter(adapter);

        // Configuration des boutons pour envoyer le spam et activer la réponse automatique
        buttonSendSpam.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
            } else {
                sendSpam();
            }
        });

        buttonActivateAutoReply.setOnClickListener(v -> {
            String selectedContact = (String) spinnerContacts.getSelectedItem();
            if (selectedContact != null) {
                sharedViewModel.setSelectedContactNumberTab3(selectedContact);
                activateAutoReply(selectedContact, sharedViewModel.getSelectedAutoReplyMessage());
            } else {
                Toast.makeText(getActivity(), "No contact selected.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    // Méthode pour envoyer le spam
    private void sendSpam() {
        String selectedContact = (String) spinnerContacts.getSelectedItem();
        String message = sharedViewModel.getSelectedSpamMessage(); // Récupère le message de spam sélectionné

        if (selectedContact != null && message != null && !message.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(selectedContact, null, message, null, null);
            Toast.makeText(getActivity(), "Spam envoyé à " + selectedContact, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Pas de contact ou message sélectionné", Toast.LENGTH_SHORT).show();
        }
    }
    // Méthode pour activer la réponse automatique
    private void activateAutoReply(String selectedContact, String autoReplyMessage) {
        SharedPreferences prefs = getActivity().getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("auto_reply_enabled", true);
        editor.putString("selected_contact_number", selectedContact);
        editor.putString("selected_auto_reply_message", autoReplyMessage);
        editor.apply();

        Toast.makeText(getActivity(), "Pas de réponse automatique sélectionné " + selectedContact, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSpam();
            } else {
                Toast.makeText(getActivity(), "Permission SMS refusé", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
