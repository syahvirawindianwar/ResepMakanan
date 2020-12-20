package com.example.resepmakanan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ResepAdapter extends BaseAdapter {
    List<Resep> reseps;
    Context context;
    LayoutInflater inflater;

    public ResepAdapter(List<Resep> reseps, Context context) {
        this.reseps = reseps;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reseps.size();
    }

    @Override
    public Object getItem(int i) {
        return reseps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_resep, null);
            holder = new ViewHolder();
            holder.textResep = convertView.findViewById(R.id.textMakanan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textResep.setText(reseps.get(position).getMakanan());
        Log.d("sss",reseps.get(position).getMakanan());

        return convertView;
    }

    static class ViewHolder {
        TextView textResep;
    }
}
