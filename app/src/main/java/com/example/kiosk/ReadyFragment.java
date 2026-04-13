package com.example.kiosk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReadyFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReadyAdapter adapter;
    private ArrayList<Order> usersList = new ArrayList<>();

    public ReadyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // SAMPLE DATA (for testing only)
        Order o1 = new Order();
        o1.setId("0001");
        o1.setOrderType("DINE IN");
        o1.setStatus("ready");
        o1.setItems(new ArrayList<>());

        Order o2 = new Order();
        o2.setId("0002");
        o2.setOrderType("TAKE OUT");
        o2.setStatus("ready");
        o2.setItems(new ArrayList<>());

        usersList.add(o1);
        usersList.add(o2);

        adapter = new ReadyAdapter(usersList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}