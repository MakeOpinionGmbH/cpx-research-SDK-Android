package com.makeopinion.javademo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.makeopinion.cpxresearchlib.CPXResearchListener;
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration;
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

        CPXCardConfiguration cardConfig = new CPXCardConfiguration(
                Color.parseColor("#41d7e5"),
                Color.WHITE,
                Color.parseColor("#dfdfdf"),
                Color.parseColor("#ffaa00"),
                Color.DKGRAY,
                3);

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
        Log.d("CPXDEMO", "Surveys updated.");
    }

    @Override
    public void onTransactionsUpdated(List<TransactionItem> unpaidTransactions) {
        Log.d("CPXDEMO", String.format("Transactions updated with %d items", unpaidTransactions.size()));
    }

    @Override
    public void onSurveyDidClose() { Log.d("CPXDEMO", "Single survey closed."); }

    @Override
    public void onSurveyDidOpen() { Log.d("CPXDEMO", "Single survey opened."); }
}