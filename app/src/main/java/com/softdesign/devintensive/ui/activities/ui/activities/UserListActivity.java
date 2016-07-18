package com.softdesign.devintensive.ui.activities.ui.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.data.managers.DataManager;
import com.softdesign.devintensive.ui.activities.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.activities.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.activities.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.activities.ui.fragments.RetainedFragment;
import com.softdesign.devintensive.ui.activities.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.ui.activities.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUsers;

    private RetainedFragment dataFragment;

    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        showProgress();

        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        setupToolbar();
        setupDrawer();

        FragmentManager fragmentManager = getFragmentManager();
        dataFragment = (RetainedFragment) fragmentManager.findFragmentByTag("mData");


        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(dataFragment, "mData").commit();
            loadUsers();
        } else {
            mUsers = dataFragment.getData();
            setNewAdapter(mUsers);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataFragment.setData(mUsers);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar( String message ) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsers() {
        Call<UserListRes> call = mDataManager.getUserList();

        call.enqueue(new Callback<UserListRes>() {

            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
                        mUsers = response.body().getData();
                        setNewAdapter(mUsers);
                    } else {
                        showSnackbar("Ошибка!");
                        Log.e(TAG, "Ошибка");
                    }
                } catch( Exception e ) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // TODO: обработать ошибки
            }
        });
    }

    protected void setNewAdapter( List<UserListRes.UserData> users ) {
        mUsersAdapter = new UsersAdapter(users, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

                startActivity(profileIntent);
            }
        });
        mRecyclerView.setAdapter(mUsersAdapter);
        hideProgress();
    }

    private void setupDrawer() {
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if( actionBar != null ) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

     /* Search */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        List<UserListRes.UserData> selectedUsers = new ArrayList<>();
        UserListRes.UserData currentUser;

        for( int i = 0; i < mUsers.size(); i++ ) {

            currentUser = mUsers.get(i);

            if( currentUser.getFullName().contains(query) ) {
                selectedUsers.add(currentUser);
            }
        }

        setNewAdapter(selectedUsers);

        return selectedUsers.size() != 0;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<UserListRes.UserData> selectedUsers = new ArrayList<>();
        UserListRes.UserData currentUser;

        for( int i = 0; i < mUsers.size(); i++ ) {

            currentUser = mUsers.get(i);

            if( currentUser.getFullName().contains(newText) ) {
                selectedUsers.add(currentUser);
            }
        }

        setNewAdapter(selectedUsers);

        return selectedUsers.size() != 0;
    }


}
