package pl.edu.agh.text.analyzer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Request {
    private String params;
    private URL api;

    public Request(String api) throws MalformedURLException {
        this.params = "";
        this.api = new URL(api);
    }

    public void addParameter(String name, String value) throws UnsupportedEncodingException {
        String chain = URLEncoder.encode(name, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
        String delimiter = params.length() > 0 ? "&" : "";
        this.params += delimiter + chain;
    }

    public HttpURLConnection setConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) this.api.openConnection();

        connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setUseCaches(false);

        return connection;
    }

    public String getResponse() throws IOException {
        this.addParameter("src", "sdk-java-1.1");
        HttpURLConnection connection = this.setConnection();

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(this.params);
        writer.flush();

        InputStreamReader input = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(input);
        String line = "";
        StringBuilder response = new StringBuilder(line);

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }
}
