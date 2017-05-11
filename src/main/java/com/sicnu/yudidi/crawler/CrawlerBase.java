package com.sicnu.yudidi.crawler;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class CrawlerBase {

	public static void trustEveryone() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			// 获取一个SSLContext实例  
			SSLContext context = SSLContext.getInstance("TLS");
			 // 初始化SSLContext实例  
			context.init(null, new X509TrustManager[]{new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			}}, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
		}
	}
	
	protected static  void sleep() {
		try {
			Thread.sleep(CrawlerConfig.CRAWLER_INTERVAL);
		} catch (InterruptedException e) {
			//因为抛出InterruptedException导致标志位复位为false，所以要重新设置为true,使得接下来的代码能够检测到true.
			//Restore the interrupted status to false
			Thread.currentThread().interrupt();
		}
	}
}
