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
import com.example.epicure.Activity.CartActivity;
import com.example.epicure.Domain.Foods;
import com.example.epicure.Helper.ChangeNumberItemsListener;
import com.example.epicure.Helper.ManagmentCart;
import com.example.epicure.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> list,Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
      holder.titleTxt_Cart.setText(list.get(position).getTitle());
      holder.feeEachItem.setText("$"+(list.get(position).getNumberInCart()*list.get(position).getPrice()));
      holder.totalEachItem.setText(list.get(position).getNumberInCart() + " * $" + (
              list.get(position).getPrice()));
      holder.numberItemTxt.setText(list.get(position).getNumberInCart()+"");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(),new RoundedCorners(30))
                .into(holder.pic_cart);

        holder.plusCartBtn.setOnClickListener(view -> managmentCart.plusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.minusCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managmentCart.minusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt_Cart,feeEachItem, plusCartBtn, minusCartBtn;
        ImageView pic_cart;
        TextView totalEachItem , numberItemTxt;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            titleTxt_Cart = itemView.findViewById(R.id.titleTxt_Cart);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusCartBtn = itemView.findViewById(R.id.plusCartBtn);
            minusCartBtn = itemView.findViewById(R.id.minusCartBtn);
            pic_cart = itemView.findViewById(R.id.pic_cart);
            totalEachItem = itemView.findViewById(R.id.total_each_item);
            numberItemTxt = itemView.findViewById(R.id.number_item_txt);
        }
    }
}
