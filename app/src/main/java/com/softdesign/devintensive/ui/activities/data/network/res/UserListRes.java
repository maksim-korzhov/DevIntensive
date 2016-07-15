package com.softdesign.devintensive.ui.activities.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserListRes {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    private List<UserData> data = new ArrayList<UserData>();

    public List<UserData> getData() {
        return data;
    }


    public class UserData {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("second_name")
        @Expose
        private String secondName;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("repositories")
        @Expose
        private UserModelRes.Repositories repositories;
        @SerializedName("profileValues")
        @Expose
        private UserModelRes.ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private UserModelRes.PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        public String specialization;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
        }

        public UserModelRes.Repositories getRepositories() {
            return repositories;
        }

        public UserModelRes.ProfileValues getProfileValues() {
            return profileValues;
        }

        public UserModelRes.PublicInfo getPublicInfo() {
            return publicInfo;
        }

        public String getSpecialization() {
            return specialization;
        }

        public String getUpdated() {
            return updated;
        }

        public Integer getV() {
            return v;
        }

        public String getFullName() {
            return String.format("%s %s", firstName, secondName);
        }
    }
}
