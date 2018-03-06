package com.mytool.m3u8;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by wanghaofei on 2017/12/28.
 */

public class M3u8ServerUtils {
    private static final String LOG_TAG = "com.cn.cntv.utils.M3u8Server";

    /**
     * 获取当前ipV4地址
     *
     * @return
     */
    public static String getLocalIpAddressIpv4() {
        try {
            String ipv4;
            List<NetworkInterface> nilist = Collections.list(NetworkInterface
                    .getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                List<InetAddress> ialist = Collections.list(ni
                        .getInetAddresses());
                for (InetAddress address : ialist) {
                    if (address.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ipv4 = address
                            .getHostAddress())) {
                        return ipv4;
                    }
                }

            }

        } catch (SocketException ex) {
        }
        return null;
    }

    /** 获取当前IPv6地址 */
    public static String getLocalIpAddressIpv6() {
        try {
            // 遍历网络接口
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                // 遍历IP地址
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    // 非回传地址时返回
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    //172.0.0.1
    public static String getLocalIpAddressW(Context context) {

        WifiManager wifimanager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = (WifiInfo) wifimanager.getConnectionInfo();
        try {
            int i = wifiinfo.getIpAddress();
            // 返回IP 地址
            return (i & 0xff) + "." + ((i >> 8) & 0xff) + "."
                    + ((i >> 16) & 0xff) + "." + ((i >> 24) & 0xff);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
