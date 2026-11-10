package com.windmill.dam2.windmillweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.windmill.dam2.windmillweather.MainActivity.PrediccionResponse;
import com.windmill.dam2.windmillweather.MainActivity.DiaConcello;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WeatherWidgetProvider";
    private static final String BASE_URL = "https://servizos.meteogalicia.gal/mgrss/predicion/jsonPredConcellos.action?idConc=";
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, android.os.Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    private static Context getLocalizedContext(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
            String lang = prefs.getString("app_language", "system");
            if (!"system".equals(lang)) {
                Locale locale = new Locale(lang);
                android.content.res.Configuration config = new android.content.res.Configuration(context.getResources().getConfiguration());
                config.setLocale(locale);
                return context.createConfigurationContext(config);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting localized context", e);
        }
        return context;
    }

    private static String getLocalizedText(Context context, int stringId) {
        return getLocalizedContext(context).getString(stringId);
    }

    private static String getLocalizedText(Context context, int stringId, Object... args) {
        return getLocalizedContext(context).getString(stringId, args);
    }

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        // 1. Determine size and choose layout
        android.os.Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

        final int layoutId = (minHeight < 100) ? R.layout.widget_weather_small : R.layout.widget_weather;
        final RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

        applyWidgetTheme(context, views, layoutId);

        // PendingIntent to launch MainActivity on widget click
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);

        // Set some default text
        if (layoutId == R.layout.widget_weather) {
            views.setTextViewText(R.id.widget_status_text, getLocalizedText(context, R.string.widget_updating));
        }
        views.setTextViewText(R.id.widget_label_today, getLocalizedText(context, R.string.widget_today));
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // 2. Offload data loading and image processing to background thread
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    String idZona = prefs.getString("idZona", null);
                    String weatherJson = prefs.getString("last_weather_data", null);

                    // Fetch fresh data if we have an idZona
                    if (idZona != null && !idZona.isEmpty()) {
                        String freshJson = downloadJson(BASE_URL + idZona);
                        if (freshJson != null && !freshJson.isEmpty()) {
                            weatherJson = freshJson;
                            prefs.edit().putString("last_weather_data", weatherJson).apply();
                        }
                    }

                    views.setTextViewText(R.id.widget_label_today, getLocalizedText(context, R.string.widget_today));

                    if (weatherJson != null && !weatherJson.isEmpty()) {
                        PrediccionResponse response = null;
                        try {
                            response = new Gson().fromJson(weatherJson, PrediccionResponse.class);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing JSON", e);
                        }

                        if (response != null && response.predConcello != null && response.predConcello.listaPredDiaConcello != null && !response.predConcello.listaPredDiaConcello.isEmpty()) {
                            MainActivity.PredConcello concello = response.predConcello;
                            DiaConcello today = concello.listaPredDiaConcello.get(0);

                            if (concello.nome != null) {
                                views.setTextViewText(R.id.widget_city_name, concello.nome.toUpperCase());
                            }

                            String maxTempStr = today.tMax != null ? today.tMax + "\u00A0°C" : "--\u00A0°C";
                            String minTempStr = today.tMin != null ? today.tMin + "\u00A0°C" : "--\u00A0°C";
                            String tempRange = maxTempStr + " / " + minTempStr;
                            views.setTextViewText(R.id.widget_temp_range, tempRange);
                            views.setContentDescription(R.id.widget_temp_range,
                                    getLocalizedText(context, R.string.widget_temp_desc, maxTempStr, minTempStr));

                            if (layoutId == R.layout.widget_weather) {
                                views.setTextViewText(R.id.widget_status_text, getLocalizedText(context, R.string.widget_forecast_today));
                            }

                            if (layoutId == R.layout.widget_weather) {
                                String rainM = today.pchoiva != null && today.pchoiva.manha != null ? today.pchoiva.manha + "%" : "--%";
                                String rainT = today.pchoiva != null && today.pchoiva.tarde != null ? today.pchoiva.tarde + "%" : "--%";
                                String rainN = today.pchoiva != null && today.pchoiva.noite != null ? today.pchoiva.noite + "%" : "--%";
                                views.setTextViewText(R.id.widget_rain_m, rainM);
                                views.setContentDescription(R.id.widget_rain_m, getLocalizedText(context, R.string.rain_desc, rainM));
                                views.setTextViewText(R.id.widget_rain_t, rainT);
                                views.setContentDescription(R.id.widget_rain_t, getLocalizedText(context, R.string.rain_desc, rainT));
                                views.setTextViewText(R.id.widget_rain_n, rainN);
                                views.setContentDescription(R.id.widget_rain_n, getLocalizedText(context, R.string.rain_desc, rainN));

                                if (today.dataPredicion != null) {
                                    String rawDate = today.dataPredicion.length() >= 10 ? today.dataPredicion.substring(0, 10) : today.dataPredicion;
                                    String formattedDate = rawDate;
                                    try {
                                        String[] parts = rawDate.split("-");
                                        if (parts.length == 3) {
                                            formattedDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error formatting date", e);
                                    }
                                    views.setTextViewText(R.id.widget_update_time, formattedDate);
                                }
                            }

                            // Publish text before downloading icons so the widget updates quickly.
                            appWidgetManager.updateAppWidget(appWidgetId, views);

                            Integer mainCeoId = today.ceoDia;
                            if (mainCeoId == null && today.ceo != null) {
                                mainCeoId = today.ceo.tarde != null ? today.ceo.tarde : today.ceo.manha;
                            }
                            if (mainCeoId != null) {
                                views.setContentDescription(R.id.widget_weather_icon,
                                        WeatherDescriptions.getSkyDescription(getLocalizedContext(context), mainCeoId));
                                Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + mainCeoId + ".png");
                                if (bitmap != null) {
                                    views.setImageViewBitmap(R.id.widget_weather_icon, bitmap);
                                }
                            }

                            if (layoutId == R.layout.widget_weather) {
                                if (today.ceo != null && today.ceo.manha != null) {
                                    views.setContentDescription(R.id.widget_sky_m,
                                            WeatherDescriptions.getSkyDescription(getLocalizedContext(context), today.ceo.manha));
                                    Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + today.ceo.manha + ".png");
                                    if (bitmap != null) {
                                        views.setImageViewBitmap(R.id.widget_sky_m, bitmap);
                                    }
                                }
                                if (today.ceo != null && today.ceo.tarde != null) {
                                    views.setContentDescription(R.id.widget_sky_t,
                                            WeatherDescriptions.getSkyDescription(getLocalizedContext(context), today.ceo.tarde));
                                    Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + today.ceo.tarde + ".png");
                                    if (bitmap != null) {
                                        views.setImageViewBitmap(R.id.widget_sky_t, bitmap);
                                    }
                                }
                                if (today.ceo != null && today.ceo.noite != null) {
                                    views.setContentDescription(R.id.widget_sky_n,
                                            WeatherDescriptions.getSkyDescription(getLocalizedContext(context), today.ceo.noite));
                                    Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + today.ceo.noite + ".png");
                                    if (bitmap != null) {
                                        views.setImageViewBitmap(R.id.widget_sky_n, bitmap);
                                    }
                                }
                            }
                        }
                    } else {
                        if (layoutId == R.layout.widget_weather) {
                            views.setTextViewText(R.id.widget_status_text, getLocalizedText(context, R.string.widget_open_app));
                        }
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating app widget " + appWidgetId, e);
                }
            }
        });
    }

    private static void applyWidgetTheme(Context context, RemoteViews views, int layoutId) {
        SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);

        int primary = ContextCompat.getColor(context,
                isDark ? R.color.widget_text_primary_dark : R.color.widget_text_primary_light);
        int secondary = ContextCompat.getColor(context,
                isDark ? R.color.widget_text_secondary_dark : R.color.widget_text_secondary_light);
        int accent = ContextCompat.getColor(context,
                isDark ? R.color.widget_text_accent_dark : R.color.widget_text_accent_light);
        int outline = ContextCompat.getColor(context,
                isDark ? R.color.widget_outline_dark : R.color.widget_outline_light);

        views.setInt(R.id.widget_root, "setBackgroundResource",
                isDark ? R.drawable.widget_background_dark : R.drawable.widget_background_light);
        views.setTextColor(R.id.widget_city_name, primary);
        views.setTextColor(R.id.widget_temp_range, primary);
        views.setTextColor(R.id.widget_label_today, accent);

        if (layoutId == R.layout.widget_weather) {
            views.setTextColor(R.id.widget_status_text, secondary);
            views.setTextColor(R.id.widget_rain_m, secondary);
            views.setTextColor(R.id.widget_rain_t, secondary);
            views.setTextColor(R.id.widget_rain_n, secondary);
            views.setTextColor(R.id.widget_period_m_label, secondary);
            views.setTextColor(R.id.widget_period_t_label, secondary);
            views.setTextColor(R.id.widget_period_n_label, secondary);
            views.setTextColor(R.id.widget_update_time, secondary);
            views.setInt(R.id.widget_separator, "setBackgroundColor", outline);
        }
    }

    private static String downloadJson(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                is.close();
                return sb.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error downloading JSON: " + urlString, e);
        }
        return null;
    }

    private static Bitmap downloadImage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error downloading image: " + urlString, e);
        }
        return null;
    }
}
