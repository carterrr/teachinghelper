package com.example.teachinghelper.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by 愿初心常在 on 2018/4/3.
 */

public class wifiUtil {
    public static String getLocalMac(Context context){
        try{
            NetworkInterface networkInterface=NetworkInterface.getByName("wlan0");
            byte[] macBytes=networkInterface.getHardwareAddress();
            if(macBytes == null){
                Toast.makeText(context,"获取Mac地址失败",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("getlocalmac", macBytes.toString());//显示[B@20609a乱码，需要转化 .一个字节（byte）＝8位（bit），“byte数组”里面全部是“byte”
                // 如果重启手机后，Wifi没有打开过，是无法获取其Mac地址的。获取到00:90:4C:7C:E4:84，然而实际是EC:9B:F3:37:28:1B（可以考虑授予CHANGE_WIFI_STATE权限，开关一次wifi刷一下。

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));//X 表示以十六进制形式输出
                    //02 表示不足两位，前面补0输出；如果超过两位，则实际输出
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);//res1不是空的，就删掉最后一个：
                }
                Log.e("getLocalMac: ",res1.toString());
                return res1.toString();

            }
        }catch(SocketException se){
            se.printStackTrace();
        }
        return null;
    }


    public static ArrayList<String> getConnectedMacs() {
        ArrayList<String> connectedMac =new ArrayList<String>();
        try{
            BufferedReader br=new BufferedReader(new FileReader("/proc/net/arp"));
            String line=br.readLine(); //舍弃第一行 IP address       HW type     Flags       HW address            Mask     Device信息
            while((line=br.readLine())!=null){
                String[] splitted=line.split("[\\s]+");//正则表达式表示一个或多个空格 []中只能写\\s 而不能\s
                if(splitted!=null){   //&&splitted.length<=6判断次数一旦太多会影响性能体验，干脆多读取一行
                    String mac=splitted[3];
                    connectedMac.add(mac);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return connectedMac;
    }
}


