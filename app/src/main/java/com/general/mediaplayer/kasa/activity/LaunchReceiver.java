package com.general.mediaplayer.kasa.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.os.ServiceManager;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mac on 21/07/2017.
 */

public class LaunchReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent )
    {
        String action = intent.getAction();
        if( action != null && action.equals( Intent.ACTION_BOOT_COMPLETED ) )
        {
            try
            {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo("com.general.mediaplayer.kasa", 0 );
                if( ai != null )
                {
                    UsbManager manager = (UsbManager) context.getSystemService( Context.USB_SERVICE );
                    IBinder b = ServiceManager.getService( Context.USB_SERVICE );
                    IUsbManager service = IUsbManager.Stub.asInterface( b );

                    HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
                    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                    while( deviceIterator.hasNext() )
                    {
                        UsbDevice device = deviceIterator.next();
                        if( device.getVendorId() == 0x2a03 )
                        {
                            service.grantDevicePermission( device, ai.uid );
                            service.setDevicePackage( device, "com.general.mediaplayer.kasa" , ai.uid);
                        }
                    }
                }
            }
            catch( Exception e )
            {
            }
        }
    }
}
