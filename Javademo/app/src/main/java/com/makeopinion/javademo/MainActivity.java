package com.makeopinion.javademo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.makeopinion.cpxresearchlib.CPXResearchListener;
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXCardStyle;
import com.makeopinion.cpxresearchlib.models.SurveyItem;
import com.makeopinion.cpxresearchlib.models.TransactionItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CPXResearchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup parentView = findViewById(R.id.ll_container);

        CPXApplication app = (CPXApplication) getApplication();
        app.getCpxResearch().registerListener(this);
        app.getCpxResearch().setSurveyVisibleIfAvailable(true, this);

        CPXCardConfiguration cardConfig = new CPXCardConfiguration.Builder()
                .accentColor(Color.parseColor("#4800AA"))
                .backgroundColor(Color.parseColor("#FFFFFF"))
                .starColor(Color.parseColor("#FFAA00"))
                .inactiveStarColor(Color.parseColor("#838393"))
                .textColor(Color.parseColor("#8E8E93"))
                .dividerColor(Color.parseColor("#5A7DFE"))
                .cornerRadius(4f)
                .cpxCardStyle(CPXCardStyle.SMALL)
                .fixedCPXCardWidth(146)
                .currencyPrefixImage(R.drawable.cpx_icon_star) // set your currency image here!!!
                .hideCurrencyName(true)
                .build();

        app.getCpxResearch().insertCPXResearchCardsIntoContainer(this, parentView, cardConfig);
    }

    @Override
    public void onSurveysDidClose() {
        Log.d("CPXDEMO", "Surveys did close.");
    }

    @Override
    public void onSurveysDidOpen() {
        Log.d("CPXDEMO", "Surveys did open.");
    }

    @Override
    public void onSurveysUpdated() {
        CPXApplication app = (CPXApplication) getApplication();
        List<SurveyItem> surveys = app.getCpxResearch().getSurveys();
        Log.d("CPXDEMO", "Surveys updated: " + surveys);
    }

    @Override
    public void onSurveyDidClose() { Log.d("CPXDEMO", "Single survey closed."); }

    @Override
    public void onSurveyDidOpen() { Log.d("CPXDEMO", "Single survey opened."); }

    @Override
    public void onTransactionsUpdated(List<TransactionItem> unpaidTransactions) {
        Log.d("CPXDEMO", String.format("Transactions updated with %d items", unpaidTransactions.size()));
    }
}