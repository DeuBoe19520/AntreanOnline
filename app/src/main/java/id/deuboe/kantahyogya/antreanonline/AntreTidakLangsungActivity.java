package id.deuboe.kantahyogya.antreanonline;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import id.deuboe.kantahyogya.antreanonline.main.MainActivity;

import static id.deuboe.kantahyogya.antreanonline.AntreLangsungActivity.getDateTime;
import static id.deuboe.kantahyogya.antreanonline.AntreLangsungActivity.getEditText;
import static id.deuboe.kantahyogya.antreanonline.AntreLangsungActivity.textWatcher;
import static id.deuboe.kantahyogya.antreanonline.AntreLangsungActivity.validator;

public class AntreTidakLangsungActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TLANGSUNG_DATABASE = "tidak_langsung_database";
    AppCompatTextView textTitle;
    AppCompatEditText editNama, editTanggalLahir, editPekerjaan, editNIK, editAlamat, editTanggalPengurusan, editNomorWhatsApp,
            editNamaDiwakilkan, editTanggalLahirDiwakilkan, editPekerjaanDiwakilkan, editNIKDiwakilkan, editAlamatDiwakilkan;
    ContentLoadingProgressBar progressBar;
    AppCompatButton buttonKumpulkan, buttonTambah;
    private String nama, tanggalLahir, pekerjaan, NIK, alamat, tanggalPengurusan, nomorWhatsApp, layanan,
            namaDiwakilkan, tanggalLahirDiwakilkan, pekerjaanDiwakilkan, NIKDiwakilkan, alamatDiwakilkan;
    private final String DATE_PICKER = "DATE_PICKER";
    public static final int REQUEST_PDF = 2;
    MaterialDatePicker pengurusanDatePicker, lahirDatePicker, lahirDiwakilkanDatePicker;
    RecyclerView recyclerView;
    private List<String> fileNameList;
    private ArrayList<Uri> uriList = new ArrayList<>();
    private String fileTotal;
    private UploadListAdapter adapter;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private Uri mUri;
    int totalCount;
    String key = getDateTime("yyMMddHHmmssSSS");
    private String fileName;
    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antre_tidak_langsung);

        init();

        setFirebase();

        setCalendar();

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
        pengurusanDatePicker = builder.build();
        pengurusanDatePicker.addOnPositiveButtonClickListener(
                selection -> editTanggalPengurusan.setText(pengurusanDatePicker.getHeaderText()));
        pengurusanDatePicker.addOnNegativeButtonClickListener(
                v -> pengurusanDatePicker.dismiss()
        );

        CalendarConstraints.Builder constraintLahirBuilder = new CalendarConstraints.Builder();
        constraintLahirBuilder.setValidator(DateValidatorPointBackward.now());

        Builder builderLahir = Builder.datePicker();
        builderLahir.setTitleText("Pilih Tanggal Lahir");
        builderLahir.setCalendarConstraints(constraintLahirBuilder.build());
        lahirDatePicker = builderLahir.build();
        lahirDatePicker.addOnPositiveButtonClickListener(
                selection -> editTanggalLahir.setText(lahirDatePicker.getHeaderText())
        );
        lahirDatePicker.addOnNegativeButtonClickListener(
                v -> lahirDatePicker.dismiss()
        );

        Builder builderLahirDiwakilkan = Builder.datePicker();
        builderLahirDiwakilkan.setTitleText("Pilih Tanggal Lahir");
        builderLahir.setCalendarConstraints(constraintLahirBuilder.build());
        lahirDiwakilkanDatePicker = builderLahirDiwakilkan.build();
        lahirDiwakilkanDatePicker.addOnPositiveButtonClickListener(
                selection -> editTanggalLahirDiwakilkan.setText(lahirDiwakilkanDatePicker.getHeaderText())
        );
        lahirDiwakilkanDatePicker.addOnNegativeButtonClickListener(
                v -> lahirDiwakilkanDatePicker.dismiss()
        );
    }

    private void setFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(TLANGSUNG_DATABASE);
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    private void init() {
        progressBar = findViewById(R.id.progress_horizontal);
        layanan = getIntent().getStringExtra(SelectedActivity.LAYANAN);
        textTitle = findViewById(R.id.text_title);
        textTitle.setText(layanan);
        editNama = findViewById(R.id.edit_nama);
        editNama.addTextChangedListener(textWatcher(editNama));
        editTanggalLahir = findViewById(R.id.edit_tanggal_lahir);
        editTanggalLahir.addTextChangedListener(textWatcher(editTanggalLahir));
        editTanggalLahir.setOnClickListener(this);
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
        editNamaDiwakilkan = findViewById(R.id.edit_nama_diwakilkan);
        editNamaDiwakilkan.addTextChangedListener(textWatcher(editNamaDiwakilkan));
        editTanggalLahirDiwakilkan = findViewById(R.id.edit_tanggal_lahir_diwakilkan);
        editTanggalLahirDiwakilkan.addTextChangedListener(textWatcher(editTanggalLahirDiwakilkan));
        editTanggalLahirDiwakilkan.setOnClickListener(this);
        editPekerjaanDiwakilkan = findViewById(R.id.edit_pekerjaan_diwakilkan);
        editPekerjaanDiwakilkan.addTextChangedListener(textWatcher(editPekerjaanDiwakilkan));
        editNIKDiwakilkan = findViewById(R.id.edit_NIK_diwakilkan);
        editNIKDiwakilkan.addTextChangedListener(textWatcher(editNIKDiwakilkan));
        editAlamatDiwakilkan = findViewById(R.id.edit_alamat_diwakilkan);
        editAlamatDiwakilkan.addTextChangedListener(textWatcher(editAlamatDiwakilkan));

        recyclerView = findViewById(R.id.recyclerView);
        fileNameList = new ArrayList<>();
        adapter = new UploadListAdapter(fileNameList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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

    private String getFileName(Uri uri) {
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
        return validator(editAlamat, "Alamat");
    }

    private boolean validateTanggalPengurusan() {
        return validator(editTanggalPengurusan, "Tanggal Pengurusan");
    }

    private boolean validateNomorWhatsApp() {
        return validator(editNomorWhatsApp, "Nomor WhatsApp");
    }

    private boolean validateNamaDiwakilkan() {
        return validator(editNamaDiwakilkan, "Nama Lengkap");
    }

    private boolean validateTanggalLahirDiwakilkan() {
        return validator(editTanggalLahirDiwakilkan, "Tanggal Lahir");
    }

    private boolean validatePekerjaanDiwakilkan() {
        return validator(editPekerjaanDiwakilkan, "Pekerjaan");
    }

    private boolean validateNIKDiwakilkan() {
        return validator(editNIKDiwakilkan, "Nomor NIK");
    }

    private boolean validateAlamatDiwakilkan() {
        return validator(editAlamatDiwakilkan, "Alamat");
    }

    private boolean finalValidator() {
        return validateNama() & validateTanggalLahir() & validatePekerjaan()
                & validateNIK() & validateAlamatLengkap() & validateTanggalPengurusan()
                & validateNomorWhatsApp() & validateNamaDiwakilkan() & validateTanggalLahirDiwakilkan()
                & validatePekerjaanDiwakilkan() & validateNIKDiwakilkan() & validateAlamatDiwakilkan();
    }

    public void setUpload(String s, String s1) {
        hashMap.put(s, s1);
    }

    private void finalUpload() {
        for (int i = 0; i < uriList.size(); i++) {
            Uri praFile = uriList.get(i);
            mStorageReference = mFirebaseStorage.getReference(TLANGSUNG_DATABASE).child("D" + key);
            StorageReference fileReference = mStorageReference.child("D" + key + i + "." + getFileExtension(praFile));
            fileReference.putFile(praFile).addOnSuccessListener(taskSnapshot -> {
                nama = getEditText(editNama);
                tanggalLahir = getEditText(editTanggalLahir);
                pekerjaan = getEditText(editPekerjaan);
                alamat = getEditText(editAlamat);
                NIK = getEditText(editNIK);
                tanggalPengurusan = getEditText(editTanggalPengurusan);
                nomorWhatsApp = getEditText(editNomorWhatsApp);
                namaDiwakilkan = getEditText(editNamaDiwakilkan);
                tanggalLahirDiwakilkan = getEditText(editTanggalLahirDiwakilkan);
                pekerjaanDiwakilkan = getEditText(editPekerjaanDiwakilkan);
                alamatDiwakilkan = getEditText(editAlamatDiwakilkan);
                NIKDiwakilkan = getEditText(editNIKDiwakilkan);
                fileTotal = String.valueOf(uriList.size());
                setUpload("nama", nama);
                setUpload("tanggalLahir", tanggalLahir);
                setUpload("pekerjaan", pekerjaan);
                setUpload("alamat", alamat);
                setUpload("tanggalPengurusan", tanggalPengurusan);
                setUpload("nik", NIK);
                setUpload("layanan", layanan);
                setUpload("fileTotal", fileTotal);
                setUpload("nomorWhatsApp", nomorWhatsApp);
                setUpload("namaDiwakilkan", namaDiwakilkan);
                setUpload("tanggalLahirDiwakilkan", tanggalLahirDiwakilkan);
                setUpload("alamatDiwakilkan", alamatDiwakilkan);
                setUpload("nikDiwakilkan", NIKDiwakilkan);
                setUpload("pekerjaanDiwakilkan", pekerjaanDiwakilkan);
                mDatabaseReference.child("D" + key).setValue(hashMap);
                Toast.makeText(this, "Data berhasil dikumpulkan", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Terdapat kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
                if (progress == 100) {
                    buttonKumpulkan.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_tanggal_lahir:
                lahirDatePicker.show(getSupportFragmentManager(), DATE_PICKER);
                break;
            case R.id.edit_tanggal_lahir_diwakilkan:
                lahirDiwakilkanDatePicker.show(getSupportFragmentManager(), DATE_PICKER);
                break;
            case R.id.edit_tanggal_pengurusan:
                pengurusanDatePicker.show(getSupportFragmentManager(), DATE_PICKER);
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
