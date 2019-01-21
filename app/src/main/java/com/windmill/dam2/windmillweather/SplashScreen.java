package com.windmill.dam2.windmillweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Debug;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LogPrinter;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends Activity {
 GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        gifImageView= findViewById(R.id.splashGIF);
//Metemos el GiF en el SplashScreen
        try{
            InputStream inputStream= getAssets().open("splash2.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();

        }catch (IOException e){
            System.out.print("Ha petado el GIF");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
