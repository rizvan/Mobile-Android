package org.scielo.search;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLTester {
	public boolean check(String strUrl){
		boolean ret = false;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();

            if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
            	ret = true;
            }
        } catch (IOException e) {
            System.err.println("Error creating HTTP connection");
            e.printStackTrace();
            
        }
        return ret;
    }
}
