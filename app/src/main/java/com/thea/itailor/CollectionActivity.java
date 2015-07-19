package com.thea.itailor;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.TintImageView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thea.itailor.entities.Collection;
import com.thea.itailor.db.CollectionDao;
import com.thea.itailor.db.CollectionSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends ActionBarActivity {
    private static final String TAG = "CollectionActivity";

    private CollectionDao dao;
    private List<Collection> collections = new ArrayList<>();
    private List<Collection> searchCollections = new ArrayList<>();

    private ListView listView;
    private CollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dao = new CollectionDao(new CollectionSQLiteOpenHelper(this));
        collections = dao.findAll();
        listView = (ListView) findViewById(R.id.lv_collection);
        mAdapter = new CollectionAdapter();
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        initSearchView(searchView);
        return true;
    }

    public void initSearchView(SearchView searchView) {
        searchView.findViewById(R.id.search_plate)
                .setBackgroundResource(R.drawable.sv_et_background);
        SearchView.SearchAutoComplete mEdit = (SearchView.SearchAutoComplete)
                searchView.findViewById(R.id.search_src_text);
        mEdit.setHint(getString(R.string.search_hint));
        TintImageView searchButton = (TintImageView) searchView.findViewById(R.id.search_go_btn);
        searchButton.setImageResource(R.mipmap.ic_action_search_white);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "submit");
                collections = dao.search(query);
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public class CollectionAdapter extends BaseAdapter {
        private int count = 10;

        @Override
        public int getCount() {
            return Math.min(count, collections.size());
        }

        @Override
        public Collection getItem(int position) {
            return collections.get(position);
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
                view = getLayoutInflater().inflate(R.layout.item_collection, parent, false);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv_collection_image);
                holder.tv = (TextView) view.findViewById(R.id.tv_collection_description);
                view.setTag(holder);
            }
            else
                holder = (ViewHolder) view.getTag();

            holder.iv.setImageBitmap(collections.get(position).getImageBitmap());
            holder.tv.setText(collections.get(position).getDescription());
            return view;
        }

        public void setCount(int count) {
            this.count = count;
            this.notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
