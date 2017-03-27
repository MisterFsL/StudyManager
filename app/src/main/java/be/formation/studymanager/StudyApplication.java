package be.formation.studymanager;

import android.app.Application;


public class StudyApplication extends Application {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
}
