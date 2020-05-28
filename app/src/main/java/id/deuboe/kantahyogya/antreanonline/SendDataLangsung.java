package id.deuboe.kantahyogya.antreanonline;

import java.util.ArrayList;
import java.util.List;

public class SendDataLangsung {

    private String nama, tanggalLahir, pekerjaan, NIK, alamat, tanggalPengurusan, layanan, today;
    private String fileTotal, file0, file1;

    public SendDataLangsung() {
    }

    public SendDataLangsung(
            String nama,
            String tanggalLahir,
            String pekerjaan,
            String NIK,
            String alamat,
            String tanggalPengurusan,
            String layanan,
            String today,
            String fileTotal,
            String file0,
            String file1) {
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.pekerjaan = pekerjaan;
        this.NIK = NIK;
        this.alamat = alamat;
        this.tanggalPengurusan = tanggalPengurusan;
        this.layanan = layanan;
        this.today = today;
        this.fileTotal = fileTotal;
        this.file0 = file0;
        this.file1 = file1;
    }
}
