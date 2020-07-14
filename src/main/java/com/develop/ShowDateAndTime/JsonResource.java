package com.develop.ShowDateAndTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpHeaders;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class JsonResource {
	public static String apikey = "LRHG6PQ9KT10BWFE";

	public static String getJsonInfo(String stockvalue) {
		StringBuilder strBuf = new StringBuilder();
		try {
			URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stockvalue
					+ "&apikey=" + apikey);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// set the request method and properties.
			con.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			String output = null;
			while ((output = reader.readLine()) != null)
				strBuf.append(output);
			con.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
	
	public static String getIntraDailyJson(String stockvalue) throws IOException {


		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result="";

		try {

			HttpGet request = new HttpGet("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+stockvalue+"&interval=5min&apikey="+apikey);

			// add request headers


			CloseableHttpResponse response = httpClient.execute(request);

			try {
				/*

            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK
				 */

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// return it as a String
					result = EntityUtils.toString(entity);
					
				}

			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
			return result;
		}

	}

	
}