package com.example.applicatie_privacy_pass_180;

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

        //  ️UI-elementen koppelen aan de variabelen
        TextView tv = findViewById(R.id.tvStatus);

        // Knop koppelen aan de HTTPS testfunctie
        findViewById(R.id.btnTest).setOnClickListener(v -> testHttps(tv));
    }

    /**
     * testHttps()
     * Probeert een echte HTTPS verbinding te maken.
     * In deze app is "android:usesCleartextTraffic" op FALSE gezet,
     * waardoor enkel beveiligde (versleutelde) HTTPS verbindingen zijn toegestaan.
     */
    private void testHttps(TextView tv) {
        tv.setText("HTTPS verbinding testen...");

        new Thread(() -> {
            boolean success = false;
            try {
                // Maak een veilige HTTPS verbinding met dezelfde webhook site
                // De verbinding is versleuteld via TLS.
                HttpURLConnection conn = (HttpURLConnection)
                        new URL("https://webhook.site/1557ddd3-4f07-41c6-a225-60ce6abe178f").openConnection();

                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                // Verstuur de HTTPS request en controleer de statuscode
                success = conn.getResponseCode() < 400;

                conn.disconnect();
            } catch (Exception ignored) {}

            boolean ok = success;

            runOnUiThread(() -> {
                // Kleur en tekst aanpassen afhankelijk van resultaat
                // Groen = HTTPS werkt > veilig verkeer toegestaan
                // Rood = mislukt > er is een probleem met de veilige verbinding
                int color = ok ? Color.parseColor("#4CAF50") : Color.parseColor("#FF4C4C");
                String msg = ok
                        ? "HTTPS gelukt > veilig verkeer toegestaan"
                        : "HTTPS mislukt > verbinding mislukt";

                tv.setBackgroundColor(color);
                tv.setText(msg);
                tv.setTextColor(Color.WHITE);

                // Toon pop-upmelding met status
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            });
        }).start();
    }
}
