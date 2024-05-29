public class ContactsFragment extends Fragment {

    private ListView contactsListView;
    private ContactsAdapter adapter;
    private ArrayList<String> contactsList;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // Obtient le ViewModel partagé
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // Vérifie les permissions et charge les contacts
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            contactsListView = view.findViewById(R.id.contacts_list_view);
            contactsList = getContacts(); // Charge les contacts depuis le ContentResolver
            adapter = new ContactsAdapter(getActivity(), contactsList, this);
            contactsListView.setAdapter(adapter);
            contactsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            contactsListView.setOnItemClickListener((parent, view1, position, id) -> {
                updateSelectedContacts(); // Met à jour les contacts sélectionnés
            });

            // Met à jour l'état de sélection des contacts selon le ViewModel partagé
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
                    contacts.add(phoneNumber); // Ajoute les numéros de téléphone à la liste
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
                selectedContacts.add(contacts.get(i)); // Met à jour la liste des contacts sélectionnés
            }
        }
        sharedViewModel.setSelectedContactNumbersTab1(selectedContacts); // Met à jour le ViewModel partagé
    }

    public boolean isItemChecked(int position) {
        return contactsListView.isItemChecked(position);
    }

    public void setItemChecked(int position, boolean isChecked) {
        contactsListView.setItemChecked(position, isChecked);
    }
}
