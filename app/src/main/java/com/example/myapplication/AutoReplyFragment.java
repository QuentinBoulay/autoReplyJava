package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AutoReplyFragment extends Fragment {

    private static final String PREFS_NAME = "AutoReplyPrefs";
    private static final String REPLIES_KEY = "AutoReplies";
    private ListView autoReplyListView;
    private AutoReplyAdapter adapter;
    private ArrayList<String> autoRepliesList;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        autoRepliesList = loadAutoReplies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autoreply, container, false);
        autoReplyListView = view.findViewById(R.id.auto_reply_list_view);
        EditText editTextNewReply = view.findViewById(R.id.edit_text_new_reply);

        // Configuration du bouton pour ajouter une nouvelle réponse
        Button buttonAddReply = view.findViewById(R.id.button_add_reply);
        adapter = new AutoReplyAdapter(getActivity(), autoRepliesList, sharedViewModel);
        autoReplyListView.setAdapter(adapter);

        // Configuration du bouton pour ajouter une nouvelle réponse
        buttonAddReply.setOnClickListener(v -> {
            String newReply = editTextNewReply.getText().toString();
            if (!newReply.isEmpty()) {
                autoRepliesList.add(newReply);
                adapter.notifyDataSetChanged();
                saveAutoReplies();
                editTextNewReply.setText("");
                sharedViewModel.setSelectedSpamMessage(newReply);
            }
        });

        String selectedAutoReplyMessage = sharedViewModel.getSelectedAutoReplyMessage();
        if (selectedAutoReplyMessage != null) {
            editTextNewReply.setText(selectedAutoReplyMessage);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveAutoReplies();
    }

    // Méthode pour charger les réponses automatiques
    private ArrayList<String> loadAutoReplies() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(REPLIES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // Méthode pour sauvegarder les réponses automatiques
    private void saveAutoReplies() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(autoRepliesList);
        editor.putString(REPLIES_KEY, json);
        editor.apply();
    }
}
