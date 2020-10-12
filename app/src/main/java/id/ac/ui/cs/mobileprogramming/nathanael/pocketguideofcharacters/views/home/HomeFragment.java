package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.cardlist.CardListFragment;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.create.CreateCharacterFragment;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.viewpager.ZoomOutPageTransformer;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.viewmodels.NavigationViewModel;

/**
 * Main and landing activity, using bottom navigation and several fragment.
 *
 * @author Nathanael
 */
public class HomeFragment extends Fragment {

    View root;
    ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_landing, container, false);

        viewPager2 = root.findViewById(R.id.home_pager);
        viewPager2.setAdapter(new HomePagerAdapter(requireActivity()));
        viewPager2.setUserInputEnabled(false);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());

        TabLayout tabLayout = root.findViewById(R.id.botton_tab_navigation);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout,
                viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setIcon(R.drawable.ic_home_black_24dp);
                                break;
                            case 1:
                                tab.setIcon(R.drawable.ic_baseline_add_24);
                                break;
                        }
                    }
                }
        );
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutMediator.attach();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        NavigationViewModel navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        navigationViewModel.getActive().setValue(viewPager2.getCurrentItem() != 1);

        String title = "Home";
        if (viewPager2.getCurrentItem() == 1) {
            title = "Create";
        }
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(title);
    }

    static class HomePagerAdapter extends FragmentStateAdapter {

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
}
