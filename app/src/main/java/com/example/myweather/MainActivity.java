package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityTextView;
    TextView weatherTextView;
    TextView tempTextView;
    String apiRequest = "http://api.openweathermap.org/data/2.5/weather?q=";
    String apiKey = "&APPID=6dda82f9dc99c1c9af29e42308e26de1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.weatherTextView);
        tempTextView = findViewById(R.id.tempTextView);

    }


    public void sendRequest(View view) {

        cityTextView = findViewById(R.id.cityText);
        String city = cityTextView.getText().toString();

        apiRequest += city;
        apiRequest += apiKey;

        DownloadInfo task = new DownloadInfo();
        task.execute(apiRequest);

    }

    public class DownloadInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    System.out.println("Each char" + current);
                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String weatherString = "";
            String tempString = "";

            try {

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);
                for (int i=0; i<arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);
                    weatherString = jsonPart.getString("main") + ", " + jsonPart.getString("description");

                }

                //weatherTextView.setText(weatherString);

                String mainInfo = jsonObject.getString("main");
                JSONObject mainObj = new JSONObject(mainInfo);

                String temp = mainObj.getString("temp");
                String tempMin = mainObj.getString("temp_min");
                String tempMax = mainObj.getString("temp_max");

                double tempCel = convertToCel(Double.parseDouble(temp));

                double tempMinCel = convertToCel(Double.parseDouble(tempMin));
                double tempMaxCel = convertToCel(Double.parseDouble(tempMax));

                tempCel = tempRound(tempCel);
                tempMinCel = tempRound(tempMinCel);
                tempMaxCel = tempRound(tempMaxCel);

                tempString = "Temperature: " + Double.toString(tempCel) + "\nMinimum Temperature: " + Double.toString(tempMinCel) + "\nMaximum Temperature: " + Double.toString(tempMaxCel);
                weatherString += "\n"+ tempString;
                weatherTextView.setText(weatherString);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            apiRequest = "http://api.openweathermap.org/data/2.5/weather?q="; //So you could request again
        }
    }

    public double tempRound(double temp) {

        BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
        temp = bd.doubleValue();
        return temp;
    }

    public double convertToCel(double temp) {

        double ret = -273.15;
        ret += temp;
        return ret;

    }


}
