package id.deuboe.kantahyogya.antreanonline;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsIntent.Builder;

import java.util.Objects;


public class SelectedActivity extends AppCompatActivity implements OnClickListener {

  AppCompatButton buttonSite, buttonTanya, buttonLanjut;
  public String url, urlTanya, layanan;
  public static final String LAYANAN = "layanan";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_selected);

   init();
   setLayanan();
  }

  private void setLayanan() {
    layanan = getIntent().getStringExtra("layanan");
    Objects.requireNonNull(getSupportActionBar()).setTitle(layanan);
  }

  public String getLayanan() {
    return layanan;
  }

  private void init() {
    buttonSite = findViewById(R.id.button_site);
    buttonSite.setOnClickListener(this);
    url = getIntent().getStringExtra("url");
    buttonTanya = findViewById(R.id.button_tanya);
    buttonTanya.setOnClickListener(this);
    buttonLanjut = findViewById(R.id.button_lanjut);
    buttonLanjut.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_site:
        customTabs(url);
        break;
      case R.id.button_tanya:
        urlTanya = "https://wa.me/6285156901191?text=Halo,%20mau%20tanya%20tentang%20pemindai%20berbasis%20OCR.";
        customTabs(urlTanya);
        break;
      case R.id.button_lanjut:
        openDialog();
        break;
    }
  }

  private void openDialog() {
    LanjutDialog dialog = new LanjutDialog();
    dialog.show(getSupportFragmentManager(), "Dialog");
  }

  private void customTabs(String url) {
    CustomTabsIntent.Builder builder = new Builder();
    CustomTabsIntent intent = builder.build();
    builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
    builder.setShowTitle(true);
    intent.launchUrl(this, Uri.parse(url));
  }
}
