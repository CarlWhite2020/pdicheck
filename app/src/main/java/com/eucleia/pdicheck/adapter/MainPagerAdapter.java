package com.eucleia.pdicheck.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;


/**
 * frag 适配器
 */
public class MainPagerAdapter extends FragmentStateAdapter {


    private List<Fragment> fragments;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        //FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        this.fragments = fragments;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}

