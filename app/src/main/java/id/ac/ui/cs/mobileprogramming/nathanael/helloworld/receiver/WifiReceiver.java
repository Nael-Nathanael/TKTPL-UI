package id.ac.ui.cs.mobileprogramming.nathanael.helloworld.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WifiReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    ListView wifiDeviceList;
    public ArrayList<String> deviceList = new ArrayList<>();


    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }

    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {

            List<ScanResult> wifiList = wifiManager.getScanResults();
            deviceList = new ArrayList<>();

            for (ScanResult scanResult : wifiList) {
                deviceList.add(scanResult.SSID);
            }

            ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_list_item_1,
                    deviceList.toArray()
            );
            wifiDeviceList.setAdapter(arrayAdapter);

            Toast.makeText(context, "Scanning complete...", Toast.LENGTH_SHORT).show();
        }
    }
}