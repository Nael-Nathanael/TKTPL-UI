package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.level_two;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new CreateCharacterFragment();
        }
        return new CardListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
