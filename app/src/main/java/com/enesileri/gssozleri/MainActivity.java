package com.enesileri.gssozleri;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    SharedPreferences index;
    Button btn_sonraki,btn_onceki,btn_paylas;
    TextView txt_söz;
    String [] sozler;
    int i;
    ClipboardManager clipboardManager;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btn_sonraki=(Button)findViewById(R.id.sonraki);
        btn_onceki=(Button)findViewById(R.id.önceki);
        btn_paylas=(Button)findViewById(R.id.paylas);
        txt_söz=(TextView)findViewById(R.id.sözler);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8449130834114994/9508566506"); // GEÇİŞ REKLAM KODU

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });

        reklamiyukle();

        index= PreferenceManager.getDefaultSharedPreferences(this);
        i=index.getInt("index",0);
        clipboardManager=(ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE);
        Resources rc=getResources();
        sozler=rc.getStringArray(R.array.sözler);
        txt_söz.setText(sozler[i]);

        btn_sonraki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if(i>=sozler.length){
                    txt_söz.setText("Sizinde bir sözünüz varsa 'enesileri34@gmail.com' adresine bekliyorum. !");
                    i=sozler.length;
                }else{
                    txt_söz.setText(sozler[i]);
                }
            }
        });

        btn_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reklamiyukle2();
                CharSequence mesaj = txt_söz.getText();
                mesajPaylas(mesaj);
            }
        });

        btn_onceki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                if(i<=-1){
                    txt_söz.setText("Bugün günlerden GALATASARAY !");
                    i=-1;
                }else{
                    txt_söz.setText(sozler[i]);
                }
            }
        });
    }
    @Override
    protected void onStop() {
        final SharedPreferences.Editor editor=index.edit();
        editor.putInt("index",i);
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onPause() {
        final SharedPreferences.Editor editor=index.edit();
        editor.putInt("index",i);
        editor.commit();
        super.onPause();
    }

    private void reklamiyukle(){
        adView=new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8449130834114994/7195719193");//BANNER REKLAM KODU

        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.reklam);
        linearLayout.addView(adView);

        AdRequest adRequest=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    private void reklamiyukle2(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void mesajPaylas(CharSequence mesaj){
        Intent paylasIntent = new Intent(Intent.ACTION_SEND);
        paylasIntent.setType("text/plain");
        paylasIntent.putExtra(Intent.EXTRA_TEXT,mesaj);
        startActivity(Intent.createChooser(paylasIntent,"Sözü nerede paylaşmak istediğinizi seçiniz..."));
    }
}
