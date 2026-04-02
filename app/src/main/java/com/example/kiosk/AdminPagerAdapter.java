package com.example.kiosk;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminPagerAdapter extends FragmentStateAdapter {

    private final int tabCount;

    public AdminPagerAdapter(@NonNull FragmentActivity fragmentActivity, int tabCount) {
        super(fragmentActivity);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new OrdersFragment(); // Pending Orders tab
            case 1: return new MenuFragment();   // Menu tab
            case 2: return new UsersFragment();  // Users tab
            default: return new OrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
