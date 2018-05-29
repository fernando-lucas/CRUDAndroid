package com.fernando.crudandroid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ContatosAdapter extends BaseAdapter {

    private Context ctx;
    private List<Contato> lista;

    public ContatosAdapter(Context ctx2, List<Contato> lista2){
        ctx = ctx2;
        lista = lista2;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Contato getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.item_lista, null);
        } else {
            v = convertView;
        }

        Contato c = getItem(position);

        TextView textView_itemNome = (TextView) v.findViewById(R.id.textView_itemNome);
        TextView textView_itemTelefone = (TextView) v.findViewById(R.id.textView_itemTelefone);
        TextView textView_itemEmail = (TextView) v.findViewById(R.id.textView_itemEmail);

        textView_itemNome.setText(c.getNome());
        textView_itemTelefone.setText(c.getTelefone());
        textView_itemEmail.setText(c.getEmail());

        return v;
    }
}
