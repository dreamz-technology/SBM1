package com.dreamz.samtaBhratruMandal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.dreamz.samtaBhratruMandal.Activities.MainActivity;
import com.dreamz.samtaBhratruMandal.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ScheduleMettingFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private MainActivity activity;

    public ScheduleMettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduled_metting, container, false);

        viewPager = view.findViewById(R.id.viewpager3);
        tabLayout = view.findViewById(R.id.tabLayout);
        MainActivity.toolbar.setTitle("Schedule Video Meeting");
        //addTabs(viewPager);
        //((TabLayout) view.findViewById(R.id.tabLayout)).setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setAdapter(createCardAdapter());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("SENT");
                        break;
                    case 1:
                        tab.setText("RECEIVED");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

    private ScheduleMettingFragment.ViewPagerAdapter createCardAdapter() {
        ScheduleMettingFragment.ViewPagerAdapter adapter = new ScheduleMettingFragment.ViewPagerAdapter(activity);
        return adapter;
    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 2;

        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SendScheduleMettingFragment();
                case 1:
                    return new ReceiveScheduleMettingFragment();
                default:
                    return new SendScheduleMettingFragment();
            }
        }

        @Override
        public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

    /*private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new SendScheduleMettingFragment(), "Sent");
        adapter.addFrag(new ReceiveScheduleMettingFragment(), "Received");

        viewPager.setAdapter(adapter);
    }*/
}