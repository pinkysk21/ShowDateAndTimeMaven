package com.develop.ShowDateAndTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
}