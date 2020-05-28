package id.deuboe.kantahyogya.antreanonline.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import androidx.appcompat.widget.AppCompatTextView;
import id.deuboe.kantahyogya.antreanonline.R;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ListAdapter extends BaseExpandableListAdapter {

  private Context context;
  private List<String> listGroup;
  private HashMap<String, List<String>> listItem;

  public ListAdapter(Context context, List<String> listGroup,
      HashMap<String, List<String>> listItem) {
    this.context = context;
    this.listGroup = listGroup;
    this.listItem = listItem;
  }

  @Override
  public int getGroupCount() {
    return listGroup.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return Objects.requireNonNull(this.listItem.get(this.listGroup.get(groupPosition))).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return this.listGroup.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return Objects.requireNonNull(this.listItem.get(this.listGroup.get(groupPosition)))
        .get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @SuppressLint("InflateParams")
  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
      ViewGroup parent) {
    String group = (String) getGroup(groupPosition);
    if (convertView == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.list_group, null);
    }

    AppCompatTextView listParent = convertView.findViewById(R.id.list_parent);
    listParent.setText(group);

    return convertView;
  }

  @SuppressLint("InflateParams")
  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {
    String child = (String) getChild(groupPosition, childPosition);

    if (convertView == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.list_item, null);
    }

    AppCompatTextView listChild = convertView.findViewById(R.id.list_child);
    listChild.setText(child);

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}
