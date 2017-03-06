package com.suntown.suntownshop.ibeacon;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * 代码改自https://github.com/RadiusNetworks/android-ibeacon-service/blob/master/src/main/java/com/radiusnetworks/ibeacon/IBeacon.java
 * @author gvzhang
 *
 */
public class IBeaconClass {

    static public  class IBeacon{
    	public String name;
    	public int major;
    	public int minor;
    	public String proximityUuid;
    	public String bluetoothAddress;
    	public int txPower;
    	public int rssi;
    }
    public static IBeacon fromScanData(BluetoothDevice device, int rssi,byte[] scanData) {
    	int startByte = 2;
		boolean patternFound = false;
		while (startByte <= 5) {
			if (((int)scanData[startByte+2] & 0xff) == 0x02 &&
				((int)scanData[startByte+3] & 0xff) == 0x15) {			
				// yes!  This is an IBeacon	
				patternFound = true;
				break;
			}
			else if (((int)scanData[startByte] & 0xff) == 0x2d &&
					((int)scanData[startByte+1] & 0xff) == 0x24 &&
					((int)scanData[startByte+2] & 0xff) == 0xbf &&
					((int)scanData[startByte+3] & 0xff) == 0x16) {
                IBeacon IBeacon = new IBeacon();
				IBeacon.major = 0;
				IBeacon.minor = 0;
				IBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
				IBeacon.txPower = -55;
				return IBeacon;
			}
            else if (((int)scanData[startByte] & 0xff) == 0xad &&
                     ((int)scanData[startByte+1] & 0xff) == 0x77 &&
                     ((int)scanData[startByte+2] & 0xff) == 0x00 &&
                     ((int)scanData[startByte+3] & 0xff) == 0xc6) {
                    IBeacon IBeacon = new IBeacon();
                    IBeacon.major = 0;
                    IBeacon.minor = 0;
                    IBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
                    IBeacon.txPower = -55;
                    return IBeacon;
            }
			startByte++;
		}


		if (patternFound == false) {
			// This is not an IBeacon
			return null;
		}

		IBeacon IBeacon = new IBeacon();

		IBeacon.major = (scanData[startByte+20] & 0xff) * 0x100 + (scanData[startByte+21] & 0xff);
		IBeacon.minor = (scanData[startByte+22] & 0xff) * 0x100 + (scanData[startByte+23] & 0xff);
		IBeacon.txPower = (int)scanData[startByte+24]; // this one is signed
		IBeacon.rssi = rssi;

		// AirLocate:
		// 02 01 1a 1a ff 4c 00 02 15  # Apple's fixed IBeacon advertising prefix
		// e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 # IBeacon profile uuid
		// 00 00 # major 
		// 00 00 # minor 
		// c5 # The 2's complement of the calibrated Tx Power

		// Estimote:		
		// 02 01 1a 11 07 2d 24 bf 16 
		// 394b31ba3f486415ab376e5c0f09457374696d6f7465426561636f6e00000000000000000000000000000000000000000000000000
		byte[] proximityUuidBytes = new byte[16];
		System.arraycopy(scanData, startByte+4, proximityUuidBytes, 0, 16); 
		String hexString = bytesToHexString(proximityUuidBytes);
		StringBuilder sb = new StringBuilder();
		sb.append(hexString.substring(0,8));
		sb.append("-");
		sb.append(hexString.substring(8,12));
		sb.append("-");
		sb.append(hexString.substring(12,16));
		sb.append("-");
		sb.append(hexString.substring(16,20));
		sb.append("-");
		sb.append(hexString.substring(20,32));
		IBeacon.proximityUuid = sb.toString();
		Log.i("fc", "IBeaconClass信息："+sb.toString());

        if (device != null) {
            IBeacon.bluetoothAddress = device.getAddress();
            IBeacon.name = device.getName();
        }
        
		return IBeacon;
	}

    private static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
    
    
}
