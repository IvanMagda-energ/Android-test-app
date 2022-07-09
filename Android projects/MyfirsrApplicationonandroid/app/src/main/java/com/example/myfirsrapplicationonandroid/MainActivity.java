package com.example.myfirsrapplicationonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText city_name;
    private Button city_button;
    private TextView result_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Create links to our objects

        city_name = findViewById(R.id.city_name);
        city_button = findViewById(R.id.city_button);
        result_textView = findViewById(R.id.result_textView);

// Create task

        city_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city_name.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.Empty_fields_error, Toast.LENGTH_LONG).show();
                else {
                    String city = city_name.getText().toString();
                    String APIKey = "2c6f061132ddca047db33cf94a92ee20";
                    String URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APIKey + "&units=metric";

                    new GetURLData().execute(URL);


                }

            }
        });
    }
    private class GetURLData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            result_textView.setText("Wait please...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("/n");
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_textView.setText("Temperature" + jsonObject.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
