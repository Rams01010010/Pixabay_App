package com.ramsolaiappan.pixabay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramsolaiappan.pixabay.GlobalVars;
import com.ramsolaiappan.pixabay.Image;
import com.ramsolaiappan.pixabay.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Image> imageList;
    private int mResource;
    private Context mContext;
    public OnItemClickListener itemClickListener;

    public interface OnItemClickListener
    {
        void OnClick(int position);
        void OnLiked(int position,boolean liked);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageIV;
        ImageView userImageIV;
        TextView userNameTV;
        TextView userIdTV;
        TextView viewsTV;
        TextView likesTV;
        TextView downloadsTV;
        ImageView likeIV;
        public ViewHolder(View itemView,OnItemClickListener itemClickListener) {
            super(itemView);

            imageIV = (ImageView) itemView.findViewById(R.id.imageIV);
            userImageIV = (ImageView) itemView.findViewById(R.id.userImageIV);
            likeIV = (ImageView) itemView.findViewById(R.id.likesIV);
            userNameTV = (TextView) itemView.findViewById(R.id.userNameTV);
            userIdTV = (TextView) itemView.findViewById(R.id.userIdTV);
            viewsTV = (TextView) itemView.findViewById(R.id.viewsTV);
            likesTV = (TextView) itemView.findViewById(R.id.likesTV);
            downloadsTV = (TextView) itemView.findViewById(R.id.downloadsTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null)
                    {
                        if(getAdapterPosition() != RecyclerView.NO_POSITION)
                        {
                            itemClickListener.OnClick(getAdapterPosition());
                        }
                    }
                }
            });

            likeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null)
                    {
                        if(getAdapterPosition() != RecyclerView.NO_POSITION)
                        {
                            boolean isliked;
                            if(likeIV.getTag().toString().equals("false"))
                                isliked = true;
                            else
                                isliked = false;
                            itemClickListener.OnLiked(getAdapterPosition(),isliked);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(this.mResource,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image currentImage = imageList.get(position);
        Picasso.get().load(currentImage.getImageUrl()).fit().centerCrop().into(holder.imageIV);
        if(!currentImage.getUserImageUrl().equals(""))
            Picasso.get().load(currentImage.getUserImageUrl()).fit().centerInside().into(holder.userImageIV);
        else
            holder.userImageIV.setImageResource(R.drawable.nodp);
        holder.userNameTV.setText(currentImage.getUser());
        holder.userIdTV.setText(currentImage.getUserId());
        holder.viewsTV.setText(String.valueOf(currentImage.getViews()));
        holder.likesTV.setText(String.valueOf(currentImage.getLikes()));
        holder.downloadsTV.setText(String.valueOf(currentImage.getDownloads()));
        if(currentImage.isLiked())
            holder.likeIV.setImageResource(R.drawable.ic_like_fill);
        else
            holder.likeIV.setImageResource(R.drawable.ic_like_outline);
        holder.likeIV.setTag(String.valueOf(currentImage.isLiked()));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public ImageAdapter(Context context, int resource,ArrayList<Image> imageList) {
        this.mContext = context;
        this.mResource = resource;
        this.imageList = imageList;
    }
}
