package com.windmill.dam2.windmillweather;

import android.content.Context;
import android.content.SharedPreferences;

public class AlmacenPreferencias {
    private static String PREFERECNIAS="provincias";
    private static Context context;
    public AlmacenPreferencias (Context context){
        this.context=context;
    }

    public void guardarPreferencias(String provincia, String localidad){
        SharedPreferences preferencias =context.getSharedPreferences(PREFERECNIAS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("provincia",provincia);
        editor.putString("localidad",localidad);
        editor.apply();
    }



}
