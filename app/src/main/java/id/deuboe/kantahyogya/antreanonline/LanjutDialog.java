package id.deuboe.kantahyogya.antreanonline;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class LanjutDialog extends AppCompatDialogFragment implements OnClickListener {

  private View view;
  AppCompatTextView textPilih;
  SelectedActivity selectedActivity = new SelectedActivity();
  public String layananText;


  @SuppressLint("InflateParams")
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

    LayoutInflater inflater = getActivity().getLayoutInflater();
    view = inflater.inflate(R.layout.layout_lanjut, null);

    builder.setView(view);

    init();
    getLayanan();
    return builder.create();
  }

  private void init() {
    CardView cardMandiri = view.findViewById(R.id.mandiri);
    cardMandiri.setOnClickListener(this);
    CardView cardMewakilkan = view.findViewById(R.id.mewakilkan);
    cardMewakilkan.setOnClickListener(this);
    textPilih = view.findViewById(R.id.text_pilih);
  }

  private void getLayanan() {
    selectedActivity = (SelectedActivity) getActivity();
    layananText = Objects.requireNonNull(selectedActivity).getLayanan();
  }

  private void intent(Class cl) {
    Intent intent = new Intent(getContext(), cl);
    intent.putExtra(SelectedActivity.LAYANAN, layananText);
    startActivity(intent);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.mandiri:
        intent(AntreLangsungActivity.class);
        break;
      case R.id.mewakilkan:
        intent(AntreTidakLangsungActivity.class);
        break;
    }
  }
}
