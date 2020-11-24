package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectedCharacterIdViewModel extends ViewModel {

    private MutableLiveData<String> selected_id;

    public MutableLiveData<String> getSelected_id() {
        if (selected_id == null) {
            selected_id = new MutableLiveData<>();
        }
        return selected_id;
    }
}
