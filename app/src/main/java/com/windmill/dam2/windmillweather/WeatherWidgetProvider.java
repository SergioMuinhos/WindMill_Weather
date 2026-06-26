package com.windmill.dam2.windmillweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.windmill.dam2.windmillweather.MainActivity.PrediccionResponse;
import com.windmill.dam2.windmillweather.MainActivity.DiaConcello;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WeatherWidgetProvider";
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    String weatherJson = prefs.getString("last_weather_data", null);
                    
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_weather);

                    // PendingIntent to launch MainActivity on widget click
                    Intent intent = new Intent(context, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            context, 
                            0, 
                            intent, 
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);

                    if (weatherJson != null) {
                        PrediccionResponse response = new Gson().fromJson(weatherJson, PrediccionResponse.class);
                        if (response != null && response.predConcello != null && response.predConcello.listaPredDiaConcello != null && !response.predConcello.listaPredDiaConcello.isEmpty()) {
                            MainActivity.PredConcello concello = response.predConcello;
                            DiaConcello today = concello.listaPredDiaConcello.get(0);
                            
                            views.setTextViewText(R.id.widget_city_name, concello.nome.toUpperCase());
                            
                            String maxTempStr = today.tMax != null ? today.tMax + "ºC" : "--ºC";
                            String minTempStr = today.tMin != null ? today.tMin + "ºC" : "--ºC";
                            views.setTextViewText(R.id.widget_temp_range, maxTempStr + " / " + minTempStr);
                            views.setTextViewText(R.id.widget_status_text, "Predicción para hoy");

                            // Download overall sky icon
                            Integer mainCeoId = today.ceoDia;
                            if (mainCeoId == null && today.ceo != null) {
                                mainCeoId = today.ceo.tarde != null ? today.ceo.tarde : today.ceo.manha;
                            }
                            if (mainCeoId != null) {
                                Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + mainCeoId + ".png");
                                if (bitmap != null) {
                                    views.setImageViewBitmap(R.id.widget_weather_icon, bitmap);
                                }
                            }

                            // Morning Period
                            if (today.ceo != null && today.ceo.manha != null) {
                                Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + today.ceo.manha + ".png");
                                if (bitmap != null) {
                                    views.setImageViewBitmap(R.id.widget_sky_m, bitmap);
                                }
                            }
                            views.setTextViewText(R.id.widget_rain_m, today.pchoiva != null && today.pchoiva.manha != null ? today.pchoiva.manha + "%" : "00%");

                            // Afternoon Period
                            if (today.ceo != null && today.ceo.tarde != null) {
                                Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + today.ceo.tarde + ".png");
                                if (bitmap != null) {
                                    views.setImageViewBitmap(R.id.widget_sky_t, bitmap);
                                }
                            }
                            views.setTextViewText(R.id.widget_rain_t, today.pchoiva != null && today.pchoiva.tarde != null ? today.pchoiva.tarde + "%" : "00%");

                            // Night Period
                            if (today.ceo != null && today.ceo.noite != null) {
                                Bitmap bitmap = downloadImage("https://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/" + today.ceo.noite + ".png");
                                if (bitmap != null) {
                                    views.setImageViewBitmap(R.id.widget_sky_n, bitmap);
                                }
                            }
                            views.setTextViewText(R.id.widget_rain_n, today.pchoiva != null && today.pchoiva.noite != null ? today.pchoiva.noite + "%" : "00%");
                        }
                    }
                    
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating app widget " + appWidgetId, e);
                }
            }
        });
    }

    private static Bitmap downloadImage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
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
