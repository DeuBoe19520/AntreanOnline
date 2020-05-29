package id.deuboe.kantahyogya.antreanonline;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import id.deuboe.kantahyogya.antreanonline.main.MainActivity;

public class AntreLangsungActivity extends AppCompatActivity implements OnClickListener {

  private static final String LANGSUNG_DATABASE = "langsung_database";
  AppCompatImageView imageBackground;
  AppCompatTextView textTitle;
  AppCompatEditText editNama, editTanggalLahir, editPekerjaan, editNIK, editAlamat, editTanggalPengurusan, editNomorWhatsApp;
  ContentLoadingProgressBar progressBar;
  AppCompatButton buttonKumpulkan, buttonTambah;
  private String nama, tanggalLahir, pekerjaan, NIK, alamat, tanggalPengurusan, layanan, fileName, nomorWhatsApp;
  public final String DATE_PICKER = "DATE_PICKER";
  public static final int REQUEST_PDF = 2;
  MaterialDatePicker materialDatePicker, materialDatePickerLahir;
  RecyclerView recyclerView;
  private List<String> fileNameList;
  private ArrayList<Uri> uriList = new ArrayList<>();
  private String fileTotal;
  private UploadListAdapter adapter;
  private FirebaseStorage mFirebaseStorage;
  private StorageReference mStorageReference;
  private FirebaseDatabase mFirebaseDatabase;
  private DatabaseReference mDatabaseReference;
  private Uri mUri;
  int totalCount;
  String key = getDateTime("yyMMddHHmmssSSS");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_antre_langsung);

    init();

    setFirebase();

    setCalendar();
  }

  private void init() {
    imageBackground = findViewById(R.id.image_background);
    progressBar = findViewById(R.id.progress_horizontal);
    layanan = getIntent().getStringExtra(SelectedActivity.LAYANAN);
    textTitle = findViewById(R.id.text_title);
    textTitle.setText(layanan);
    editNama = findViewById(R.id.edit_nama);
    editNama.addTextChangedListener(textWatcher(editNama));
    editTanggalLahir = findViewById(R.id.edit_tanggal_lahir);
    editTanggalLahir.addTextChangedListener(textWatcher(editTanggalLahir));
    editPekerjaan = findViewById(R.id.edit_pekerjaan);
    editPekerjaan.addTextChangedListener(textWatcher(editPekerjaan));
    editNIK = findViewById(R.id.edit_NIK);
    editNIK.addTextChangedListener(textWatcher(editNIK));
    editAlamat = findViewById(R.id.edit_alamat);
    editAlamat.addTextChangedListener(textWatcher(editAlamat));
    editTanggalPengurusan = findViewById(R.id.edit_tanggal_pengurusan);
    editTanggalPengurusan.addTextChangedListener(textWatcher(editTanggalPengurusan));
    editTanggalPengurusan.setOnClickListener(this);
    editNomorWhatsApp = findViewById(R.id.edit_nomor_whatsapp);
    editNomorWhatsApp.addTextChangedListener(textWatcher(editNomorWhatsApp));
    buttonKumpulkan = findViewById(R.id.button_kumpulkan);
    buttonKumpulkan.setOnClickListener(this);
    buttonTambah = findViewById(R.id.button_tambah);
    buttonTambah.setOnClickListener(this);

    recyclerView = findViewById(R.id.recyclerView);
    fileNameList = new ArrayList<>();
    adapter = new UploadListAdapter(fileNameList);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
  }

  private void setFirebase() {
    mFirebaseDatabase = FirebaseDatabase.getInstance();
    mDatabaseReference = mFirebaseDatabase.getReference(LANGSUNG_DATABASE);
    mFirebaseStorage = FirebaseStorage.getInstance();
  }

  private void setCalendar() {
    Date date = new Date();
    Calendar calendarDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendarDate.setTime(date);
    calendarDate.add(Calendar.DATE, 0);
    date = calendarDate.getTime();
    long nextDate = date.getTime();

    ArrayList<CalendarConstraints.DateValidator> validators = new ArrayList<>();

    WeekDayValidator weekDayValidator = new WeekDayValidator();
    validators.add(weekDayValidator);
    validators.add(DateValidatorPointForward.from(nextDate));
    CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
    constraintBuilder.setValidator(CompositeDateValidator.allOf(validators));

    Builder builder = Builder.datePicker();
    builder.setTitleText("Pilih Tanggal Pengurusan");
    builder.setCalendarConstraints(constraintBuilder.build());
    materialDatePicker = builder.build();
    materialDatePicker.addOnPositiveButtonClickListener(
        selection -> editTanggalPengurusan.setText(materialDatePicker.getHeaderText()));

    CalendarConstraints.Builder constraintLahirBuilder = new CalendarConstraints.Builder();
    constraintLahirBuilder.setValidator(DateValidatorPointBackward.now());
    CalendarConstraints constraintLahir = constraintLahirBuilder.build();

    Builder builderLahir = Builder.datePicker();
    builderLahir.setTitleText("Pilih Tanggal Lahir");
    builderLahir.setCalendarConstraints(constraintLahir);
    materialDatePickerLahir = builderLahir.build();
    materialDatePickerLahir.addOnPositiveButtonClickListener(
        selection -> editTanggalLahir.setText(materialDatePickerLahir.getHeaderText()));
    materialDatePickerLahir.addOnNegativeButtonClickListener(
        v -> materialDatePickerLahir.dismiss());

    editTanggalLahir.setOnClickListener(
        v -> materialDatePickerLahir.show(getSupportFragmentManager(), DATE_PICKER));
  }

  private void pickPdfFile() {
    Intent intent = new Intent();
    intent.setType("application/pdf");
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Pilih Berkas"), REQUEST_PDF);
  }

  private String getFileExtension(Uri uri) {
    ContentResolver contentResolver = getContentResolver();
    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_PDF && resultCode == RESULT_OK) {
      if (Objects.requireNonNull(data).getClipData() != null) {
        int totalItemSelected = data.getClipData().getItemCount();

        for (totalCount = 0; totalCount < totalItemSelected; totalCount++) {

          mUri = data.getClipData().getItemAt(totalCount).getUri();

          fileName = getFileName(mUri);
          uriList.add(mUri);
          fileNameList.add(fileName);
          buttonTambah.setText(String.valueOf(uriList.size()));
          adapter.notifyDataSetChanged();


        }
      } else if (data.getData() != null) {

        mUri = data.getData().normalizeScheme();

        fileName = getFileName(mUri);
        uriList.add(mUri);
        fileNameList.add(fileName);
        buttonTambah.setText(String.valueOf(uriList.size()));
        adapter.notifyDataSetChanged();
      }
    }
  }

  public String getFileName(Uri uri) {
    String result = null;
    if (uri.getScheme().equals("content")) {
      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        Objects.requireNonNull(cursor).close();
      }
    }

    if (result == null) {
      result = uri.getPath();
      int cut = result.lastIndexOf("/");
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }

    return result;
  }

  @SuppressLint("SimpleDateFormat")
  public static String getDateTime(String pattern) {
    return new SimpleDateFormat(pattern).format(new Date());
  }

  public static String getEditText(AppCompatEditText editText) {
    String s = Objects.requireNonNull(editText.getText()).toString().trim();
    return s;
  }

  public static TextWatcher textWatcher(AppCompatEditText editText) {
    TextWatcher watcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String string = getEditText(editText);
        if (!string.isEmpty()) {
          editText.setError(null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };

    return watcher;
  }

  public static boolean validator(AppCompatEditText editText, String s) {
    String string = getEditText(editText);
    if (string.isEmpty()) {
      editText.setError("Kolom " + s + " Harap Diisi!");
      return false;
    } else {
      editText.setError(null);
      return true;
    }
  }

  private boolean validateNama() {
    return validator(editNama, "Nama Lengkap");
  }

  private boolean validateTanggalLahir() {
    return validator(editTanggalLahir, "Tanggal Lahir");
  }

  private boolean validatePekerjaan() {
    return validator(editPekerjaan, "Pekerjaan");
  }

  private boolean validateNIK() {
    return validator(editNIK, "Nomor NIK");
  }

  private boolean validateAlamatLengkap() {
    return validator(editAlamat, "Alamat Lengkap");
  }

  private boolean validateTanggalPengurusan() {
    return validator(editTanggalPengurusan, "Tanggal Pengurusan");
  }

  private boolean validateNomorWhatsApp() {
    return validator(editNomorWhatsApp, "Nomor WhatsApp");
  }

  private boolean finalValidator() {
    return validateNama() & validateTanggalLahir() & validatePekerjaan()
        & validateNIK() & validateAlamatLengkap() & validateTanggalPengurusan()
        & validateNomorWhatsApp();
  }

  private void finalUpload() {
    for (int i = 0; i < uriList.size(); i++) {
      Uri praFile = uriList.get(i);
      mStorageReference = mFirebaseStorage.getReference(LANGSUNG_DATABASE).child("M" + key);
      StorageReference fileReference = mStorageReference
          .child("M" + key + i + "." + getFileExtension(praFile));
      fileReference.putFile(praFile).addOnSuccessListener(taskSnapshot -> {
        HashMap<String, String> hashMap = new HashMap<>();
        nama = getEditText(editNama);
        tanggalLahir = getEditText(editTanggalLahir);
        pekerjaan = getEditText(editPekerjaan);
        alamat = getEditText(editAlamat);
        NIK = getEditText(editNIK);
        tanggalPengurusan = getEditText(editTanggalPengurusan);
        fileTotal = String.valueOf(uriList.size());
        nomorWhatsApp = getEditText(editNomorWhatsApp);
        hashMap.put("nama", nama);
        hashMap.put("tanggalLahir", tanggalLahir);
        hashMap.put("pekerjaan", pekerjaan);
        hashMap.put("alamat", alamat);
        hashMap.put("tanggalPengurusan", tanggalPengurusan);
        hashMap.put("nik", NIK);
        hashMap.put("layanan", layanan);
        hashMap.put("fileTotal", fileTotal);
        hashMap.put("nomorWhatsApp", nomorWhatsApp);
        mDatabaseReference.child("M" + key).setValue(hashMap);
        Toast.makeText(this, "Data berhasil dikumpulkan", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
      }).addOnFailureListener(e ->
          Toast.makeText(this, "Terdapat kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show()
      ).addOnProgressListener(taskSnapshot -> {
        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
            .getTotalByteCount());
        progressBar.setProgress((int) progress);
        if (progress == 100) {
          buttonKumpulkan.setEnabled(true);
          progressBar.setVisibility(View.INVISIBLE);
        }
      });
    }
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.edit_tanggal_pengurusan:
        materialDatePicker.show(getSupportFragmentManager(), DATE_PICKER);
        break;
      case R.id.button_tambah:
        pickPdfFile();
        break;
      case R.id.button_kumpulkan:
        if (uriList.size() == 0) {
          Toast.makeText(this, "Belum ada berkas", Toast.LENGTH_SHORT).show();
        } else if (finalValidator()) {
          finalUpload();
          buttonKumpulkan.setEnabled(false);
          progressBar.setVisibility(View.VISIBLE);
        }
        break;
    }
  }
}
