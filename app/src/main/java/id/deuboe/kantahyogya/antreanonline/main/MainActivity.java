package id.deuboe.kantahyogya.antreanonline.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.GoogleAuthProvider;
import id.deuboe.kantahyogya.antreanonline.AntreLangsungActivity;
import id.deuboe.kantahyogya.antreanonline.AntreTidakLangsungActivity;
import id.deuboe.kantahyogya.antreanonline.R;
import id.deuboe.kantahyogya.antreanonline.SelectedActivity;
import id.deuboe.kantahyogya.antreanonline.SignInActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private ExpandableListView mExpandableListView;
  private List<String> listGroup;
  private HashMap<String, List<String>> listItem;
  private ListAdapter adapter;
  private int lastExpandedPosition = -1;
  FirebaseUser mFirebaseUser;
  String selected;
  private AppCompatTextView textHai, textSilaPilihLayanan;
  // SignIn
  private final String TAG = this.getClass().getName();
  private GoogleSignInClient mGoogleSignInClient;
  private FirebaseAuth mFirebaseAuth;
  private static final int RC_SIGN_IN = 161;
  private GoogleSignInAccount signInAccount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    init();
    setmExpandableListView();
    initListData();
    checkUser();
    google();
  }

  private void checkUser() {
    signInAccount = GoogleSignIn.getLastSignedInAccount(this);
    if (signInAccount == null) {
      startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    } else {
      textHai.setText(String.format("Hai, %s", signInAccount.getDisplayName()));
    }
  }

  private void google() {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
  }

  private void init() {
    textSilaPilihLayanan = findViewById(R.id.text_sila_pilih_layanan);
    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseUser = mFirebaseAuth.getCurrentUser();
    textHai = findViewById(R.id.text_hai);
    mExpandableListView = findViewById(R.id.expandable_list);
  }

  private void initListData() {
    addGroupName(R.string.pelayanan_pemeliharaan_data_pendaftaran_tanah);
    addGroupName(R.string.pelayanan_peralihan_hak);
    addGroupName(R.string.pelayanan_pendaftaran_tanah_pertama_kali);
    addGroupName(R.string.pelayanan_informasi_pertanahan);
    addGroupName(R.string.pelayanan_hak_tanggungan);
    addGroupName(R.string.pelayanan_sertipikat_pengganti);
    addGroupName(R.string.pelayanan_hak_milik_satuan_rumah_susun);
    addGroupName(R.string.pelayanan_perubahan_hak);
    addGroupName(R.string.pelayanan_pengukuran);
    addGroupName(R.string.pelayanan_pemberian_hak_atas_tanah);
    addGroupName(R.string.pelayanan_perpanjangan_hak_atas_tanah);
    addGroupName(R.string.pelayanan_pembaruan_hak);
    addGroupName(R.string.pelayanan_pertimbangan_teknis_pertanahan);

    String[] arrayItem0 = arrayItem(R.array.pelayanan_pemeliharaan_data_pendaftaran_tanah);
    List<String> list0 = makeList(arrayItem0);

    String[] arrayItem1 = arrayItem(R.array.pelayanan_peralihan_hak);
    List<String> list1 = makeList(arrayItem1);

    String[] arrayItem2 = arrayItem(R.array.pelayanan_pendaftaran_tanah_pertama_kali);
    List<String> list2 = makeList(arrayItem2);

    String[] arrayItem3 = arrayItem(R.array.pelayanan_informasi_pertanahan);
    List<String> list3 = makeList(arrayItem3);

    String[] arrayItem4 = arrayItem(R.array.pelayanan_hak_tanggungan);
    List<String> list4 = makeList(arrayItem4);

    String[] arrayItem5 = arrayItem(R.array.pelayanan_sertipikat_pengganti);
    List<String> list5 = makeList(arrayItem5);

    String[] arrayItem6 = arrayItem(R.array.pelayanan_hak_milik_satuan_rumah_susun);
    List<String> list6 = makeList(arrayItem6);

    String[] arrayItem7 = arrayItem(R.array.pelayanan_perubahan_hak);
    List<String> list7 = makeList(arrayItem7);

    String[] arrayItem8 = arrayItem(R.array.pelayanan_pengukuran);
    List<String> list8 = makeList(arrayItem8);

    String[] arrayItem9 = arrayItem(R.array.pelayanan_pemberian_hak_atas_tanah);
    List<String> list9 = makeList(arrayItem9);

    String[] arrayItem10 = arrayItem(R.array.pelayanan_perpanjangan_hak_atas_tanah);
    List<String> list10 = makeList(arrayItem10);

    String[] arrayItem11 = arrayItem(R.array.pelayanan_pembaruan_hak);
    List<String> list11 = makeList(arrayItem11);

    String[] arrayItem12 = arrayItem(R.array.pelayanan_pertimbangan_teknis_pertanahan);
    List<String> list12 = makeList(arrayItem12);

    combineGroupNItem(0, list0);
    combineGroupNItem(1, list1);
    combineGroupNItem(2, list2);
    combineGroupNItem(3, list3);
    combineGroupNItem(4, list4);
    combineGroupNItem(5, list5);
    combineGroupNItem(6, list6);
    combineGroupNItem(7, list7);
    combineGroupNItem(8, list8);
    combineGroupNItem(9, list9);
    combineGroupNItem(10, list10);
    combineGroupNItem(11, list11);
    combineGroupNItem(12, list12);

    adapter.notifyDataSetChanged();
  }

  private void setmExpandableListView() {
    listGroup = new ArrayList<>();
    listItem = new HashMap<>();
    adapter = new ListAdapter(this, listGroup, listItem);
    mExpandableListView.setAdapter(adapter);
    mExpandableListView.setOnGroupExpandListener(groupPosition -> {
      if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
        mExpandableListView.collapseGroup(lastExpandedPosition);
      }
      lastExpandedPosition = groupPosition;
    });
    mExpandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
      selected = (String) adapter.getChild(groupPosition, childPosition);
      String key = "key";
      String[] array0, array1, array2, array3, array4, array5, array6, array7, array8, array9, array10, array11, array12;
      array0 = arrayItem(R.array.pelayanan_pemeliharaan_data_pendaftaran_tanah);
      String s0_0, s0_1, s0_2;
      s0_0 = array0[0];
      s0_1 = array0[1];
      s0_2 = array0[2];
      boolean equal0_0, equal0_1, equal0_2;
      equal0_0 = equal(s0_0);
      equal0_1 = equal(s0_1);
      equal0_2 = equal(s0_2);
      array1 = arrayItem(R.array.pelayanan_peralihan_hak);
      String s1_0, s1_1, s1_2, s1_3, s1_4, s1_5, s1_6;
      s1_0 = array1[0];
      s1_1 = array1[1];
      s1_2 = array1[2];
      s1_3 = array1[3];
      s1_4 = array1[4];
      s1_5 = array1[5];
      s1_6 = array1[6];
      boolean equal1_0, equal1_1, equal1_2, equal1_3, equal1_4, equal1_5, equal1_6;
      equal1_0 = equal(s1_0);
      equal1_1 = equal(s1_1);
      equal1_2 = equal(s1_2);
      equal1_3 = equal(s1_3);
      equal1_4 = equal(s1_4);
      equal1_5 = equal(s1_5);
      equal1_6 = equal(s1_6);
      array2 = arrayItem(R.array.pelayanan_pendaftaran_tanah_pertama_kali);
      String s2_0, s2_1;
      s2_0 = array2[0];
      s2_1 = array2[1];
      boolean equal2_0, equal2_1;
      equal2_0 = equal(s2_0);
      equal2_1 = equal(s2_1);
      array3 = arrayItem(R.array.pelayanan_informasi_pertanahan);
      String s3_0, s3_1, s3_2, s3_3, s3_4;
      s3_0 = array3[0];
      s3_1 = array3[1];
      s3_2 = array3[2];
      s3_3 = array3[3];
      s3_4 = array3[4];
      boolean equal3_0, equal3_1, equal3_2, equal3_3, equal3_4;
      equal3_0 = equal(s3_0);
      equal3_1 = equal(s3_1);
      equal3_2 = equal(s3_2);
      equal3_3 = equal(s3_3);
      equal3_4 = equal(s3_4);
      array4 = arrayItem(R.array.pelayanan_hak_tanggungan);
      String s4_0, s4_1, s4_2, s4_3;
      s4_0 = array4[0];
      s4_1 = array4[1];
      s4_2 = array4[2];
      s4_3 = array4[3];
      boolean equal4_0, equal4_1, equal4_2, equal4_3;
      equal4_0 = equal(s4_0);
      equal4_1 = equal(s4_1);
      equal4_2 = equal(s4_2);
      equal4_3 = equal(s4_3);
      array5 = arrayItem(R.array.pelayanan_sertipikat_pengganti);
      String s5_0, s5_1, s5_2;
      s5_0 = array5[0];
      s5_1 = array5[1];
      s5_2 = array5[2];
      boolean equal5_0, equal5_1, equal5_2;
      equal5_0 = equal(s5_0);
      equal5_1 = equal(s5_1);
      equal5_2 = equal(s5_2);
      array6 = arrayItem(R.array.pelayanan_hak_milik_satuan_rumah_susun);
      String s6_0, s6_1;
      s6_0 = array6[0];
      s6_1 = array6[1];
      boolean equal6_0, equal6_1;
      equal6_0 = equal(s6_0);
      equal6_1 = equal(s6_1);
      array7 = arrayItem(R.array.pelayanan_perubahan_hak);
      String s7_0, s7_1;
      s7_0 = array7[0];
      s7_1 = array7[1];
      boolean equal7_0, equal7_1;
      equal7_0 = equal(s7_0);
      equal7_1 = equal(s7_1);
      array8 = arrayItem(R.array.pelayanan_pengukuran);
      String s8_0, s8_1;
      s8_0 = array8[0];
      s8_1 = array8[1];
      boolean equal8_0, equal8_1;
      equal8_0 = equal(s8_0);
      equal8_1 = equal(s8_1);
      array9 = arrayItem(R.array.pelayanan_pemberian_hak_atas_tanah);
      String s9_0, s9_1, s9_2, s9_3, s9_4, s9_5, s9_6, s9_7, s9_8, s9_9, s9_10, s9_11, s9_12, s9_13, s9_14;
      s9_0 = array9[0];
      s9_1 = array9[1];
      s9_2 = array9[2];
      s9_3 = array9[3];
      s9_4 = array9[4];
      s9_5 = array9[5];
      s9_6 = array9[6];
      s9_7 = array9[7];
      s9_8 = array9[8];
      s9_9 = array9[9];
      s9_10 = array9[10];
      s9_11 = array9[11];
      s9_12 = array9[12];
      s9_13 = array9[13];
      s9_14 = array9[14];
      boolean equal9_0, equal9_1, equal9_2, equal9_3, equal9_4, equal9_5, equal9_6, equal9_7,
          equal9_8, equal9_9, equal9_10, equal9_11, equal9_12, equal9_13, equal9_14;
      equal9_0 = equal(s9_0);
      equal9_1 = equal(s9_1);
      equal9_2 = equal(s9_2);
      equal9_3 = equal(s9_3);
      equal9_4 = equal(s9_4);
      equal9_5 = equal(s9_5);
      equal9_6 = equal(s9_6);
      equal9_7 = equal(s9_7);
      equal9_8 = equal(s9_8);
      equal9_9 = equal(s9_9);
      equal9_10 = equal(s9_10);
      equal9_11 = equal(s9_11);
      equal9_12 = equal(s9_12);
      equal9_13 = equal(s9_13);
      equal9_14 = equal(s9_14);
      array10 = arrayItem(R.array.pelayanan_perpanjangan_hak_atas_tanah);
      String s10_0, s10_1;
      s10_0 = array10[0];
      s10_1 = array10[1];
      boolean equal10_0, equal10_1;
      equal10_0 = equal(s10_0);
      equal10_1 = equal(s10_1);
      array11 = arrayItem(R.array.pelayanan_pembaruan_hak);
      String s11_0, s11_1, s11_2, s11_3, s11_4, s11_5, s11_6, s11_7, s11_8, s11_9;
      s11_0 = array11[0];
      s11_1 = array11[1];
      s11_2 = array11[2];
      s11_3 = array11[3];
      s11_4 = array11[4];
      s11_5 = array11[5];
      s11_6 = array11[6];
      s11_7 = array11[7];
      s11_8 = array11[8];
      s11_9 = array11[9];
      boolean equal11_0, equal11_1, equal11_2, equal11_3, equal11_4, equal11_5, equal11_6, equal11_7, equal11_8, equal11_9;
      equal11_0 = equal(s11_0);
      equal11_1 = equal(s11_1);
      equal11_2 = equal(s11_2);
      equal11_3 = equal(s11_3);
      equal11_4 = equal(s11_4);
      equal11_5 = equal(s11_5);
      equal11_6 = equal(s11_6);
      equal11_7 = equal(s11_7);
      equal11_8 = equal(s11_8);
      equal11_9 = equal(s11_9);
      array12 = arrayItem(R.array.pelayanan_pertimbangan_teknis_pertanahan);
      String s12_0, s12_1;
      s12_0 = array12[0];
      s12_1 = array12[1];
      boolean equal12_0, equal12_1;
      equal12_0 = equal(s12_0);
      equal12_1 = equal(s12_1);

      if (equal0_0) {
        intent(R.string.url_pemecahan_bidang, s0_0);
      } else if (equal0_1) {
        intent(R.string.url_penggabungan_bidang, s0_1);
      } else if (equal0_2) {
        intent(R.string.url_pemisahan_bidang, s0_2);
      } else if (equal1_0) {
        intent(R.string.url_jual_beli, s1_0);
      } else if (equal1_1) {
        intent(R.string.url_hibah, s1_1);
      } else if (equal1_2) {
        intent(R.string.url_pewarisan, s1_2);
      } else if (equal1_3) {
        intent(R.string.url_tukar_menukar, s1_3);
      } else if (equal1_4) {
        intent(R.string.url_pemasukan_dalam_perusahaan, s1_4);
      } else if (equal1_5) {
        intent(R.string.url_pembagian_hak_bersama, s1_5);
      } else if (equal1_6) {
        intent(R.string.url_lelang, s1_6);
      } else if (equal2_0) {
        intent(R.string.url_konversi_pengakuan_dan_penegasan, s2_0);
      } else if (equal2_1) {
        intent(R.string.url_konversi_pengakuan_dan_penegasan_wakaf, s2_1);
      } else if (equal3_0) {
        intent(R.string.url_blokir, s3_0);
      } else if (equal3_1) {
        intent(R.string.url_pengecekan, s3_1);
      } else if (equal3_2) {
        intent(R.string.url_sita, s3_2);
      } else if (equal3_3) {
        intent(R.string.url_pengangkatan_sita, s3_3);
      } else if (equal3_4) {
        intent(R.string.url_surat_keterangan_pendaftaran_tanah, s3_4);
      } else if (equal4_0) {
        intent(R.string.url_pendaftaran_hak_tanggungan, s4_0);
      } else if (equal4_1) {
        intent(R.string.url_roya, s4_1);
      } else if (equal4_2) {
        intent(R.string.url_peralihan_hak_tanggungan, s4_2);
      } else if (equal4_3) {
        intent(R.string.url_perubahan_kreditur, s4_3);
      } else if (equal5_0) {
        intent(R.string.url_pengganti_sertipikat_hilang, s5_0);
      } else if (equal5_1) {
        intent(R.string.url_pengganti_sertipikat_lama, s5_1);
      } else if (equal5_2) {
        intent(R.string.url_pengganti_sertipikat_rusak, s5_2);
      } else if (equal6_0) {
        intent(R.string.url_pendaftaran_hmsrs, s6_0);
      } else if (equal6_1) {
        intent(R.string.url_perpanjangan_hmsrs, s6_1);
      } else if (equal7_0) {
        intent(R.string.url_perubahan_hak_wakaf, s7_0);
      } else if (equal7_1) {
        intent(R.string.url_perubahan_hak_rumah_tinggal, s7_1);
      } else if (equal8_0) {
        intent(R.string.url_pengembalian_batas, s8_0);
      } else if (equal8_1) {
        intent(R.string.url_mengetahui_luas, s8_1);
      } else if (equal9_0) {
        intent(R.string.url_pemberian_hak_milik_perorangan, s9_0);
      } else if (equal9_1) {
        intent(R.string.url_pemberian_hak_milik_badan_hukum, s9_1);
      } else if (equal9_2) {
        intent(R.string.url_pemberian_hak_guna_usaha_perorangan, s9_2);
      } else if (equal9_3) {
        intent(R.string.url_pemberian_hak_guna_usaha_badan_hukum, s9_3);
      } else if (equal9_4) {
        intent(R.string.url_pemberian_hak_guna_bangunan_perorangan, s9_4);
      } else if (equal9_5) {
        intent(R.string.url_pemberian_hak_guna_bangunan_badan_hukum, s9_5);
      } else if (equal9_6) {
        intent(R.string.url_pemberian_hak_pakai_perorangan_wni, s9_6);
      } else if (equal9_7) {
        intent(R.string.url_pemberian_hak_pakai_perorangan_wna, s9_7);
      } else if (equal9_8) {
        intent(R.string.url_pemberian_hak_pakai_badan_hukum_indonesia, s9_8);
      } else if (equal9_9) {
        intent(R.string.url_pemberian_hak_pakai_badan_hukum_asing, s9_9);
      } else if (equal9_10) {
        intent(R.string.url_pemberian_hak_pakai_instansi_pemerintah, s9_10);
      } else if (equal9_11) {
        intent(R.string.url_pemberian_hak_pakai_pemerintah_asing, s9_11);
      } else if (equal9_12) {
        intent(R.string.url_pemberian_hak_pengelolaan_instansi_pemerintah_bumn_dan_bumd, s9_12);
      } else if (equal9_13) {
        intent(R.string.url_pemberian_hak_wakaf, s9_13);
      } else if (equal9_14) {
        intent(R.string.url_p3mb_atau_prk5, s9_14);
      } else if (equal10_0) {
        intent(R.string.url_perpanjangan_hak_guna_usaha, s10_0);
      } else if (equal10_1) {
        intent(R.string.url_perpanjangan_hak_guna_bangunan_atau_hak_pakai, s10_1);
      } else if (equal11_0) {
        intent(R.string.url_pembaruan_hak_guna_usaha_perorangan, s11_0);
      } else if (equal11_1) {
        intent(R.string.url_pembaruan_hak_guna_usaha_badan_hukum, s11_1);
      } else if (equal11_2) {
        intent(R.string.url_pembaruan_hak_guna_bangunan_perorangan, s11_2);
      } else if (equal11_3) {
        intent(R.string.url_pembaruan_hak_guna_bangunan_badan_hukum, s11_3);
      } else if (equal11_4) {
        intent(R.string.url_pembaruan_hak_pakai_perorangan_wni, s11_4);
      } else if (equal11_5) {
        intent(R.string.url_pembaruan_hak_pakai_perorangan_wna, s11_5);
      } else if (equal11_6) {
        intent(R.string.url_pembaruan_hak_pakai_badan_hukum_indonesia, s11_6);
      } else if (equal11_7) {
        intent(R.string.url_pembaruan_hak_pakai_badan_hukum_asing, s11_7);
      } else if (equal11_8) {
        intent(R.string.url_pembaruan_hak_pakai_instansi_pemerintah, s11_8);
      } else if (equal11_9) {
        intent(R.string.url_pembaruan_hak_pakai_pemerintah_asing, s11_9);
      } else if (equal12_0) {
        intent(R.string.url_pertimbangan_teknis_pertanahan_dalam_rangka_izin_lokasi, s12_0);
      } else if (equal12_1) {
        intent(
            R.string.url_pertimbangan_teknis_pertanahan_dalam_rangka_izin_perubahan_penggunaan_tanah,
            s12_1);
      }

      return true;
    });
  }

  private void addGroupName(int groupName) {
    listGroup.add(getString(groupName));
  }

  public String[] arrayItem(int arrayId) {
    return getResources().getStringArray(arrayId);
  }

  private List<String> makeList(String[] arrayItem) {
    return new ArrayList<>(Arrays.asList(arrayItem));
  }

  private void combineGroupNItem(int i, List<String> list) {
    listItem.put(listGroup.get(i), list);
  }

  private boolean equal(String s) {
    return Objects.equals(selected, s);
  }

  private void intent(int idUrl, String layanan) {
    Intent intentUrl = new Intent(getApplicationContext(), SelectedActivity.class);
    intentUrl.putExtra("url", setUrl(idUrl));
    intentUrl.putExtra("layanan", layanan);
    startActivity(intentUrl);
  }

  private String setUrl(int idUrl) {
    return getString(idUrl);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.item_logout:
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
          startActivity(new Intent(getApplicationContext(), SignInActivity.class));
          Toast.makeText(this, "Sampai Jumpa\n\t" + signInAccount.getDisplayName(),
              Toast.LENGTH_SHORT).show();
        });
        break;
      case R.id.item_tanya:
        String s = "https://wa.me/6285156901191";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(intent);
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
