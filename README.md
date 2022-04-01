# CPX Research Android SDK

#### Monetize your product with fun surveys.

We will make it easy for you: Simply implement our solution and be ready to start monetizing your product immediately!
Let users earn your virtual currency by simply participating in exciting and well paid online surveys!

This SDK is owned by [MakeOpinion GmbH](http://www.makeopinion.com).

[Learn more.](https://cpx-research.com/)

# Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [ProGuard](#proguard)
- [Logging](#logging)

# Prerequisites

- Android SDK 21 (Lollipop) or newer

# Preview

![Unknown](https://user-images.githubusercontent.com/7074507/137196166-568c2e90-dc80-49e8-bd38-e20bdbee4d2c.png)

# Installation

1. Add it in your **root** build.gradle at the end of repositories:

```gradle
	allprojects {
		repositories {

			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add it in your **module** build.gradle in the dependencies section:

```gradle
dependencies {
	implementation 'com.github.MakeOpinionGmbH:cpx-research-SDK-Android:1.4.6'
}
```

# Usage

## Initialize the library

Enter the following code early in your App's life cycle, for example in the App's Application class. You can extra information like subIds, email or an entire array of specific data that will be sent back to you when you finish a transaction. For Kotlin

```kotlin
val style = CPXStyleConfiguration(
            SurveyPosition.SideRightNormal,
            "Earn up to 3 Coins in<br> 4 minutes with surveys",
            20,
            "#ffffff",
            "ffaf20",
            true)

val config = CPXConfigurationBuilder(
            "<Your app id>",
            "<Your external user id>",
            "<Your secure hash>",
            style)
            //.withSubId1("subId1")
            //.withSubId2("subId2")
            //.withEmail("user@email.com")
            //.withExtraInfo(arrayOf("value1", "value2"))
            //.withCustomConfirmCloseDialogTexts("Title", "msg", "ok", "cancel")
            .build()

val cpx = CPXResearch.init(config)
```

or for Java

```java
CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideRightNormal,
        "Earn up to 3 Coins in<br> 4 minutes with surveys",
        20,
        "#ffffff",
        "#ffaf20",
        true);

CPXConfiguration config = new CPXConfigurationBuilder("<Your app id>",
        "<Your external user id>",
        "<Your secure hash>",
        style)
        //.withSubId1("subId1")
        //.withSubId2("subId2")
        //.withEmail("user@email.com")
        //.withExtraInfo(new String[]{"value1", "value2"})
        //.withCustomConfirmCloseDialogTexts("Title", "msg", "ok", "cancel")
        .build();

cpxResearch = CPXResearch.Companion.init(config);
```

## Using the SDK's banner for overlays

In your Activity activate the automatic banner display and set the delegate to handle CPX Research events. For Kotlin

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as? CPXApplication)?.let {
            it.cpxResearch().setSurveyVisibleIfAvailable(true, this)
        }
    }
```

or Java

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    CPXApplication app = (CPXApplication) getApplication();
    app.getCpxResearch().setSurveyVisibleIfAvailable(true, this);
}
```

## Using the CPX Survey Cards

To use a recycler view with a default survey card you can get a fully prepared RecyclerView from the SDK. Add this to a parent view on your activity. The recycler view also handles click and update events for you.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentView = findViewById(R.id.container)

        val cardConfig = CPXCardConfiguration.Builder()
                    .build() //don't set anything, just use default values

        // OR

        val cardConfig = CPXCardConfiguration.Builder()
                    .accentColor(Color.parseColor("#41d7e5")) // color of amount and currency
                    .backgroundColor(Color.WHITE) // background of the card
                    .starColor(Color.parseColor("#ffaa00")) // color for active stars
                    .inactiveStarColor(Color.parseColor("#dfdfdf")) // color for inactive stars
                    .textColor(Color.DKGRAY) // text color for survey time
                    .promotionAmountColor(Color.RED) // text color for promotion offers
                    .cardsOnScreen(4) // set how many cards are initially visible on screen
                    .cornerRadius(4f) // set the corner radius of cards
                    .maximumSurveys(4) // set maximum amount of surveys showns
                    .paddingHorizontal(16f) // set left/right
                    //.paddingVertical(16f) // set top/bottom
                    //.padding(16f) // set all borders
                    //.paddingLeft(16f) // just set the left
                    //.paddingRight(16f) // just set the right
                    //.paddingTop(16f) // just set the top
                    //.paddingBottom(16f) //just set the bottom
                    .build()

        (application as? CPXApplication)?.let {
            it.cpxResearch().insertCPXResearchCardsIntoContainer(this, parentView, cardConfig)
        }
}
```
or Java

```java
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup parentView = findViewById(R.id.ll_container);

        CPXApplication app = (CPXApplication) getApplication();

        CPXCardConfiguration cardConfig = new CPXCardConfiguration.Builder()
                .build(); //don't set anything, just use default values

        // OR

        CPXCardConfiguration cardConfig = new CPXCardConfiguration.Builder()
                .accentColor(Color.parseColor("#41d7e5")) // color of amount and currency
                .backgroundColor(Color.WHITE) // background of the card
                .starColor(Color.parseColor("#ffaa00")) // color for active stars
                .inactiveStarColor(Color.parseColor("#dfdfdf")) // color for inactive stars
                .textColor(Color.DKGRAY) // text color for survey time
                .promotionAmountColor(Color.RED) // text color for promotion offers
                .cardsOnScreen(4) // set how many cards are initially visible on screen
                .cornerRadius(4f) // set the corner radius of cards
                .maximumSurveys(4) // set maximum amount of surveys showns
                .paddingHorizontal(16f) // set left/right
                //.paddingVertical(16f) // set top/bottom
                //.padding(16f) // set all borders
                //.paddingLeft(16f) // just set the left
                //.paddingRight(16f) // just set the right
                //.paddingTop(16f) // just set the top
                //.paddingBottom(16f) //just set the bottom
                .build();

        app.getCpxResearch().insertCPXResearchCardsIntoContainer(this, parentView, cardConfig);
    }
```

## Handling events of the SDK

In your Activity set the delegate to handle CPX Research events. In Kotlin

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as? CPXApplication)?.let {
            it.cpxResearch().registerListener(object : CPXResearchListener {
                override fun onSurveysUpdated() {
                    Log.d("CPX", "surveys updated.")
                    // get current surveys if needed with it.cpxResearch().surveys
                }

                override fun onTransactionsUpdated(unpaidTransactions: List<TransactionItem>) {
                    Log.d("CPX", "transactions updated.")
                }

                override fun onSurveysDidOpen() {
                    Log.d("CPX", "surveys opened.")
                }

                override fun onSurveysDidClose() {
                    Log.d("CPX", "surveys closed.")
                }

                override fun onSurveyDidOpen() {
                    Log.d("CPX", "single survey opened.")
                }

                override fun onSurveyDidClose() {
                    Log.d("CPX", "single survey closed.")
                }
            })
        }
    }
```

or Java

```java
app.getCpxResearch().registerListener(new CPXResearchListener() {
    @Override
    public void onSurveysUpdated() {
        // get current surveys if needed with app.getCpxResearch().getSurveys()
    }

    @Override
    public void onTransactionsUpdated(List<TransactionItem> unpaidTransactions) {

    }

    @Override
    public void onSurveysDidOpen() {

    }

    @Override
    public void onSurveysDidClose() {

    }

    @Override
    public void onSurveyDidOpen() {

    }

    @Override
    public void onSurveyDidClose() {

    }
});
```

## Additional functions of the SDK

Tell the library to show the surveys list, call

```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().openSurvey(this)
}
```

```java
((CPXApplication) getApplication()).getCpxResearch().openSurveyList(this);
```

Show a specific survey

```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().openSurvey(this, surveyId)
}
```

```java
((CPXApplication) getApplication()).getCpxResearch().openSurvey(this, surveyId);
```

Mark a transaction as paid

```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().markTransactionAsPaid(transactionId, messageId)
}
```

```java
((CPXApplication) getApplication()).getCpxResearch().markTransactionAsPaid(transactionId, messageId);
```

Manually request an update to the surveys
```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().requestSurveyUpdate(false) // set parameter to true if you also want to update the unpaid transactions
}
```

```java
((CPXApplication) getApplication()).getCpxResearch().requestSurveyUpdate(false); // set parameter to true if you also want to update the unpaid transactions
```

Access all current surveys for the user
```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    val surveys = app.cpxResearch().surveys
}
```

```java
List<SurveyItem> surveys = ((CPXApplication) getApplication()).getCpxResearch().getSurveys();
```

Access all current unpaid transactions for the user
```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    val transactions = app.cpxResearch().unpaidTransactions
}
```

```java
List<TransactionItem> transactions = ((CPXApplication) getApplication()).getCpxResearch().getUnpaidTransactions();
```

## How to add an own application class

1. Create a class extending Application class like in Kotlin

```kotlin
class CPXApplication: Application() { }
```

or Java

```java
public class CPXApplication extends Application { }
```

2. Add this Application-class to your App's Manifest

```xml
<application
    android:name=".CPXApplication"
    ...
</application>
```

3. In your Application class overwrite the onCreate Method and add the variables/getter.
   For Kotlin

```kotlin
override fun onCreate() {
    super.onCreate()
    initCPX(this)
}

private var cpxResearch: CPXResearch? = null

fun cpxResearch(): CPXResearch {
    return cpxResearch!!
}

private fun initCPX(context: Context) {
    val style = CPXStyleConfiguration(
        SurveyPosition.SideRightNormal,
        "Earn up to 3 Coins in<br> 4 minutes with surveys",
        20,
        "#ffffff",
        "#ffaf20",
        true)

    val config = CPXConfigurationBuilder(
        "<Your app id>",
        "<Your external user id>",
        "<Your secure hash>",
        style)
        .build()

    val cpx = CPXResearch.init(context, config)
    cpxResearch = cpx
}
```

or Java

```java
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
    CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideRightNormal,
            "Earn up to 3 Coins in<br> 4 minutes with surveys",
            20,
            "#ffffff",
            "#ffaf20",
            true);

    CPXConfiguration config = new CPXConfigurationBuilder("<Your app id>",
            "<Your external user id>",
            "<Your secure hash>",
            style)
            .build();

    cpxResearch = CPXResearch.Companion.init(this, config);
}
```

# ProGuard
To use ProGuard minifying for your app, add the rules to your app's proguard-rules.pro file to exclude the SDK from being minified:
```groovy
-keep class com.makeopinion.cpxresearchlib.** { *; }
```

To also surpress warnings from the underlying okhttp library you can add the following as well:
```groovy
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
```

# Logging
CPXResearch provides an internal debug logging mechanism that can be activated calling the function setLogMode(boolean). If set to true the SDK will log in memory.
Logs can be exported calling exportLog(onActivity).
