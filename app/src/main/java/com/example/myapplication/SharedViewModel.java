public class SharedViewModel extends AndroidViewModel {
    private List<String> selectedContactNumbersTab1;
    private String selectedContactNumberTab3;
    private String selectedAutoReplyMessage;
    private String selectedSpamMessage;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        this.selectedContactNumbersTab1 = new ArrayList<>(); // Initialisation de la liste des contacts sélectionnés
    }

    // Getters et setters pour les différentes propriétés partagées
    public List<String> getSelectedContactNumbersTab1() {
        return selectedContactNumbersTab1;
    }

    public void setSelectedContactNumbersTab1(List<String> selectedContactNumbers) {
        this.selectedContactNumbersTab1 = selectedContactNumbers;
    }

    public String getSelectedContactNumberTab3() {
        return selectedContactNumberTab3;
    }

    public void setSelectedContactNumberTab3(String selectedContactNumber) {
        this.selectedContactNumberTab3 = selectedContactNumber;
    }

    public String getSelectedAutoReplyMessage() {
        return selectedAutoReplyMessage;
    }

    public void setSelectedAutoReplyMessage(String selectedAutoReplyMessage) {
        this.selectedAutoReplyMessage = selectedAutoReplyMessage;
    }

    public String getSelectedSpamMessage() {
        return selectedSpamMessage;
    }

    public void setSelectedSpamMessage(String selectedSpamMessage) {
        this.selectedSpamMessage = selectedSpamMessage;
    }
}
