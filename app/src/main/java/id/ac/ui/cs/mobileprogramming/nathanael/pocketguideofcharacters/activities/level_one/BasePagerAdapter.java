package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.level_one;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.level_two.HomeFragment;

public class BasePagerAdapter extends FragmentStateAdapter {
    public BasePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ChatFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
