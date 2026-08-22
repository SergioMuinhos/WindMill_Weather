package com.windmill.dam2.windmillweather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.activity.EdgeToEdge;
import androidx.core.os.LocaleListCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Spinner spinnerProvincias;
    private Spinner spinnerLocalidades;
    public String idZona = "";
    public int idProv = 0;
    TextView textview;
    ProgressBar pDialog;
    static ImageView imgView;
    TabItem hoy, manana, pasado;
    private boolean isInitialLoad = true;
    private PredConcello currentPredConcello;
    private int currentTabPosition = 0;

    public String URL2 = "https://servizos.meteogalicia.gal/mgrss/predicion/jsonPredConcellos.action?idConc=";
    String[] provincias = new String[]{"Pontevedra", "Lugo", "Ourense", "A Coruña"};
    public String[] pontevedra = new String[]{"Arbo", "Barro", "Baiona", "Bueu", "Caldas de Reis",
            "Cambados", "Campo Lameiro", "Cangas", "A Cañiza", "Catoira", "Cerdedo", "Cotobade",
            "Covelo", "Crecente", "Cuntis", "Dozón", "A Estrada", "Forcarei", "Fornelos de Montes",
            "Agolada", "Gondomar", "O Grove", "A Guarda", "Lalín", "A Lama", "Marín", "Meaño",
            "Meis", "Moaña", "Mondariz", "Mondariz-Balneario", "Moraña", "Mos", "As Neves", "Nigrán", "Oia",
            "Pazos de Borbén", "Pontevedra", "O Porriño", "Portas", "Poio", "Ponteareas", "Ponte Caldelas", "Pontecesures",
            "Redondela", "Ribadumia", "Rodeiro", "O Rosal", "Salceda de Caselas",
            "Salvaterra de Miño", "Sanxenxo", "Silleda", "Soutomaior", "Tomiño", "Tui", "Valga",
            "Vigo", "Vilaboa", "Vila de Cruces", "Vilagarcía de Arousa", "Vilanova de Arousa"};

    public String[] ourense = new String[]{"Allariz", "Amoeiro", "A Arnoia", "Avión", "Baltar", "Bande", "Baños de Molgas", "Barbadás",
            "O Barco de Valdeorras", "Beade", "Beariz", "Os Blancos", "Boborás", "A Bola", "O Bolo", "Calvos de Randín",
            "Carballeda de Avia", "Carballeda de Valdeorras", "O Carballiño",
            "Cartelle", "Castrelo do Val", "Castrelo de Miño", "Castro Caldelas", "Celanova",
            "Cenlle", "Coles", "Cortegada", "Cualedro", "Chandrexa de Queixa", "Entrimo", "Esgos", "Xinzo de Limia", "Gomesende", "A Gudiña",
            "O Irixo", "Xunqueira de Ambía", "Xunqueira de Espadanedo", "Larouco", "Laza", "Leiro", "Lobeira", "Lobios", "Maceda", "Manzaneda", "Maside", "Melón", "A Merca",
            "A Mezquita", "Montederramo", "Monterrei", "Muíños", "Nogueira de Ramuín", "Oímbra", "Ourense", "Paderne de Allariz",
            "Padrenda", "Parada de Sil", "O Pereiro de Aguiar", "A Peroxa", "Petín", "Piñor", "Porqueira", "A Pobra de Trives", "Pontedeva",
            "Punxín", "Quintela de Leirado", "Rairiz de Veiga", "Ramirás", "Ribadavia", "San Xoán de Río", "Riós", "A Rúa", "Rubiá",
            "San Amaro", "San Cibrao das Viñas", "San Cristovo de Cea", "Sandiás", "Sarreaus", "Taboadela", "A Teixeira",
            "Toén", "Trasmiras", "(A)Veiga", "Verea", "Verín", "Viana do Bolo", "Vilamarín", "Vilamartín de Valdeorras",
            "Vilar de Barrio", "Vilar de Santos", "Vilardevós", "Vilariño de Conso"};

    public String[] lugo = new String[]{"Abadín", "Alfoz", "Antas de Ulla", "Baleira", "Barreiros", "Becerreá", "Begonte", "Bóveda", "Carballedo",
            "Castro de Rei", "Castroverde", "Cervantes", "Cervo", "O Corgo", "Cospeito", "Chantada", "Folgoso do Courel",
            "A Fonsagrada", "Foz", "Friol", "Xermade", "Guitiriz", "Guntín", "O Incio", "Xove", "Láncara", "Lourenzá",
            "Lugo", "Meira", "Mondoñedo", "Monforte de Lemos", "Monterroso", "Muras", "Navia de Suarna",
            "Negueira de Muñiz", "", "As Nogais", "Ourol", "Outeiro de Rei", "Palas de Re", "Pantón", "Paradela", "O Páramo", "A Pastoriza",
            "Pedrafita do Cebreiro", "Pol", "A Pobra do Brollón", "A Pontenova", "Portomarín", "Quiroga", "Ribadeo",
            "Ribas de Sil", "Ribeira de Piquín", "Riotorto", "Samos", "Rábade", "Sarria", "O Saviñao", "Sober", "Taboada",
            "Trabada", "Triacastela", "O Valadouro", "O Vicedo", "Vilalba", "Viveiro"};

    public String[] coruna = new String[]{"Abegondo", "Ames", "Aranga", "Ares", "Arteixo", "Arzúa", "A Baña", "Bergondo", "Betanzos",
            "Boimorto", "Boiro", "Boqueixón", "Brión", "Cabana de Bergantiños", "Cabanas", "Camariñas", "Cambre", "A Capela",
            "Carballo", "Carnota", "Carral", "Cedeira", "Cee", "Cerceda", "Cerdido", "Cesuras", "Coirós", "Corcubión",
            "Coristanco", "A Coruña", "Culleredo", "Curtis", "Dodro", "Dumbría", "Fene", "Ferrol", "Fisterra", "Frades", "Irixoa",
            "Laxe", "A Laracha", "Lousame", "Malpica de Bergantiños", "Mañón", "Mazaricos", "Melide", "Mesía", "Miño", "Moeche",
            "Monfero", "Mugardos", "Muxía", "Muros", "Narón", "Neda", "Negreira", "Noia", "Oleiros", "Ordes", "Oroso", "Ortigueira",
            "Outes", "Oza dos Ríos", "Paderne", "Padrón", "O Pino", "Pobra do Caramiñal", "Ponteceso", "Pontedeume", "As Pontes de García Rodríguez",
            "Porto do Son", "Rianxo", "Ribeira", "Rois", "Sada", "San Sadurniño", "Santa Comba", "Santiago de Compostela", "Santiso",
            "Sobrado", "As Somozas", "Teo", "Toques", "Tordoia", "Touro", "Trazo", "Valdoviño", "Val do Dubra", "Vedra",
            "Vilasantar", "Vilarmaior", "Vimianzo", "Zas"};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);

        // Configuración de Modo Oscuro
        final boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        int targetMode = isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        if (AppCompatDelegate.getDefaultNightMode() != targetMode) {
            AppCompatDelegate.setDefaultNightMode(targetMode);
        }

        // Configuración de Idioma
        String currentLang = sharedPreferences.getString("app_language", "system");
        if (!"system".equals(currentLang)) {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(currentLang);
            if (!appLocale.equals(AppCompatDelegate.getApplicationLocales())) {
                AppCompatDelegate.setApplicationLocales(appLocale);
            }
        }

        final int selectedProvincias = sharedPreferences.getInt("provincias", 0);
        final int selectedLocalidades = sharedPreferences.getInt("localidades", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // Spinner de Provincias y Localidades
        spinnerProvincias = findViewById(R.id.provincia);
        spinnerLocalidades = findViewById(R.id.localidad);
        pDialog = findViewById(R.id.pBar);
        ArrayAdapter<String> adapterProv = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, provincias);
        adapterProv.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerProvincias.setAdapter(adapterProv);

        spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapterLoc = null;

                switch (position) {
                    case 0:
                        idProv = 360;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, pontevedra);
                        break;
                    case 1:
                        idProv = 270;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, lugo);
                        break;
                    case 2:
                        idProv = 320;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, ourense);
                        break;
                    case 3:
                        idProv = 150;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, coruna);
                        break;
                }

                if (adapterLoc != null) {
                    adapterLoc.setDropDownViewResource(R.layout.spinner_dropdown_item);
                }

                // Guardar la provincia seleccionada
                sharedPreferences.edit().putInt("provincias", position).apply();

                spinnerLocalidades.setAdapter(adapterLoc);

                // Si es la carga inicial, seleccionamos la localidad guardada
                if (isInitialLoad) {
                    spinnerLocalidades.setSelection(selectedLocalidades);
                    isInitialLoad = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLocalidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Guardar la localidad seleccionada
                sharedPreferences.edit().putInt("localidades", position).apply();

                idZona = idProv + String.format("%02d", position + 1);
                sharedPreferences.edit().putString("idZona", idZona).apply();
                String enlaces = URL2 + idZona;

                try {
                    if (!isOnline(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(R.string.dialog_no_connection)
                                .setTitle(R.string.dialog_error_title)
                                .setCancelable(false)
                                .setPositiveButton(R.string.dialog_retry, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                        builder.create();
                        builder.show();
                    } else {
                        new DownloadJSON().execute(enlaces);
                    }
                } catch (Exception e) {
                    Log.e("Error conexion", e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerProvincias.setSelection(selectedProvincias);

        TabLayout pestanas = findViewById(R.id.tabs);
        if (pestanas != null && pestanas.getTabCount() >= 3) {
            TabLayout.Tab t0 = pestanas.getTabAt(0);
            if (t0 != null) t0.setText(R.string.today);
            TabLayout.Tab t1 = pestanas.getTabAt(1);
            if (t1 != null) t1.setText(R.string.tomorrow);
            TabLayout.Tab t2 = pestanas.getTabAt(2);
            if (t2 != null) t2.setText(R.string.after);
        }

        pestanas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    currentTabPosition = tab.getPosition();
                    updateUIForDay(currentTabPosition);
                } catch (Exception e) {
                    Log.e("Error en OnTabSelected", e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        if (nestedScrollView != null) {
            nestedScrollView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                @Override
                public void onSwipeLeft() {
                    int nextTab = currentTabPosition + 1;
                    if (nextTab < pestanas.getTabCount()) {
                        TabLayout.Tab tab = pestanas.getTabAt(nextTab);
                        if (tab != null) {
                            tab.select();
                        }
                    }
                }

                @Override
                public void onSwipeRight() {
                    int prevTab = currentTabPosition - 1;
                    if (prevTab >= 0) {
                        TabLayout.Tab tab = pestanas.getTabAt(prevTab);
                        if (tab != null) {
                            tab.select();
                        }
                    }
                }
            });
        }
    }

    private class DownloadJSON extends AsyncTask<String, Void, PrediccionResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.pDialog.setVisibility(View.VISIBLE);
            View container = MainActivity.this.findViewById(R.id.weatherDataContainer);
            if (container != null) {
                container.setVisibility(View.GONE);
            }
        }

        @Override
        protected PrediccionResponse doInBackground(String... Url) {
            String jsonResult = "";
            try {
                URL url = new URL(Url[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                is.close();

                jsonResult = sb.toString();
                return new Gson().fromJson(jsonResult, PrediccionResponse.class);
            } catch (Exception e) {
                Log.e("Error en doInBackground", e.getMessage() != null ? e.getMessage() : "Error");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            MainActivity.this.pDialog.setVisibility(View.INVISIBLE);
            View container = MainActivity.this.findViewById(R.id.weatherDataContainer);
            if (container != null) {
                container.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(PrediccionResponse result) {
            MainActivity.this.pDialog.setVisibility(View.INVISIBLE);
            View container = MainActivity.this.findViewById(R.id.weatherDataContainer);
            if (container != null) {
                container.setVisibility(View.VISIBLE);
            }
            if (result != null && result.predConcello != null) {
                currentPredConcello = result.predConcello;
                updateUIForDay(currentTabPosition);
                try {
                    String jsonString = new Gson().toJson(result);
                    sharedPreferences.edit().putString("last_weather_data", jsonString).apply();
                    updateWidget();
                } catch (Exception e) {
                    Log.e("MainActivity", "Error saving weather cache for widget", e);
                }
            } else {
                Toast.makeText(MainActivity.this, R.string.error_download_prediction, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateWidget() {
        Intent intent = new Intent(this, WeatherWidgetProvider.class);
        intent.setAction(android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = android.appwidget.AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new android.content.ComponentName(getApplication(), WeatherWidgetProvider.class));
        intent.putExtra(android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    private void updateUIForDay(int dayIndex) {
        if (currentPredConcello == null || currentPredConcello.listaPredDiaConcello == null
                || dayIndex >= currentPredConcello.listaPredDiaConcello.size()) {
            return;
        }

        DiaConcello dia = currentPredConcello.listaPredDiaConcello.get(dayIndex);

        // Nombre del concello
        TextView tvCity = findViewById(R.id.cityText);
        if (tvCity != null) {
            tvCity.setText(currentPredConcello.nome.toUpperCase());
        }

        // Temperaturas máximas y mínimas
        TextView tvTempMax = findViewById(R.id.tempTextMax);
        if (tvTempMax != null) {
            tvTempMax.setText(dia.tMax != null ? dia.tMax + "ºC" : "--ºC");
            new cargarImagenTempMax().execute("https://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/termometros/405.png");
        }

        TextView tvTempMin = findViewById(R.id.tempTextMin);
        if (tvTempMin != null) {
            tvTempMin.setText(dia.tMin != null ? dia.tMin + "ºC" : "--ºC");
            new cargarImagenTempMin().execute("https://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/termometros/400.png");
        }

        // Mañana: Cielo, Viento, Lluvia
        if (dia.ceo != null && dia.ceo.manha != null) {
            new cargarImagenCieloM().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + dia.ceo.manha + ".png");
        }
        if (dia.vento != null && dia.vento.manha != null) {
            new cargarImagenVientoM().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/" + dia.vento.manha + ".png");
        }
        TextView tvRainM = findViewById(R.id.textML);
        if (tvRainM != null) {
            tvRainM.setText(dia.pchoiva != null && dia.pchoiva.manha != null ? dia.pchoiva.manha + "%" : "00%");
        }

        // Tarde: Cielo, Viento, Lluvia
        if (dia.ceo != null && dia.ceo.tarde != null) {
            new cargarImagenCieloT().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + dia.ceo.tarde + ".png");
        }
        if (dia.vento != null && dia.vento.tarde != null) {
            new cargarImagenVientoT().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/" + dia.vento.tarde + ".png");
        }
        TextView tvRainT = findViewById(R.id.textTL);
        if (tvRainT != null) {
            tvRainT.setText(dia.pchoiva != null && dia.pchoiva.tarde != null ? dia.pchoiva.tarde + "%" : "00%");
        }

        // Noche: Cielo, Viento, Lluvia
        if (dia.ceo != null && dia.ceo.noite != null) {
            new cargarImagenCieloN().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + dia.ceo.noite + ".png");
        }
        if (dia.vento != null && dia.vento.noite != null) {
            new cargarImagenVientoN().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/" + dia.vento.noite + ".png");
        }
        TextView tvRainN = findViewById(R.id.textNL);
        if (tvRainN != null) {
            tvRainN.setText(dia.pchoiva != null && dia.pchoiva.noite != null ? dia.pchoiva.noite + "%" : "00%");
        }

        // Fecha Predicción
        TextView tvActualizacion = findViewById(R.id.txtActualizacion);
        if (tvActualizacion != null && dia.dataPredicion != null) {
            String rawDate = dia.dataPredicion.length() >= 10 ? dia.dataPredicion.substring(0, 10) : dia.dataPredicion;
            String formattedDate = rawDate;
            try {
                String separator = "-";
                if (rawDate.contains("/")) {
                    separator = "/";
                }
                String[] parts = rawDate.split(separator);
                if (parts.length == 3) {
                    formattedDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                }
            } catch (Exception e) {
                Log.e("Date Formatter", "Error formatting date: " + rawDate, e);
            }
            tvActualizacion.setText(getString(R.string.prediction_prefix) + formattedDate);
        }
    }

    private void toggleTheme() {
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        boolean newMode = !isDarkMode;
        sharedPreferences.edit().putBoolean("dark_mode", newMode).apply();

        int targetMode = newMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(targetMode);
        recreate();
    }

    private void showLanguageDialog() {
        String currentLang = sharedPreferences.getString("app_language", "system");
        int selectedIndex = 2; // Predeterminado do sistema
        if ("gl".equals(currentLang)) {
            selectedIndex = 0;
        } else if ("es".equals(currentLang)) {
            selectedIndex = 1;
        }

        final String[] languages = new String[]{
                getString(R.string.language_galician),
                getString(R.string.language_spanish),
                getString(R.string.language_system)
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_language_title)
                .setSingleChoiceItems(languages, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedLangCode = "system";
                        if (which == 0) {
                            selectedLangCode = "gl";
                        } else if (which == 1) {
                            selectedLangCode = "es";
                        } else {
                            selectedLangCode = "system";
                        }
                        dialog.dismiss();
                        setAppLocale(selectedLangCode);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void setAppLocale(String langCode) {
        sharedPreferences.edit().putString("app_language", langCode).apply();
        if ("system".equals(langCode)) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(langCode));
        }
        updateWidget();
        recreate();
    }

    // Clases del Modelo de Datos para Gson
    public static class PrediccionResponse {
        public PredConcello predConcello;
    }

    public static class PredConcello {
        public int idConcello;
        public String nome;
        public List<DiaConcello> listaPredDiaConcello;
    }

    public static class DiaConcello {
        public String dataPredicion;
        public Integer tMax;
        public Integer tMin;
        public Integer ceoDia;
        public Integer uvMax;
        public FranxaTemp tmaxFranxa;
        public FranxaTemp tminFranxa;
        public FranxaChoiva pchoiva;
        public FranxaCeo ceo;
        public FranxaVento vento;
    }

    public static class FranxaTemp {
        public Integer manha;
        public Integer tarde;
        public Integer noite;
    }

    public static class FranxaChoiva {
        public Integer manha;
        public Integer tarde;
        public Integer noite;
    }

    public static class FranxaCeo {
        public Integer manha;
        public Integer tarde;
        public Integer noite;
    }

    public static class FranxaVento {
        public Integer manha;
        public Integer tarde;
        public Integer noite;
    }

    private InputStream openHttpConnection(String url) throws IOException {
        InputStream is = null;
        int responseCode;

        URLConnection connection;
        connection = (new URL(url)).openConnection();

        if (!(connection instanceof HttpURLConnection)) {
            throw new IOException("Not HTTP connection");
        }

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setAllowUserInteraction(false);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            is = httpURLConnection.getInputStream();
        }

        return is;
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream is;

        try {
            is = openHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }

    private class cargarImagenCieloM extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgMC);
            imgView.setImageBitmap(bitmap);
        }
    }

    private class cargarImagenCieloT extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgTC);
            imgView.setImageBitmap(bitmap);
        }
    }

    private class cargarImagenCieloN extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgNC);
            imgView.setImageBitmap(bitmap);
        }
    }

    private class cargarImagenTempMin extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgTempMin);
            imgView.setImageBitmap(bitmap);
        }
    }

    private class cargarImagenTempMax extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgTempMax);
            imgView.setImageBitmap(bitmap);
        }
    }

    private class cargarImagenVientoN extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgNV);
            imgView.setImageBitmap(bitmap);
            pDialog.setVisibility(View.INVISIBLE);
        }
    }

    private class cargarImagenVientoM extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgMV);
            imgView.setImageBitmap(bitmap);
        }
    }

    private class cargarImagenVientoT extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            pDialog.setVisibility(View.INVISIBLE);
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgTV);
            imgView.setImageBitmap(bitmap);
            pDialog.setVisibility(View.INVISIBLE);
        }
    }

    private static ConnectivityManager manager;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (themeItem != null) {
            themeItem.setIcon(isDarkMode ? R.drawable.ic_sun : R.drawable.ic_moon);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            toggleTheme();
            return true;
        } else if (item.getItemId() == R.id.action_language) {
            showLanguageDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
