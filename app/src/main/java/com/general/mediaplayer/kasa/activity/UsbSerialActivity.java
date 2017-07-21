package com.general.mediaplayer.kasa.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UsbSerialActivity extends BaseActivity {

    private final String TAG = UsbSerialActivity.class.getSimpleName();
    private static final String ACTION_USB_PERMISSION = "com.examples.accessory.controller.action.USB_PERMISSION";

    UsbSerialPort sPort;
    UsbDeviceConnection connection;
    private boolean isAsked = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
//        filter.addAction(ACTION_USB_PERMISSION);
//        this.registerReceiver(mUsbReceiver, filter);

        ProbeTable customTable = new ProbeTable();
        customTable.addProduct(0x2a03, 0x0043, CdcAcmSerialDriver.class);
        UsbSerialProber prober = new UsbSerialProber(customTable);
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = prober.findAllDrivers(manager);
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

        refreshDevices();

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

                if (!isAsked &&  connection == null)
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

    private void refreshDevices()
    {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // Get the list of attached devices
        HashMap<String, UsbDevice> devices = manager.getDeviceList();

        String str = "";
        str =  "Number of devices: " + devices.size() + "\n";

        // Iterate over all devices
        Iterator<String> it = devices.keySet().iterator();
        while (it.hasNext())
        {
            String deviceName = it.next();
            UsbDevice device = devices.get(deviceName);

            String VID = Integer.toHexString(device.getVendorId()).toUpperCase();
            String PID = Integer.toHexString(device.getProductId()).toUpperCase();
            str += deviceName + " " +  VID + ":" + PID + " " + manager.hasPermission(device) + "\n";
        }

        Toast.makeText(this ,str ,Toast.LENGTH_LONG).show();
    }

}
