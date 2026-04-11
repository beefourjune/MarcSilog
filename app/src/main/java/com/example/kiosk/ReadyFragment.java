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
    private ArrayList<String> usersList;

    public ReadyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample users
        usersList = new ArrayList<>();
        usersList.add("John Doe - admin");
        usersList.add("Jane Smith - user");
        usersList.add("Mark Lee - user");

        adapter = new ReadyAdapter(usersList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
