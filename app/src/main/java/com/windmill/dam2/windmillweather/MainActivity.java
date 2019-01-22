package com.windmill.dam2.windmillweather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerProvincias;
    private Spinner spinnerLocalidades;
    public String idZona = "";
    public int idProv = 0;
    TextView textview;
    ProgressBar pDialog;
    NodeList nodelist;
    ImageView imgView;
    TabItem hoy,manana,pasado;
    public String URL="http://servizos.meteogalicia.es/rss/predicion/rssLocalidades.action?idZona=36001&dia=1";
    public String URL2="http://servizos.meteogalicia.es/rss/predicion/rssLocalidades.action?idZona=";
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Creamos los dos Spinner de Provincias y Localidades
        spinnerProvincias =  findViewById(R.id.provincia);
        spinnerLocalidades =  findViewById(R.id.localidad);
       pDialog =findViewById(R.id.pBar);
        ArrayAdapter<String> adapterProv = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, provincias);

        spinnerProvincias.setAdapter(adapterProv);

        spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(MainActivity.this,"Has Seleccionado:"+position,Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> adapterLoc=null;

                switch (position) {
                    case 0:
                        idProv = 360;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, pontevedra);


                        break;
                    case 1:
                        idProv = 270;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, lugo);


                        break;
                    case 2:
                        idProv = 320;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ourense);

                        break;
                    case 3:
                        idProv = 150;
                        adapterLoc = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, coruna);
                        break;
                }

                spinnerLocalidades.setAdapter(adapterLoc );


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerLocalidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MainActivity.this, "locPosicion: " + position + " IdProv: " + idProv, Toast.LENGTH_SHORT).show();
                idZona=idProv+String.format("%02d",position+1);
                // Toast.makeText(MainActivity.this, "URL: " + URL+""+idZona, Toast.LENGTH_SHORT).show();
                String enlaces=URL2+idZona+"&dia=0";


                        try {
                            if (!isOnline(getApplicationContext())) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Conectese a una red de datos para poder utilizar la aplicacion.")
                                        .setTitle("Error")
                                        .setCancelable(false)
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
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
                                new DownloadXML().execute(enlaces);
                               // pDialog.hide();

                            }
                        } catch (Exception e) {
                            Log.e("Error en comprobar conexion", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        TabLayout pestanas=findViewById(R.id.tabs);


        pestanas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    // Toast.makeText(MainActivity.this,"Seleccionado: "+tab.getText(),Toast.LENGTH_SHORT).show();
                    if (tab.getText().equals("HOY")) {
                        Toast.makeText(MainActivity.this, "Seleccionado hoy", Toast.LENGTH_SHORT).show();
                        new DownloadXML().execute(URL2 + idZona + "&dia=0");
                    }
                    if (tab.getText().equals("MAÑANA")) {
                        Toast.makeText(MainActivity.this, "Seleccionado mañana", Toast.LENGTH_SHORT).show();
                        new DownloadXML().execute(URL2 + idZona + "&dia=1");
                    }
                    if (tab.getText().equals("PASADO")) {
                        Toast.makeText(MainActivity.this, "Seleccionado pasado", Toast.LENGTH_SHORT).show();
                        new DownloadXML().execute(URL2 + idZona + "&dia=2");
                    }
                }catch (Exception e){
                    Log.e("Error en OnTabSelected ",e.getLocalizedMessage());
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


    }


    private class DownloadXML extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... Url) {
            try{
                URL url=new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db =dbf.newDocumentBuilder();
                //Descargamos el XML
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                //Localizar el Nombre del TAG
                nodelist=doc.getElementsByTagName("item");

            }catch (Exception e){
                MainActivity.this.pDialog.setVisibility(View.INVISIBLE);
                Log.e("Error en el doInBackground",e.getMessage());
                e.printStackTrace();
            }  return null;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void args) {


            for(int i=0;i<nodelist.getLength();i++){
                Node nNode= nodelist.item(i);
                NodeList datos=nNode.getChildNodes();
                for (int j = 0; j < datos.getLength(); j++) {
                    Node dato=datos.item(j);
                    String etiq=dato.getNodeName();

                    //Nombre del Concello
                    if(etiq.equals("Concellos:nomeConcello")){
                        textview=  findViewById(R.id.cityText);
                        textview.setText(dato.getFirstChild().getNodeValue().toUpperCase());
                    }
                    //Temperatura Maxima
                    //TEXTO
                    if(etiq.equals("Concellos:tMax")){
                        textview= findViewById(R.id.tempTextMax);
                        textview.setText(dato.getFirstChild().getNodeValue()+"ºC");
                        //IMAGEN
                        new cargarImagenTempMax().execute("http://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/termometros/405.png");
                    }

                    //Temperatura Minima
                    //TEXTO
                    if(etiq.equals("Concellos:tMin")){
                        textview= findViewById(R.id.tempTextMin);
                        textview.setText(dato.getFirstChild().getNodeValue()+"ºC");
                        //IMAGEN
                        new cargarImagenTempMin().execute("http://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/termometros/400.png");
                    }

                    //MAÑANA
                    //Cielo

                    if(etiq.equals("Concellos:ceoM")){
                        String img=dato.getFirstChild().getNodeValue();
                        new cargarImagenCieloM().execute("http://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/"+img+".png");
                    }
                    //Viento
                    if(etiq.equals("Concellos:ventoM")){
                        String img=dato.getFirstChild().getNodeValue();
                        new cargarImagenVientoM().execute("http://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/combo/"+img+".png");
                    }

                    //Lluvia
                    if(etiq.equals("Concellos:pChoivaM")){
                        textview= findViewById(R.id.textML);
                        textview.setText(dato.getFirstChild().getNodeValue()+"%");
                    }

                    //TARDE
                    //Cielo
                    if(etiq.equals("Concellos:ceoT")){
                        String img=dato.getFirstChild().getNodeValue();
                        new cargarImagenCieloT().execute("http://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/"+img+".png");
                    }
                    //Viento
                    if(etiq.equals("Concellos:ventoT")){
                        String img=dato.getFirstChild().getNodeValue();
                        new cargarImagenVientoT().execute("http://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/combo/"+img+".png");
                    }

                    //Lluvia
                    if(etiq.equals("Concellos:pChoivaT")){
                        textview= findViewById(R.id.textTL);
                        textview.setText(dato.getFirstChild().getNodeValue()+"%");
                    }

                    //NOCHE
                    //Cielo
                    if(etiq.equals("Concellos:ceoN")){
                        String img=dato.getFirstChild().getNodeValue();
                        new cargarImagenCieloN().execute("http://www.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/ceo/"+img+".png");
                    }
                    //Viento
                    if(etiq.equals("Concellos:ventoN")){
                        String img=dato.getFirstChild().getNodeValue();
                        new cargarImagenVientoN().execute("http://servizos.meteogalicia.gal/datosred/infoweb/meteo/imagenes/meteoros/vento/combo/"+img+".png");
                    }

                    //Lluvia
                    if(etiq.equals("Concellos:pChoivaN")){
                        textview= findViewById(R.id.textNL);
                        textview.setText(dato.getFirstChild().getNodeValue()+"%");
                    }

                    //ULTIMA ACTUALIZACION

                    if(etiq.equals("Concellos:dataCreacion")){
                        textview= findViewById(R.id.txtActualizacion);
                        textview.setText("Última Actualización: "+dato.getFirstChild().getNodeValue().substring(0,10));
                    }

                }
            }

        }

        @Override
        protected void onCancelled(Void aVoid) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Alert!")
                    .setTitle("Conectese a una red de datos para poder utilizar la aplicacion. Gracias")
                    .create()
                    .show();
        }
    }

    private InputStream openHttpConnection(String url) throws IOException {
        InputStream is = null;
        int responseCode;

        URLConnection connection;
        connection = (new URL(url)).openConnection();

        if(!(connection instanceof HttpURLConnection)) {
            throw new IOException("Not HTTP connection");
        }

        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setAllowUserInteraction(false);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) {
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
        }
        catch (Exception ex) {
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
            // pDialog.dismiss();
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
            // pDialog.dismiss();
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

}

