package com.softdesign.devintensive.ui.activities.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.data.managers.DataManager;
import com.softdesign.devintensive.ui.activities.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrectEditorMode = 0;

    private DataManager mDataManager;
    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;

    private AppBarLayout mAppBarLayout;
    private AppBarLayout.LayoutParams mAppBarParams = null;

    private LinearLayout mGrayBlock;

    private List<EditText> mUserInfoViews;

    private TextView mUserValueRating, mUserValueCodeLines, mUserValueProjects;
    private List<TextView> mUserValueViews;

    private File mPhotoFile = null;
    private Uri mSelectedimage = null;
    private ImageView mProfileImage;
    private ImageView mProfileAvatar;

    private ImageView mCallTrigger;
    private ImageView mEmailTrigger;
    private ImageView mVkTrigger;
    private ImageView mGithubTrigger;

    private TextView mUserName, mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);

        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserBio = (EditText) findViewById(R.id.about_et);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mUserValueRating = (TextView) findViewById(R.id.user_rait_value_txt);
        mUserValueCodeLines = (TextView) findViewById(R.id.user_code_lines_txt);
        mUserValueProjects = (TextView) findViewById(R.id.user_projects_txt);

        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodeLines);
        mUserValueViews.add(mUserValueProjects);

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        mCallTrigger = (ImageView) findViewById(R.id.call_trigger);
        mEmailTrigger = (ImageView) findViewById(R.id.email_trigger);
        mVkTrigger = (ImageView) findViewById(R.id.vk_trigger);
        mGithubTrigger = (ImageView) findViewById(R.id.github_trigger);

        /* Setup intent triggers */
        mCallTrigger.setOnClickListener(this);
        mEmailTrigger.setOnClickListener(this);
        mVkTrigger.setOnClickListener(this);
        mGithubTrigger.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_photo) // TODO: 07.07.2016 Сделать placeholder + transform + crop
                .into(mProfileImage);

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserAvatar())
                .placeholder(R.drawable.maks_120) // TODO: 07.07.2016 Сделать placeholder + transform + crop
                .fit()
                .into(mProfileAvatar);

        List<String> test = mDataManager.getPreferencesManager().loadUserProfileData();

        if( savedInstanceState == null ) {
            // активити запущено впервые
            //showSnackbar("Activity запускается впервые");
        } else {
            mCurrectEditorMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrectEditorMode);
        }
    }

    /**
     * Обработка клика по элементу меню
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ) {
            // Указываем, с какой стороны он должен открываться
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
        //saveUserFields();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG, "onStart");
    }

    /**
     * Обработка события onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.fab:
                if( mCurrectEditorMode == 0 ) {
                    changeEditMode(1);
                    mCurrectEditorMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrectEditorMode = 0;
                }
                break;

            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            case R.id.call_trigger:
                startCallIntent();
                break;

            case R.id.email_trigger:
                startEmailIntent();
                break;

            case R.id.github_trigger:
            case R.id.vk_trigger:
                startBrowserIntent();
                break;
        }
    }

    private void startEmailIntent() {
        String email = mUserMail.getText().toString();

        if (email.equals("null")) {
            showSnackbar(getString(R.string.error_user_profile_email_empty));
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            startActivity(intent);
        }
    }


    private void startCallIntent() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            String callPhone = mUserPhone.getText().toString();

            if (callPhone.equals("null")) {
                showSnackbar(getString(R.string.error_user_profile_phone_empty));
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + callPhone));
                startActivity(intent);
            }
        }
    }

    private void startBrowserIntent() {
        String stringUri = mUserGit.getText().toString();

        if( stringUri.equals("null") ) {
            showSnackbar(getString(R.string.error_user_profile_link_empty));
        } else {
            Uri uri = Uri.parse(stringUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    //

    /**
     * Обработка нажатия кнопки назад(закрываем Drawer)
     */
    @Override
    public void onBackPressed() {
        if( mNavigationDrawer.isDrawerOpen(GravityCompat.START) ) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Сохранение данных
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrectEditorMode);
    }

    /**
     * Показываем Snackbar
     * @param message
     */
    private void showSnackbar( String message ) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настройка toolbar
     */
    private void setupToolbar() {
        // Заменим actionbar на toolbar, он более гибкий и позволяет настраивать себя
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        if( actionBar != null ) {
            // Установим картинку главной кнопки
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            // отображать кнопку в actionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Создание Navigation Drawer. Обработка нажатий на кнопки меню
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);

        mUserName = (TextView)header.findViewById(R.id.user_name_txt);
        mUserEmail = (TextView)header.findViewById(R.id.user_email_txt);
        mProfileAvatar = (ImageView) header.findViewById(R.id.small_rounded_user_icon);


        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    showSnackbar(item.getTitle().toString());
                    item.setChecked(true);
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }


    }

    /**
     * Получение результата из другой ACTIVITY(фото из камеры или из галереи)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if( resultCode == RESULT_OK && data != null ) {
                    mSelectedimage = data.getData();

                    insertProfileImage(mSelectedimage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if( resultCode == RESULT_OK && mPhotoFile != null ) {
                    mSelectedimage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedimage);
                }
                break;
        }
    }

    /**
     * переключает режим редактирования
     * @param mode если 1 - режим редактирования, 0 - режим просмотра
     */
    private void changeEditMode( int mode ) {
        if( mode == 1 ) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }

            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

            }

            hideProfilePlaceholder();
            unlockToolbar();
            //saveUserFields();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * Загрузка информации о пользователе
     */
    private void initUserFields() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }

        // Init data for navigation drawer header
        mUserEmail.setText(userData.get(1));

        List<String> userNames = mDataManager.getPreferencesManager().loadUserName();
        String concatName = userNames.get(0) + " " + userNames.get(1);
        mUserName.setText(concatName);
    }

    /**
     * Сохранение информации о пользователе
     */
    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 06.07.2016 обработать ошибку
            }

            if (mPhotoFile != null) {
                // TODO: 06.07.2016 передать фотофайл в интент
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы приложения необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                .setAction("Разрешить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                    }
                }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if( requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2 ) {
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                // TODO 7.07.2016
            }

            if( grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                // TODO 7.07.2016
            }
        }
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectedItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectedItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch( choiceItem ) {
                            case 0:
                                // TODO 6.07.2016 Выбрать из галереи
                                loadPhotoFromGallery();
                                //showSnackbar("Выбрать из галереи");
                                break;
                            case 1:
                                // TODO 6.07.2016 Сделать снимок
                                loadPhotoFromCamera();
                                //showSnackbar("Сделать снимок");
                                break;
                            case 2:
                                // TODO 6.07.2016 Отменить
                                dialog.cancel();
                                //showSnackbar("Отменить");
                                break;
                        }
                    }
                });

                return builder.create();
            default:
                return null;
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Метод вставки изображения в Toolbar
     * @param selectedimage
     */
    private void insertProfileImage(Uri selectedimage) {
        Picasso.with(this)
                .load(selectedimage)
                .into(mProfileImage);

        if (!selectedimage.equals(mDataManager.getPreferencesManager().loadUserPhoto())) {
            mDataManager.getPreferencesManager().saveUserPhoto(mSelectedimage);

            // upload to server
            uploadPhoto(new File(selectedimage.getPath()));
        }
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Загружает фотографию из профиля пользователя на сервер
     * @param photoFile представление файла фотографии
     */
    private void uploadPhoto(File photoFile) {
        Call<ResponseBody> call = mDataManager.uploadPhoto(photoFile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("TAG", "Upload success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackbar("Не удалось загрузить фотографию на сервер");
                Log.e("TAG", t.getMessage());
            }
        });
    }
}
