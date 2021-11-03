package com.ramsolaiappan.pixabay.Adapters;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource;

    public SpinnerAdapter(Context context, int resource, ArrayList<String> strings) {
        super(context, resource, strings);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(mResource,null,false);
        TextView tv = convertView.findViewById(com.ramsolaiappan.pixabay.R.id.spinnerText);
        tv.setText(getItem(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(mContext).inflate(mResource,null,false);
        TextView tv = convertView.findViewById(com.ramsolaiappan.pixabay.R.id.spinnerText);
        tv.setText(getItem(position));
        return convertView;
    }
}
