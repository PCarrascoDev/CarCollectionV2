package com.desafiolatam.carcollectionv2.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.desafiolatam.carcollectionv2.R;
import com.desafiolatam.carcollectionv2.data.CurrentUser;
import com.desafiolatam.carcollectionv2.model.Car;
import com.desafiolatam.carcollectionv2.views.car.CarActivity;
import com.desafiolatam.carcollectionv2.views.cars.CarsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


/**
 * Created by Pedro on 05-07-2017.
 */

public class CarsAdapter extends FirebaseRecyclerAdapter<Car, CarsAdapter.CarHolder>{

    private CarsListener listener;

    public CarsAdapter(CarsListener listener) {
        super(Car.class, R.layout.list_item_car, CarHolder.class, new CurrentUser().getReference());
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(final CarHolder viewHolder, Car model, int position) {
        String tvText = model.getBrand() + " " + model.getModel() + " " + model.getYear();
        viewHolder.textView.setText(tvText);
        if (model.getPhoto() != null)
        {
            Picasso.with(viewHolder.imageView.getContext()).load(model.getPhoto()).fit().centerCrop().into(viewHolder.imageView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clicked(getItem(viewHolder.getAdapterPosition()));
            }
        });
    }



    public static class CarHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private CircularImageView imageView;

        public CarHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.titleTv);
            imageView = (CircularImageView) itemView.findViewById(R.id.imageTv);
        }
    }
}
