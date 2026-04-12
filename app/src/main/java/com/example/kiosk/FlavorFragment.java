package com.example.kiosk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FlavorFragment extends BottomSheetDialogFragment {

    public FlavorFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_flavour, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View classic = view.findViewById(R.id.classicBtn);
        View inasal = view.findViewById(R.id.inasalBtn);
        View biryani = view.findViewById(R.id.biryaniBtn);
        View bbq = view.findViewById(R.id.bbqBtn);

        classic.setOnClickListener(v -> selectFlavor("Classic"));
        inasal.setOnClickListener(v -> selectFlavor("Inasal"));
        biryani.setOnClickListener(v -> selectFlavor("Biryani"));
        bbq.setOnClickListener(v -> selectFlavor("BBQ"));
    }
    public interface FlavorListener {
        void onFlavorSelected(String flavor);
    }

    private FlavorListener listener;

    public void setFlavorListener(FlavorListener listener) {
        this.listener = listener;
    }
    private void selectFlavor(String flavor){

        if (listener != null) {
            listener.onFlavorSelected(flavor);
        }

        dismiss(); // close bottom sheet
    }
}
