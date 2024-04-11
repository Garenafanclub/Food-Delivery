package com.example.epicure.Adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.epicure.Domain.Category;
import com.example.epicure.Domain.Foods;
import com.example.epicure.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.veiwholder_category,parent,false);
        return new viewholder(inflate);
    }

    @SuppressLint("SetTextI18n") // your choice...
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {

        holder.titleTxt.setText(items.get(position).getName());
        switch (position)
        {
            case 0:{
                holder.pic.setBackgroundResource(R.drawable.card_1_background);
                break;
            }
            case 1:{
                holder.pic.setBackgroundResource(R.drawable.card_2_background);
                break;
            }
            case 2:{
                holder.pic.setBackgroundResource(R.drawable.card_3_background);
                break;
            }
            case 3:{
                holder.pic.setBackgroundResource(R.drawable.card_4_background);
                break;
            }
            case 4:{
                holder.pic.setBackgroundResource(R.drawable.card_5_background);
                break;
            }
            case 5:{
                holder.pic.setBackgroundResource(R.drawable.card_6_background);
                break;
            }
            case 6:{
                holder.pic.setBackgroundResource(R.drawable.card_7_background);
                break;
            }
            case 7:{
                holder.pic.setBackgroundResource(R.drawable.card_8_background);
                break;
            }
        }

        int drawableResourceId = context.getResources().getIdentifier(items.get(position).getImagePath(),
                "drawable",holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {   // make a static or not...
        TextView titleTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.cardNameText );
            pic = itemView.findViewById(R.id.imgCard);
        }
    }
}
