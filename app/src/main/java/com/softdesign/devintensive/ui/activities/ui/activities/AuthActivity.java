package com.softdesign.devintensive.ui.activities.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.data.managers.DataManager;
import com.softdesign.devintensive.ui.activities.data.network.req.UserLoginReq;
import com.softdesign.devintensive.ui.activities.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.activities.data.network.res.UserModelRes;
import com.softdesign.devintensive.ui.activities.data.storage.models.Repository;
import com.softdesign.devintensive.ui.activities.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.ui.activities.data.storage.models.User;
import com.softdesign.devintensive.ui.activities.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.activities.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.activities.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.activities.utils.AppConfig;
import com.softdesign.devintensive.ui.activities.utils.ConstantManager;
import com.softdesign.devintensive.ui.activities.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private Button mSignin;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;

    public AuthActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mSignin = (Button) findViewById(R.id.login_btn);
        mLogin = (EditText) findViewById(R.id.login_email_et);
        mPassword = (EditText) findViewById(R.id.login_password_et);
        mRememberPassword = (TextView) findViewById(R.id.remember_txt);

        mRememberPassword.setOnClickListener(this);
        mSignin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.login_btn:
                signIn();
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar( String message ) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess( UserModelRes userModel ) {
        showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserData(userModel);
        saveUserPhotos(userModel);

        saveUserInDb();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent loginIntent = new Intent(this, MainActivity.class);
                Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);
    }

    private void signIn() {
        if(NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль");
                    } else {
                        showSnackbar("Ошибка!");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // todo: Обработать ошибки ретрофита
                }
            });
        } else {
            showSnackbar("Сеть на данный момент не доступна, попробуйте позже.");
        }
    }

    /**
     * Получим рейтинг, строки кода и проекты из модели
     * @param userModel
     */
    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRaiting(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    /**
     * Получим все остальные данные из модели
     * @param userModel
     */
    private void saveUserData( UserModelRes userModel ) {
        List<String> userData = new ArrayList<>();

        userData.add(userModel.getData().getUser().getContactsData().getPhone());
        userData.add(userModel.getData().getUser().getContactsData().getEmail());
        userData.add(userModel.getData().getUser().getContactsData().getVk());
        userData.add(userModel.getData().getUser().getRepositories().getSingleRepoLink());
        userData.add(userModel.getData().getUser().getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileData(userData);

        String userFirstName = userModel.getData().getUser().getFirstName();
        String userSecondName = userModel.getData().getUser().getSecondName();

        mDataManager.getPreferencesManager().saveUserName(userFirstName, userSecondName);
    }

    private void saveUserPhotos( UserModelRes userModel ) {
        Uri avatar = Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar());
        Uri photo = Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto());

        mDataManager.getPreferencesManager().saveUserPhoto(photo);
        mDataManager.getPreferencesManager().saveUserAvatar(avatar);
    }

    private void saveUserInDb() {
        Call<UserListRes> call = mDataManager.getUserListFromNetwork();

        call.enqueue(new Callback<UserListRes>() {

            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {

                        List<Repository> allRepositories = new ArrayList<Repository>();
                        List<User> allUsers = new ArrayList<User>();

                        for (UserListRes.UserData userRes : response.body().getData()) {
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }

                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);

                    } else {
                        showSnackbar("Список пользователей не может быть получен!");
                        Log.e(TAG, "OnResponse: " + String.valueOf(response.errorBody().source()));
                    }
                } catch( Exception e ) {
                    e.printStackTrace();
                    showSnackbar("Произошла ошибка в методе saveUserInDb");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // TODO: обработать ошибки
            }
        });
    }

    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userId));
        }

        return repositories;
    }
}
