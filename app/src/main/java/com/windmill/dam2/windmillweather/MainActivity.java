package com.windmill.dam2.windmillweather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private PredConcello currentPredConcello;
    private int currentTabPosition = 0;
    private boolean errorDialogShowing = false;
    private static final String PREF_CURRENT_TAB = "current_tab";
    private static final String PREF_LAST_WEATHER_ZONE = "last_weather_idZona";

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
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("preferences", MODE_PRIVATE);
        String lang = prefs.getString("app_language", null);
        if (lang != null && !"system".equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration(newBase.getResources().getConfiguration());
            config.setLocale(locale);
            newBase = newBase.createConfigurationContext(config);
        }
        super.attachBaseContext(newBase);
    }

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

        final int selectedProvincias = sharedPreferences.getInt("provincias", 0);
        final int selectedLocalidades = sharedPreferences.getInt("localidades", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View appBar = findViewById(R.id.appbar);
        if (appBar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), bars.top, v.getPaddingRight(), 0);
                return insets;
            });
            ViewCompat.requestApplyInsets(appBar);
        }

        final NestedScrollView scrollView = findViewById(R.id.nestedScrollView);
        if (scrollView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bars.bottom);
                return insets;
            });
            ViewCompat.requestApplyInsets(scrollView);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            Configuration config = getResources().getConfiguration();
            boolean useShortTitle = config.screenWidthDp < 400 || config.fontScale > 1.15f;
            getSupportActionBar().setTitle(useShortTitle ? R.string.app_name_short : R.string.app_name);
        }

        // Spinner de Provincias y Localidades
        spinnerProvincias = findViewById(R.id.provincia);
        spinnerLocalidades = findViewById(R.id.localidad);
        pDialog = findViewById(R.id.pBar);

        ArrayAdapter<String> adapterProv = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, provincias);
        adapterProv.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerProvincias.setAdapter(adapterProv);

        int safeProvincia = Math.max(0, Math.min(provincias.length - 1, selectedProvincias));
        idProv = getProvinceId(safeProvincia);
        ArrayAdapter<String> initialLocalityAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, getLocalitiesForProvince(safeProvincia));
        initialLocalityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerLocalidades.setAdapter(initialLocalityAdapter);

        spinnerProvincias.setSelection(safeProvincia, false);
        int safeLocalidad = Math.max(0, Math.min(selectedLocalidades, initialLocalityAdapter.getCount() - 1));
        spinnerLocalidades.setSelection(safeLocalidad, false);
        idZona = idProv + String.format("%02d", safeLocalidad + 1);
        sharedPreferences.edit().putString("idZona", idZona).apply();

        setupSpinnerDialogs();

        currentTabPosition = sharedPreferences.getInt(PREF_CURRENT_TAB, 0);
        currentTabPosition = Math.max(0, Math.min(2, currentTabPosition));

        TabLayout pestanas = findViewById(R.id.tabs);
        if (pestanas != null && pestanas.getTabCount() >= 3) {
            TabLayout.Tab t0 = pestanas.getTabAt(0);
            if (t0 != null) {
                t0.setText(R.string.today);
                t0.setContentDescription(getString(R.string.tab_today_desc));
            }
            TabLayout.Tab t1 = pestanas.getTabAt(1);
            if (t1 != null) {
                t1.setText(R.string.tomorrow);
                t1.setContentDescription(getString(R.string.tab_tomorrow_desc));
            }
            TabLayout.Tab t2 = pestanas.getTabAt(2);
            if (t2 != null) {
                t2.setText(R.string.after);
                t2.setContentDescription(getString(R.string.tab_after_desc));
            }
        }

        pestanas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    currentTabPosition = tab.getPosition();
                    sharedPreferences.edit().putInt(PREF_CURRENT_TAB, currentTabPosition).apply();
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

        TabLayout.Tab restoredTab = pestanas.getTabAt(currentTabPosition);
        if (restoredTab != null) {
            restoredTab.select();
        }

        loadInitialWeather(savedInstanceState != null);
        attachSpinnerListeners();
    }

    private int getProvinceId(int position) {
        switch (position) {
            case 1:
                return 270;
            case 2:
                return 320;
            case 3:
                return 150;
            case 0:
            default:
                return 360;
        }
    }

    private String[] getLocalitiesForProvince(int position) {
        switch (position) {
            case 1:
                return lugo;
            case 2:
                return ourense;
            case 3:
                return coruna;
            case 0:
            default:
                return pontevedra;
        }
    }

    private String getSelectedLocalityName() {
        Object item = spinnerLocalidades.getSelectedItem();
        return item != null ? item.toString() : "";
    }

    private void attachSpinnerListeners() {
        spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idProv = getProvinceId(position);
                sharedPreferences.edit().putInt("provincias", position).apply();

                ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, getLocalitiesForProvince(position));
                adapterLoc.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerLocalidades.setAdapter(adapterLoc);
                spinnerLocalidades.setSelection(0, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerLocalidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("localidades", position).apply();

                idZona = idProv + String.format("%02d", position + 1);
                sharedPreferences.edit().putString("idZona", idZona).apply();

                setLoadingState(getSelectedLocalityName());
                if (!isOnline(getApplicationContext())) {
                    handleOfflineState();
                    return;
                }
                new DownloadJSON(idZona).execute(URL2 + idZona);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSpinnerDialogs() {
        spinnerProvincias.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showProvinceDialog();
            }
            return true;
        });
        spinnerLocalidades.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showLocalityDialog();
            }
            return true;
        });

        spinnerProvincias.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == AccessibilityNodeInfo.ACTION_CLICK) {
                    showProvinceDialog();
                    return true;
                }
                return super.performAccessibilityAction(host, action, args);
            }
        });
        spinnerLocalidades.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == AccessibilityNodeInfo.ACTION_CLICK) {
                    showLocalityDialog();
                    return true;
                }
                return super.performAccessibilityAction(host, action, args);
            }
        });
    }

    private void showProvinceDialog() {
        int selected = spinnerProvincias.getSelectedItemPosition();
        if (selected < 0) {
            selected = 0;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_province)
                .setSingleChoiceItems(provincias, selected, (dialog, which) -> {
                    spinnerProvincias.setSelection(which);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showLocalityDialog() {
        SpinnerAdapter adapter = spinnerLocalidades.getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }

        List<String> labels = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            String label = String.valueOf(adapter.getItem(i));
            if (!label.trim().isEmpty()) {
                labels.add(label);
                indices.add(i);
            }
        }

        int selected = spinnerLocalidades.getSelectedItemPosition();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search_list, null);
        EditText search = dialogView.findViewById(R.id.dialogSearch);
        ListView list = dialogView.findViewById(R.id.dialogList);

        List<String> filteredLabels = new ArrayList<>(labels);
        List<Integer> filteredIndices = new ArrayList<>(indices);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, filteredLabels);
        list.setAdapter(listAdapter);

        int checked = filteredIndices.indexOf(selected);
        if (checked >= 0) {
            list.setItemChecked(checked, true);
            list.setSelection(checked);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.select_locality)
                .setView(dialogView)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        list.setOnItemClickListener((parent, view, position, id) -> {
            int original = filteredIndices.get(position);
            spinnerLocalidades.setSelection(original);
            dialog.dismiss();
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = normalizeForSearch(s.toString());
                filteredLabels.clear();
                filteredIndices.clear();
                for (int i = 0; i < labels.size(); i++) {
                    if (query.isEmpty() || normalizeForSearch(labels.get(i)).contains(query)) {
                        filteredLabels.add(labels.get(i));
                        filteredIndices.add(indices.get(i));
                    }
                }
                listAdapter.notifyDataSetChanged();
                list.clearChoices();
                int newChecked = filteredIndices.indexOf(selected);
                if (newChecked >= 0) {
                    list.setItemChecked(newChecked, true);
                    list.setSelection(newChecked);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.show();
    }

    private static String normalizeForSearch(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase(Locale.getDefault());
    }

    private void loadInitialWeather(boolean isRecreate) {
        boolean online = isOnline(getApplicationContext());
        PrediccionResponse cached = getCachedResponseForCurrentZone();

        if (isRecreate) {
            if (cached != null && cached.predConcello != null) {
                showCachedData(cached);
                if (!online) {
                    showCachedDataSnackbar();
                }
            } else if (online) {
                setLoadingState(getSelectedLocalityName());
                new DownloadJSON(idZona).execute(URL2 + idZona);
            } else {
                handleOfflineState();
            }
        } else if (online) {
            setLoadingState(getSelectedLocalityName());
            new DownloadJSON(idZona).execute(URL2 + idZona);
        } else if (cached != null && cached.predConcello != null) {
            showCachedData(cached);
            showCachedDataSnackbar();
        } else {
            handleOfflineState();
        }
    }

    private void setLoadingState(String localityName) {
        TextView tvCity = findViewById(R.id.cityText);
        if (tvCity != null) {
            tvCity.setText(localityName != null && !localityName.isEmpty()
                    ? localityName.toUpperCase(Locale.getDefault())
                    : getString(R.string.station_header));
        }
        TextView tvDate = findViewById(R.id.txtActualizacion);
        if (tvDate != null) {
            tvDate.setText(R.string.loading_prediction);
        }

        TextView tvMax = findViewById(R.id.tempTextMax);
        if (tvMax != null) {
            tvMax.setText("--\u00A0°C");
            tvMax.setContentDescription(getString(R.string.temp_max_unknown));
        }
        TextView tvMin = findViewById(R.id.tempTextMin);
        if (tvMin != null) {
            tvMin.setText("--\u00A0°C");
            tvMin.setContentDescription(getString(R.string.temp_min_unknown));
        }
        TextView tvRainM = findViewById(R.id.textML);
        if (tvRainM != null) {
            tvRainM.setText("--%");
            tvRainM.setContentDescription(getString(R.string.rain_desc, "--%"));
        }
        TextView tvRainT = findViewById(R.id.textTL);
        if (tvRainT != null) {
            tvRainT.setText("--%");
            tvRainT.setContentDescription(getString(R.string.rain_desc, "--%"));
        }
        TextView tvRainN = findViewById(R.id.textNL);
        if (tvRainN != null) {
            tvRainN.setText("--%");
            tvRainN.setContentDescription(getString(R.string.rain_desc, "--%"));
        }

        pDialog.setVisibility(View.VISIBLE);
        View container = findViewById(R.id.weatherDataContainer);
        if (container != null) {
            container.setVisibility(View.GONE);
        }
    }

    private void setErrorState() {
        TextView tvDate = findViewById(R.id.txtActualizacion);
        if (tvDate != null) {
            tvDate.setText(R.string.error_download_prediction);
        }
        pDialog.setVisibility(View.GONE);
        View container = findViewById(R.id.weatherDataContainer);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        }
    }

    private PrediccionResponse getCachedResponseForCurrentZone() {
        String json = sharedPreferences.getString("last_weather_data", null);
        String cachedZone = sharedPreferences.getString(PREF_LAST_WEATHER_ZONE, null);
        if (json == null || cachedZone == null || idZona == null || !cachedZone.equals(idZona)) {
            return null;
        }
        try {
            return new Gson().fromJson(json, PrediccionResponse.class);
        } catch (Exception e) {
            Log.e("MainActivity", "Error reading cached weather", e);
            return null;
        }
    }

    private void showCachedData(PrediccionResponse cached) {
        if (cached == null || cached.predConcello == null) {
            return;
        }
        currentPredConcello = cached.predConcello;
        updateUIForDay(currentTabPosition);
        pDialog.setVisibility(View.GONE);
        View container = findViewById(R.id.weatherDataContainer);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        }
    }

    private void showCachedDataSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), R.string.cached_data_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.dialog_retry, v -> reloadWeather())
                .show();
    }

    private void handleOfflineState() {
        PrediccionResponse cached = getCachedResponseForCurrentZone();
        if (cached != null && cached.predConcello != null) {
            showCachedData(cached);
            showCachedDataSnackbar();
        } else {
            currentPredConcello = null;
            setErrorState();
            showOfflineDialog();
        }
    }

    private void handleDownloadFailure() {
        PrediccionResponse cached = getCachedResponseForCurrentZone();
        if (cached != null && cached.predConcello != null) {
            showCachedData(cached);
            showCachedDataSnackbar();
        } else {
            currentPredConcello = null;
            setErrorState();
            showOfflineDialog();
        }
    }

    private void showOfflineDialog() {
        if (errorDialogShowing) {
            return;
        }
        errorDialogShowing = true;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_error_title)
                .setMessage(R.string.error_update_message)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_retry, (d, which) -> {
                    errorDialogShowing = false;
                    reloadWeather();
                })
                .setNeutralButton(R.string.dialog_change_location, (d, which) -> {
                    errorDialogShowing = false;
                    showProvinceDialog();
                })
                .setNegativeButton(R.string.dialog_close, (d, which) -> errorDialogShowing = false)
                .create();
        dialog.setOnDismissListener(d -> errorDialogShowing = false);
        dialog.show();
    }

    private class DownloadJSON extends AsyncTask<String, Void, PrediccionResponse> {

        private final String zoneId;

        DownloadJSON(String zoneId) {
            this.zoneId = zoneId;
        }

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
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
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
            MainActivity.this.pDialog.setVisibility(View.GONE);
            View container = MainActivity.this.findViewById(R.id.weatherDataContainer);
            if (container != null) {
                container.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(PrediccionResponse result) {
            if (!zoneId.equals(idZona)) {
                return;
            }
            if (result != null && result.predConcello != null) {
                currentPredConcello = result.predConcello;
                updateUIForDay(currentTabPosition);
                pDialog.setVisibility(View.GONE);
                View container = findViewById(R.id.weatherDataContainer);
                if (container != null) {
                    container.setVisibility(View.VISIBLE);
                }
                try {
                    String jsonString = new Gson().toJson(result);
                    sharedPreferences.edit()
                            .putString("last_weather_data", jsonString)
                            .putString(PREF_LAST_WEATHER_ZONE, zoneId)
                            .apply();
                    updateWidget();
                } catch (Exception e) {
                    Log.e("MainActivity", "Error saving weather cache for widget", e);
                }
            } else {
                handleDownloadFailure();
            }
        }
    }

    private void reloadWeather() {
        if (idZona == null || idZona.isEmpty()) {
            return;
        }
        if (!isOnline(getApplicationContext())) {
            handleOfflineState();
            return;
        }
        setLoadingState(getSelectedLocalityName());
        new DownloadJSON(idZona).execute(URL2 + idZona);
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
            String maxText = dia.tMax != null ? dia.tMax + "\u00A0°C" : "--\u00A0°C";
            tvTempMax.setText(maxText);
            tvTempMax.setContentDescription(dia.tMax != null
                    ? getString(R.string.temp_max_desc, dia.tMax)
                    : getString(R.string.temp_max_unknown));
        }

        TextView tvTempMin = findViewById(R.id.tempTextMin);
        if (tvTempMin != null) {
            String minText = dia.tMin != null ? dia.tMin + "\u00A0°C" : "--\u00A0°C";
            tvTempMin.setText(minText);
            tvTempMin.setContentDescription(dia.tMin != null
                    ? getString(R.string.temp_min_desc, dia.tMin)
                    : getString(R.string.temp_min_unknown));
        }

        // Mañana: Cielo, Viento, Lluvia
        ImageView imgSkyM = findViewById(R.id.imgMC);
        if (imgSkyM != null) {
            if (dia.ceo != null && dia.ceo.manha != null) {
                imgSkyM.setContentDescription(WeatherDescriptions.getSkyDescription(this, dia.ceo.manha));
                new cargarImagenCieloM().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + dia.ceo.manha + ".png");
            } else {
                imgSkyM.setContentDescription(getString(R.string.sky_unknown));
            }
        }
        ImageView imgWindM = findViewById(R.id.imgMV);
        if (imgWindM != null) {
            if (dia.vento != null && dia.vento.manha != null) {
                imgWindM.setContentDescription(WeatherDescriptions.getWindDescription(this, dia.vento.manha));
                new cargarImagenVientoM().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/" + dia.vento.manha + ".png");
            } else {
                imgWindM.setContentDescription(getString(R.string.wind_unknown));
            }
        }
        TextView tvRainM = findViewById(R.id.textML);
        if (tvRainM != null) {
            String rainM = dia.pchoiva != null && dia.pchoiva.manha != null ? dia.pchoiva.manha + "%" : "--%";
            tvRainM.setText(rainM);
            tvRainM.setContentDescription(getString(R.string.rain_desc, rainM));
        }

        // Tarde: Cielo, Viento, Lluvia
        ImageView imgSkyT = findViewById(R.id.imgTC);
        if (imgSkyT != null) {
            if (dia.ceo != null && dia.ceo.tarde != null) {
                imgSkyT.setContentDescription(WeatherDescriptions.getSkyDescription(this, dia.ceo.tarde));
                new cargarImagenCieloT().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + dia.ceo.tarde + ".png");
            } else {
                imgSkyT.setContentDescription(getString(R.string.sky_unknown));
            }
        }
        ImageView imgWindT = findViewById(R.id.imgTV);
        if (imgWindT != null) {
            if (dia.vento != null && dia.vento.tarde != null) {
                imgWindT.setContentDescription(WeatherDescriptions.getWindDescription(this, dia.vento.tarde));
                new cargarImagenVientoT().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/" + dia.vento.tarde + ".png");
            } else {
                imgWindT.setContentDescription(getString(R.string.wind_unknown));
            }
        }
        TextView tvRainT = findViewById(R.id.textTL);
        if (tvRainT != null) {
            String rainT = dia.pchoiva != null && dia.pchoiva.tarde != null ? dia.pchoiva.tarde + "%" : "--%";
            tvRainT.setText(rainT);
            tvRainT.setContentDescription(getString(R.string.rain_desc, rainT));
        }

        // Noche: Cielo, Viento, Lluvia
        ImageView imgSkyN = findViewById(R.id.imgNC);
        if (imgSkyN != null) {
            if (dia.ceo != null && dia.ceo.noite != null) {
                imgSkyN.setContentDescription(WeatherDescriptions.getSkyDescription(this, dia.ceo.noite));
                new cargarImagenCieloN().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + dia.ceo.noite + ".png");
            } else {
                imgSkyN.setContentDescription(getString(R.string.sky_unknown));
            }
        }
        ImageView imgWindN = findViewById(R.id.imgNV);
        if (imgWindN != null) {
            if (dia.vento != null && dia.vento.noite != null) {
                imgWindN.setContentDescription(WeatherDescriptions.getWindDescription(this, dia.vento.noite));
                new cargarImagenVientoN().execute("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/" + dia.vento.noite + ".png");
            } else {
                imgWindN.setContentDescription(getString(R.string.wind_unknown));
            }
        }
        TextView tvRainN = findViewById(R.id.textNL);
        if (tvRainN != null) {
            String rainN = dia.pchoiva != null && dia.pchoiva.noite != null ? dia.pchoiva.noite + "%" : "--%";
            tvRainN.setText(rainN);
            tvRainN.setContentDescription(getString(R.string.rain_desc, rainN));
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
            tvActualizacion.setText(getString(R.string.prediction_format, formattedDate));
        }
    }

    private void toggleTheme() {
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        boolean newMode = !isDarkMode;
        sharedPreferences.edit().putBoolean("dark_mode", newMode).apply();

        int targetMode = newMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(targetMode);
        updateWidget();
        recreate();
    }

    private String getSavedLanguage() {
        String lang = sharedPreferences.getString("app_language", null);
        if (lang == null || "system".equals(lang)) {
            try {
                Locale current = getResources().getConfiguration().getLocales().get(0);
                if (current != null && current.getLanguage().toLowerCase().startsWith("gl")) {
                    return "gl";
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error getting system locale", e);
            }
            return "es";
        }
        return lang;
    }

    private void toggleLanguage() {
        String currentLang = getSavedLanguage();
        String newLang = "gl".equalsIgnoreCase(currentLang) ? "es" : "gl";
        setAppLocale(newLang);
    }

    private void setAppLocale(String langCode) {
        sharedPreferences.edit().putString("app_language", langCode).apply();
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
        httpURLConnection.setConnectTimeout(8000);
        httpURLConnection.setReadTimeout(8000);
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
            pDialog.setVisibility(View.GONE);
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
            pDialog.setVisibility(View.GONE);
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView = findViewById(R.id.imgTV);
            imgView.setImageBitmap(bitmap);
            pDialog.setVisibility(View.GONE);
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
        
        // Configurar Toggle de Tema (Sol / Luna)
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (themeItem != null) {
            int themeTitle = isDarkMode ? R.string.switch_to_light : R.string.switch_to_dark;
            themeItem.setTitle(themeTitle);
            themeItem.setIcon(isDarkMode ? R.drawable.ic_sun : R.drawable.ic_moon);
            MenuItemCompat.setContentDescription(themeItem, getString(themeTitle));
        }

        // Configurar Toggle de Idioma (ES / GL)
        MenuItem langItem = menu.findItem(R.id.action_language);
        if (langItem != null) {
            View actionView = langItem.getActionView();
            String currentLang = getSavedLanguage();
            String currentName = "gl".equalsIgnoreCase(currentLang)
                    ? getString(R.string.language_galician)
                    : getString(R.string.language_spanish);
            String targetName = "gl".equalsIgnoreCase(currentLang)
                    ? getString(R.string.language_spanish)
                    : getString(R.string.language_galician);
            String languageDescription = getString(R.string.language_current, currentName, targetName);
            if (actionView != null) {
                TextView tvLang = actionView.findViewById(R.id.tvLanguageToggle);
                if (tvLang != null) {
                    tvLang.setText("gl".equalsIgnoreCase(currentLang) ? "GL" : "ES");
                    tvLang.setContentDescription(languageDescription);
                }
                actionView.setContentDescription(languageDescription);
                actionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleLanguage();
                    }
                });
            }
            MenuItemCompat.setContentDescription(langItem, languageDescription);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            toggleTheme();
            return true;
        } else if (item.getItemId() == R.id.action_language) {
            toggleLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
