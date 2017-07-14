package com.desafiolatam.carcollectionv2.views.cars;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desafiolatam.carcollectionv2.R;
import com.desafiolatam.carcollectionv2.adapter.CarsAdapter;
import com.desafiolatam.carcollectionv2.adapter.CarsListener;
import com.desafiolatam.carcollectionv2.model.Car;
import com.desafiolatam.carcollectionv2.views.car.CarActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class CarsActivityFragment extends Fragment implements CarsListener{

    private CarsAdapter adapter;

    public CarsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CarsAdapter(this);
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    @Override
    public void clicked(Car car) {
        Intent intent = new Intent(getActivity(), CarActivity.class);
        intent.putExtra("CAR", car);
        startActivity(intent);
    }
}
