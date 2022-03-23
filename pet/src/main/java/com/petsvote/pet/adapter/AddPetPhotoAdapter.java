package com.petsvote.pet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.petsvote.pet.R;
import com.petsvote.pet.adapter.helper.ItemTouchHelperAdapter;
import com.petsvote.pet.adapter.helper.ItemTouchHelperViewHolder;
import com.petsvote.pet.entity.PetPhoto;
import com.petsvote.ui.AnimatedRoundedImage;
import com.petsvote.ui.BesieLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddPetPhotoAdapter extends RecyclerView.Adapter<AddPetPhotoAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<PetPhoto> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;

    public AddPetPhotoAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    public void addData(List<PetPhoto> list){
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(Bitmap photo){
        for(PetPhoto i: mItems){
            if(i.getBitmap() == null) {
                i.setBitmap(photo);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void removeItem(int position){
        mItems.remove(position);
        notifyItemRemoved(position);
        mItems.add(mItems.size(), new PetPhoto(null));
        notifyItemInserted(mItems.size() -1);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_photo, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        DisplayMetrics dm = holder.root.getContext().getResources().getDisplayMetrics();
        int with = dm.widthPixels;
        int height = dm.heightPixels;
        float cardHeight = height * 0.21f;
        float cardWidth = (with - dm.density * 16) / 3;

        ViewGroup.LayoutParams lp = holder.root.getLayoutParams();
        lp.height = (int) cardHeight;
        lp.width = (int) cardWidth;
        holder.root.setLayoutParams(lp);

        PetPhoto item = mItems.get(position);
        if(item.getBitmap() != null){
            holder.handleView.setImageBitmap(mItems.get(position).getBitmap());
            holder.add_photo.setVisibility(View.GONE);
            holder.close.setVisibility(View.VISIBLE);
        }else {
            holder.add_photo.setVisibility(View.VISIBLE);
            holder.close.setVisibility(View.GONE);
            holder.handleView.setImageBitmap(null);
        }

        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(item.getBitmap() != null) mDragStartListener.onStartDrag(holder);
                return false;
            }
        });

        holder.handleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getBitmap() == null)  mDragStartListener.onClick();
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDragStartListener.onClose(position);
            }
        });

    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final AnimatedRoundedImage handleView;
        public final ImageView add_photo;
        public final ConstraintLayout root;
        public final BesieLayout close;
        public final View select;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (AnimatedRoundedImage) itemView.findViewById(R.id.pet_photo);
            add_photo = (ImageView) itemView.findViewById(R.id.add_photo);
            root = (ConstraintLayout) itemView.findViewById(R.id.root);
            select = (View) itemView.findViewById(R.id.select);
            close = (BesieLayout) itemView.findViewById(R.id.close);
        }

        @Override
        public void onItemSelected() {
            select.setAlpha(0.2f);
        }

        @Override
        public void onItemClear() {
            select.setAlpha(0.0f);
        }
    }
}