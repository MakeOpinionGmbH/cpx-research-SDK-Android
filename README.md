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

# Prerequisites

- Android SDK 21 (Lollipop) or newer

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
	implementation 'com.github.MakeOpinionGmbH:cpx-research-SDK-Android:0.9.5'
}
```

# Usage

## Initialize the library

Enter the following code early in your App's life cycle, for example in the App's Application class. For Kotlin

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
            .build()

val cpx = CPXResearch.init(context, config)
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
        .build();

cpxResearch = CPXResearch.Companion.init(this, config);
```

## Using the SDK's banner for overlays

In your Activity activate the automatic banner display and set the delegate to handle CPX Research events.

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as? CPXApplication)?.let {
            it.cpxResearch().setSurveyVisibleIfAvailable(true, this)
            it.cpxResearch().registerListener(object : CPXResearchListener {
                override fun onSurveysUpdated() {
                    Log.d("CPX", "surveys updated.")
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
            })
        }
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
            })
        }
    }
```

or Java

```java
app.getCpxResearch().registerListener(new CPXResearchListener() {
    @Override
    public void onSurveysUpdated() {

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
