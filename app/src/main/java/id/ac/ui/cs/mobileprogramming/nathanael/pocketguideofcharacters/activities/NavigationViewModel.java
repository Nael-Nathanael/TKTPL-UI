package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel {

    private MutableLiveData<Boolean> active;

    public MutableLiveData<Boolean> getActive() {
        if (active == null) {
            active = new MutableLiveData<>();
            active.setValue(true);
        }
        return active;
    }
}
