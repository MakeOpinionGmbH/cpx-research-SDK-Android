package com.makeopinion.javademo;

import android.app.Application;

import androidx.annotation.NonNull;

import com.makeopinion.cpxresearchlib.CPXResearch;
import com.makeopinion.cpxresearchlib.models.CPXConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder;
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyPosition;

public class CPXApplication extends Application {

    private CPXResearch cpxResearch;

    @Override
    public void onCreate() {
        super.onCreate();
        initCPX();
    }

    @NonNull
    public CPXResearch getCpxResearch() {
        return cpxResearch;
    }

    private void initCPX() {
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.CornerBottomRight,
                "Earn up to<br> 3 Coins",
                20,
                "#ffffff",
                "#ffaf20",
                true);

        CPXConfiguration config = new CPXConfigurationBuilder("5878",
                "1",
                "secureHash",
                style)
                .build();

        cpxResearch = CPXResearch.Companion.init(config);
    }
}
