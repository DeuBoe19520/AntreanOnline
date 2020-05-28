package id.deuboe.kantahyogya.antreanonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;

    public UploadListAdapter(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String fileName = fileNameList.get(position);
        holder.fileNameText.setText(fileName);
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public AppCompatTextView fileNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            fileNameText = mView.findViewById(R.id.text_pdf);
        }
    }

}


