package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class PersonajeAdapter extends BaseAdapter {

    private Context context;
    private List<Personaje> listaPersonajes;

    public PersonajeAdapter(Context context, List<Personaje> listaPersonajes) {
        this.context = context;
        this.listaPersonajes = listaPersonajes;
    }

    @Override
    public int getCount() {
        return listaPersonajes.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPersonajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_personaje, parent, false);
        }

        Personaje p = listaPersonajes.get(position);

        MaterialCardView card = convertView.findViewById(R.id.cardPersonaje);
        TextView tvNombre = convertView.findViewById(R.id.tvNombrePersonaje);
        TextView tvClase = convertView.findViewById(R.id.tvClasePersonaje);
        ProgressBar pbVida = convertView.findViewById(R.id.pbVidaPersonaje);
        TextView tvVidaTexto = convertView.findViewById(R.id.tvVidaTexto);
        TextView tvStats = convertView.findViewById(R.id.tvStatsPersonaje);

        tvNombre.setText(p.getNombre());

        pbVida.setMax(p.getVidaMax());
        pbVida.setProgress(p.getVida());
        tvVidaTexto.setText(p.getVida() + "/" + p.getVidaMax());
        tvStats.setText("⚔️ ATK: " + p.getAtaque() + "  🛡️ DEF: " + p.getDefensa());

        if (p instanceof Guerrero) {
            tvClase.setText("🛡️ Guerrero");
            tvClase.setTextColor(Color.parseColor("#FF5252"));
            card.setStrokeColor(Color.parseColor("#FF5252"));

        } else if (p instanceof Mago) {
            tvClase.setText("🔮 Mago");
            tvClase.setTextColor(Color.parseColor("#448AFF"));
            card.setStrokeColor(Color.parseColor("#448AFF"));

        } else if (p instanceof Mistico) {
            tvClase.setText("✨ Místico");
            tvClase.setTextColor(Color.parseColor("#E040FB"));
            card.setStrokeColor(Color.parseColor("#E040FB"));
        }

        return convertView;
    }
}
