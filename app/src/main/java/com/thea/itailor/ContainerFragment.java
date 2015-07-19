package com.thea.itailor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.thea.itailor.db.ClothDao;
import com.thea.itailor.db.ClothSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ContainerFragment extends Fragment {
    private static final String TAG = "ContainerFragment";

    private static final String PATTERN = "pattern";

    private LayoutInflater inflater;

    private ExpandableListView listView;
    private ScrollView scrollView;

    private static ClothDao dao;
    private static List<String> groups = new ArrayList<>();
    private static List<List<String>> children = new ArrayList<>();
    private static List<List<String>> filenames = new ArrayList<>();

    private static MyExpandableAdapter expandableAdapter;

    private ImageLoader imageLoader;

    public static ContainerFragment newInstance(String pattern) {
        ContainerFragment fragment = new ContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATTERN, pattern);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ContainerFragment() {
    }

    public String getPattern() {
        return getArguments().getString(PATTERN, Constant.LIST_PATTERN);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dao = new ClothDao(new ClothSQLiteOpenHelper(activity));
        if (dao.findGroups().isEmpty())
            dao.add("最新", null, null, null, new Date());
        initData();
    }

    public void initData() {
        groups.clear();
        children.clear();
        filenames.clear();
        groups.addAll(dao.findGroups());
        for (int i = 0; i < groups.size(); i++) {
            children.add(dao.findChildrenAndFilename(groups.get(i)).get(0));
            filenames.add(dao.findChildrenAndFilename(groups.get(i)).get(1));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        if (getPattern().equalsIgnoreCase(Constant.LIST_PATTERN)) {
            listView = (ExpandableListView)
                    inflater.inflate(R.layout.list_armoire, container, false);
            expandableAdapter = new MyExpandableAdapter(inflater);
            listView.setAdapter(expandableAdapter);
            listView.setOnChildClickListener(mChildClickListener);
            listView.setOnItemLongClickListener(mItemLongClickListener);
            return listView;
        }
        else {
            scrollView = (ScrollView) inflater.inflate(R.layout.scroll_view, container, false);
            return scrollView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        if (getPattern().equalsIgnoreCase(Constant.LIST_PATTERN))
            expandableAdapter.notifyDataSetChanged();
        else {
            LinearLayout ll = (LinearLayout) scrollView.findViewById(R.id.ll_images);
            ll.removeAllViews();
            for (int i = 0; i < groups.size(); i++) {
                LinearLayout groupLayout = (LinearLayout)
                        inflater.inflate(R.layout.picture_group, ll, false);
                TextView groupTitle = (TextView) groupLayout.findViewById(R.id.tv_group_name);
                groupTitle.setText(groups.get(i));
                if (children.get(i).size() == 0)
                    continue;

                GridView gridView = (GridView) groupLayout.findViewById(R.id.gv_images);
                gridView.setAdapter(new ImageAdapter(inflater, children.get(i), filenames.get(i)));
                gridView.setOnItemClickListener(mItemClickListener);

                ll.addView(groupLayout);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageLoader.destroy();
    }

    private OnChildClickListener mChildClickListener = new OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            Intent intent = new Intent(getActivity(), ShowBodyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            return false;
        }
    };

    private OnItemLongClickListener mItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final int groupPosition = (int) view.getTag(R.string.group_position);
            final int childPosition = (int) view.getTag(R.string.child_position);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            CharSequence[] dialogMenu = childPosition == -1
                    ? getResources().getTextArray(R.array.group_edit)
                    : getResources().getTextArray(R.array.child_edit);
            builder.setItems(dialogMenu, new MyDialogOnClickListener(
                    getActivity(), groupPosition, childPosition)).show();
            return true;
        }
    };

    //点击图片监听事件
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), ShowBodyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    };

    public static class MyDialogOnClickListener implements DialogInterface.OnClickListener {
        private Context context;
        private int groupPosition;
        private int childPosition;

        public MyDialogOnClickListener(Context context, int groupPosition, int childPosition) {
            this.context = context;
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == 0)
                rename(groupPosition, childPosition);
            else if (which == 2)
                delete(groupPosition, childPosition);
            else if (childPosition == -1 && which == 1)
                addNew();
            else if (childPosition != -1 && which == 1)
                moveTo(groupPosition, childPosition);
        }

        public void rename(final int groupPosition, final int childPosition) {
            final EditText name = new EditText(context);
            name.setText(getText(groupPosition, childPosition));
            name.setEnabled(true);
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            name.setSelection(name.getText().length());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("重命名").setView(name).setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setText(groupPosition, childPosition, name.getText().toString());
                            expandableAdapter.notifyDataSetChanged();
                        }
                    }).show();
        }

        public void delete(final int groupPosition, final int childPosition) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("确定删除吗？").setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem(groupPosition, childPosition);
                            expandableAdapter.notifyDataSetChanged();
                        }
                    }).show();
        }

        public void addNew() {
            final EditText name = new EditText(context);
            name.setEnabled(true);
            name.setInputType(InputType.TYPE_CLASS_TEXT);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新建组名").setView(name).setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String groupname = name.getText().toString();
                            if (groups.contains(groupname)) {
                                Toast.makeText(context, R.string.error_same_name, Toast.LENGTH_LONG).show();
                                return;
                            }
                            dao.add(groupname, null, null, null, new Date());
                            groups.add(groupname);
                            children.add(new ArrayList<String>());
                            filenames.add(new ArrayList<String>());
                            expandableAdapter.notifyDataSetChanged();
                        }
                    }).show();
        }

        public void moveTo(final int groupPosition, final int childPosition) {
            String[] items = new String[groups.size()];
            groups.toArray(items);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dao.moveTo(groups.get(which), filenames.get(groupPosition).get(childPosition));
                    children.get(which).add(0, children.get(groupPosition).get(childPosition));
                    children.get(groupPosition).remove(childPosition);
                    filenames.get(which).add(0, filenames.get(groupPosition).get(childPosition));
                    filenames.get(groupPosition).remove(childPosition);
                    expandableAdapter.notifyDataSetChanged();
                }
            }).show();
        }

        public String getText(int groupPosition, int childPosition) {
            return childPosition == -1 ? groups.get(groupPosition)
                    : children.get(groupPosition).get(childPosition);
        }

        public void setText(int groupPosition, int childPosition, String text) {
            if (childPosition == -1) {
                if (groups.contains(text)) {
                    Toast.makeText(context, R.string.error_same_name, Toast.LENGTH_LONG).show();
                    return;
                }
                dao.updateGroup(text, groups.get(groupPosition));
                groups.set(groupPosition, text);
            }
            else {
                for (int i = 0; i < children.size(); i++) {
                    if (children.get(i).contains(text)) {
                        Toast.makeText(context, R.string.error_same_name, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                dao.updateRemark(text, filenames.get(groupPosition).get(childPosition));
                children.get(groupPosition).set(childPosition, text);
            }
        }

        public void deleteItem(int groupPosition, int childPosition) {
            if (childPosition == -1) {
                dao.delete(1, groups.remove(groupPosition));
                children.remove(groupPosition);
                filenames.remove(groupPosition);
            }
            else {
                dao.delete(3, filenames.get(groupPosition).remove(childPosition));
                children.get(groupPosition).remove(childPosition);
            }
        }
    }

    public class MyExpandableAdapter extends BaseExpandableListAdapter {
        private LayoutInflater inflater;
        private DisplayImageOptions options;
        private ImageLoadingListener imageLoadingListener = new AnimateFirstDisplayListener();

        public MyExpandableAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
            options = new DisplayImageOptions.Builder()
                    .considerExifParams(true)
                    .build();
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children.get(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition*100 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_group, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.tv_group_name);
            name.setText(getGroup(groupPosition));
            TextView count = (TextView) convertView.findViewById(R.id.tv_group_count);
            count.setText(getChildrenCount(groupPosition) + "");
            convertView.setTag(R.string.group_position, groupPosition);
            convertView.setTag(R.string.child_position, -1);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_cloth, parent, false);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv_cloth);
                holder.tv = (TextView) view.findViewById(R.id.tv_cloth_name);
                view.setTag(holder);
            }
            else
                holder = (ViewHolder) view.getTag();

            String uri = "file://" + Constant.PATH + Constant.DIRECTORY_ARMOIRE +
                    filenames.get(groupPosition).get(childPosition);
            imageLoader.displayImage(uri, holder.iv, options, imageLoadingListener);
            holder.tv.setText(getChild(groupPosition, childPosition));
            view.setTag(R.string.group_position, groupPosition);
            view.setTag(R.string.child_position, childPosition);
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages =
                Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        private DisplayImageOptions options;

        private List<String> children;
        private List<String> filenames;

        public ImageAdapter(LayoutInflater inflater, List<String> children, List<String> filenames) {
            this.inflater = inflater;
            this.children = children;
            this.filenames = filenames;
            options = new DisplayImageOptions.Builder()
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return children.size();
        }

        @Override
        public String getItem(int position) {
            return children.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (convertView == null) {
                view = inflater.inflate(R.layout.image_text, parent, false);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv_image_cloth);
                holder.tv = (TextView) view.findViewById(R.id.tv_image_description);
                view.setTag(holder);
            }
            else
                holder = (ViewHolder) view.getTag();

            String uri = "file://" + Constant.PATH + Constant.DIRECTORY_ARMOIRE +
                    filenames.get(position);
            imageLoader.displayImage(uri, holder.iv, options, new AnimateFirstDisplayListener());
            holder.tv.setText(children.get(position));
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
