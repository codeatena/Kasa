package com.general.mediaplayer.kasa.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;

public class UsbSerialActivity extends BaseActivity {

    private final String TAG = UsbSerialActivity.class.getSimpleName();
    private static final String ACTION_USB_PERMISSION = "com.examples.accessory.controller.action.USB_PERMISSION";

    UsbSerialPort sPort;
    UsbDeviceConnection connection;

    private boolean isAsked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ProbeTable customTable = new ProbeTable();
//        customTable.addProduct(0x2a03, 0x0043, CdcAcmSerialDriver.class);
//        customTable.addProduct(0x2a03, 0x0043, Ch34xSerialDriver.class);
//        customTable.addProduct(0x2a03, 0x0043, Cp21xxSerialDriver.class);
//        customTable.addProduct(0x2a03, 0x0043, FtdiSerialDriver.class);
//        customTable.addProduct(0x2a03, 0x0043, ProlificSerialDriver.class);
//        UsbSerialProber prober = new UsbSerialProber(customTable);
//        List<UsbSerialDriver> availableDrivers = prober.findAllDrivers(manager);

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        sPort = driver.getPorts().get(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sPort != null) {

            final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            if (usbManager.hasPermission(sPort.getDriver().getDevice())){

                if (connection == null)
                {
                    connection = usbManager.openDevice(sPort.getDriver().getDevice());
                    openConnection(connection);
                }
            }
            else{

                if (!isAsked)
                {
                    isAsked = true;

                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    registerReceiver(mUsbReceiver, filter);
                    usbManager.requestPermission(sPort.getDriver().getDevice(), mPermissionIntent);
                }

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

//        if (sPort != null) {
//            try {
//                sPort.close();
//            } catch (IOException e) {
//                // Ignore.
//            }
//            sPort = null;
//        }
    }

    private void openConnection(UsbDeviceConnection connection)
    {
        try {
            sPort.open(connection);
            sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            sPort.setDTR(true);
            sPort.setRTS(true);

        } catch (IOException e) {
            Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
            try {
                sPort.close();
            } catch (IOException e2) {
                // Ignore.
            }
            sPort = null;
            return;
        }
    }

    public void sendCommand(String str) {

        if (sPort != null) {

            try {
                byte response[] = str.getBytes();
                sPort.write(response, 200);
            } catch (IOException e) {

                Log.e(TAG, "write error: " + e.getMessage());
            }
        }
    }

    public final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {

                    final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    if (connection == null)
                    {
                        connection = usbManager.openDevice(sPort.getDriver().getDevice());
                        openConnection(connection);
                    }

                }
            }

        }
    };

}
