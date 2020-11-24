package id.ac.ui.cs.mobileprogramming.nathanael.helloworld;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import id.ac.ui.cs.mobileprogramming.nathanael.helloworld.receiver.WifiReceiver;

public class MainActivity extends AppCompatActivity {

    WifiReceiver receiverWifi;
    private ListView wifiList;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiList = findViewById(R.id.wifiList);
        FloatingActionButton buttonScan = findViewById(R.id.scanBtn);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWifi();
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        receiverWifi = new WifiReceiver(wifiManager, wifiList);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        registerReceiver(receiverWifi, intentFilter);

        getWifi();
    }

    private void getWifi() {
        Toast.makeText(MainActivity.this, "Scanning...", Toast.LENGTH_SHORT).show();

        wifiManager.startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverWifi);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
        if (requestCode == MY_PERMISSIONS_ACCESS_COARSE_LOCATION) {
            getWifi();
        }
    }

}