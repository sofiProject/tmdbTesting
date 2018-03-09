package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

//Right now this class only returns the json response as a string.
//I could rename it to TMDBJsonToString to more accurately communicate its current
//functionality, but there is also the chance that in the future it could contain other
//related helper methods so I'll leave it with a more generic name.
public class TMDBHelper {

    public String getJsonAsString(String urlString, int timeout) throws IOException {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-length", "0");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setReadTimeout(timeout);
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
                case 401:
                    return  String.valueOf(status);
            }

        }
        catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new MalformedURLException();
        }
        catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }
}
