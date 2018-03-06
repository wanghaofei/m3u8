package com.mytool.m3u8;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WebService extends Service {

	public static int PORT = 7766;
	public static final String WEBROOT = "/";

	private WebServer webServer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("three",  "。。service..");
		while (isPortAvailable(PORT)) {
			PORT += 1;
			Log.e("端口号", PORT + "");
		}
		webServer = new WebServer(PORT, WEBROOT);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// webServer.setDaemon(true);

		Log.e("three",  "。。service.1.");


		try {
			if (!webServer.isAlive())
				webServer.start();
		} catch (Exception e) {

		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		webServer.close();
		super.onDestroy();
	}

	private void bindPort(String host, int port) throws Exception {
		Socket s = new Socket();
		s.bind(new InetSocketAddress(host, port));
		s.close();
	}

	/**
	 * true 是用了 false 是没有
	 *
	 * @return
	 * @throws IOException 
	 */
	public boolean isPortAvailable(int port)  {
		try {
			ServerSocket s = new ServerSocket();
		
			bindPort("127.0.0.1", port);
			return false;
		} catch (Exception e) {
			return  true;
		}
	}
}
