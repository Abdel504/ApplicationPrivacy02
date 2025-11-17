package com.example.applicatie_privacy_fail_180;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI-elementen koppelen aan de variabelen
        TextView tv = findViewById(R.id.tvStatus);

        // Knop koppelen aan de HTTP testfunctie
        findViewById(R.id.btnTest).setOnClickListener(v -> testHttp(tv));
    }

    /**
     * testHttp()
     * Probeert een echte HTTP verbinding te maken.
     * In deze app is "android:usesCleartextTraffic" op TRUE gezet,
     * waardoor onversleuteld verkeer (HTTP) wordt toegestaan.
     */
    private void testHttp(TextView tv) {
        tv.setText("HTTP verbinding testen...");

        new Thread(() -> {
            boolean success = false;

            try {
                // Maak een onbeveiligde HTTP verbinding
                // Hiermee wordt onversleuteld verkeer gesimuleerd.
                HttpURLConnection conn = (HttpURLConnection)
                        new URL("http://webhook.site/1557ddd3-4f07-41c6-a225-60ce6abe178f").openConnection();

                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                // Verstuur de HTTP request en controleer de statuscode
                success = conn.getResponseCode() < 400;

                conn.disconnect();
            } catch (Exception ignored) {}

            boolean ok = success;

            runOnUiThread(() -> {
                // Kleur en tekst aanpassen afhankelijk van resultaat
                // Rood = HTTP toegestaan > onveilig verkeer
                // Groen = verbinding geblokkeerd > veilig gedrag
                int color = ok ? Color.parseColor("#FF4C4C") : Color.parseColor("#4CAF50");
                String msg = ok
                        ? "HTTP gelukt > onveilig verkeer toegestaan"
                        : "HTTP mislukt > verkeer geblokkeerd";

                tv.setBackgroundColor(color);
                tv.setText(msg);
                tv.setTextColor(Color.WHITE);

                // Toon pop-upmelding met status
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            });
        }).start();
    }
}
