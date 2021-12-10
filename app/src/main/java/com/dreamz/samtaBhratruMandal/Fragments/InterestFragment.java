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


public class InterestFragment extends Fragment {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    MainActivity activity;

    public InterestFragment() {
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
        View view = inflater.inflate(R.layout.fragment_interest, container, false);

        viewPager = view.findViewById(R.id.viewpager2);
        tabLayout = view.findViewById(R.id.tabLayout);

        MainActivity.toolbar.setTitle("Account Interest");

        // ((TabLayout) view.findViewById(R.id.tabLayout)).setupWithViewPager( viewPager );

        //addTabs(viewPager);

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

   /* private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapter.addFrag(new SendFragment(), "Sent");
                adapter.addFrag(new ReceiveFragment(), "Received");

                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

            }
        });

    }*/

    /*
    public void getTabs(){
        final PagerAdapter pagerAdapter= new PagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                pagerAdapter.addFragment(RecentFragment.getInstance(),"Recent Chats");
                pagerAdapter.addFragment(UserFragment.getInstance(),"Connect with Living Beauties");
                binding.viewPager.setAdapter(pagerAdapter);

                binding.tabLayout.setupWithViewPager(binding.viewPager);

            }
        });


    }
    * */

    private InterestFragment.ViewPagerAdapter createCardAdapter() {
        InterestFragment.ViewPagerAdapter adapter = new InterestFragment.ViewPagerAdapter(activity);
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
                    return new SendFragment();
                case 1:
                    return new ReceiveFragment();
                default:
                    return new SendFragment();
            }
        }

        @Override
        public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }
}