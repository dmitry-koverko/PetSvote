package com.petsvote.pet.adapter;

import android.content.Context;
import android.graphics.Color;
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

    public void addItem(PetPhoto photo){
        mItems.add(photo);
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

        PetPhoto item = mItems.get(position);
        if(item.getBitmap() != null){
            holder.handleView.setImageBitmap(mItems.get(position).getBitmap());
            holder.add_photo.setVisibility(View.GONE);
        }

        holder.root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDragStartListener.onClick();
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
        public final ImageView handleView;
        public final ImageView add_photo;
        public final ConstraintLayout root;
        public final View select;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.pet_photo);
            add_photo = (ImageView) itemView.findViewById(R.id.add_photo);
            root = (ConstraintLayout) itemView.findViewById(R.id.root);
            select = (View) itemView.findViewById(R.id.select);
        }

        @Override
        public void onItemSelected() {
            select.setAlpha(0.3f);
        }

        @Override
        public void onItemClear() {
            select.setAlpha(0.0f);
        }
    }
}