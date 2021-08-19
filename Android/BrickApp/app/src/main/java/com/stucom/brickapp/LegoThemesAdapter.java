package com.stucom.brickapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stucom.brickapp.model.LegoTheme;
import com.stucom.brickapp.model.LegoThemes;

public class LegoThemesAdapter extends BaseAdapter {

    private Context context;
    private LegoThemes themes;

    public LegoThemesAdapter(Context context, LegoThemes themes) {
        this.context = context;
        this.themes = themes;
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Object getItem(int position) {
        return themes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return themes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, null);
        TextView textView = view.findViewById(android.R.id.text1);
        LegoTheme theme = themes.get(position);
        textView.setText(theme.getName());
        return view;
    }

    public int getItemPositionById(long id){
        for (LegoTheme theme : themes) {
            if(theme.getId() == id){
                return  themes.indexOf(theme);
            }
        }

        for (int i = 0; i < themes.size() ; i++) {
            if(themes.get(i).getId() == id){
                return i;
            }
        }

        return 0;
    }
}
