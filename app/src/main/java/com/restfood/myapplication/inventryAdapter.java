package com.restfood.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
class InventryAdapter extends RecyclerView.Adapter<com.restfood.myapplication.InventryAdapter.InventryViewHolder> {

    ///use this list to show in list view
    private ArrayList<InventryData> itemListX;

    //private OnItemClickListener mListener;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(com.restfood.myapplication.InventryAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    //this is for ui view elements
    public static class InventryViewHolder extends RecyclerView.ViewHolder
    {
        ///create view for single unit
        public TextView nameTextView;
        //public TextView priceTextView;
        public TextView qtyTextView;

        public InventryViewHolder(@NonNull View itemView,final com.restfood.myapplication.InventryAdapter.OnItemClickListener listener) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.name_card);
            qtyTextView=itemView.findViewById(R.id.qty_card);
            //catTextView=itemView.findViewById(R.id.catTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }

                }
            });


        }
    }

    //constructor for this class
    public InventryAdapter(ArrayList<InventryData> list)
    {
        itemListX=list;
    }


    ///this is identfing the view component andr returnig
    @NonNull
    @Override
    public com.restfood.myapplication.InventryAdapter.InventryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_card, parent, false);
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.inventry_card, parent, false);
        com.restfood.myapplication.InventryAdapter.InventryViewHolder vi = new com.restfood.myapplication.InventryAdapter.InventryViewHolder(v,mListener);
        return vi;
    }


    //getting position of list and setting all values to that
    @Override
    public void onBindViewHolder(@NonNull com.restfood.myapplication.InventryAdapter.InventryViewHolder holder, int position) {
        InventryData currentdata=itemListX.get(position);

        holder.nameTextView.setText(currentdata.getName());
        holder.qtyTextView.setText(String.valueOf(currentdata.getQuantity()));
//
//        Log.d("Available check",String.valueOf(currentdata.getIsAvailable()));

    }


    //this returnig number of elements in this list
    @Override
    public int getItemCount() {
        return itemListX.size();
    }


}