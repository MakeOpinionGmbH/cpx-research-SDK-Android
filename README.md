# CPX Research Android SDK

#### Monetize your product with fun surveys.

We will make it easy for you: Simply implement our solution and be ready to start monetizing your product immediately!
Let users earn your virtual currency by simply participating in exciting and well paid online surveys!

This SDK is owned by [MakeOpinion GmbH](http://www.makeopinion.com).

[Learn more.](https://cpx-research.com/)

# Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage Kotlin](#usage)

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
	implementation 'com.github.MakeOpinionGmbH:cpx-research-SDK-Android:0.9.0'
}
```

# Usage

## Initialize the library

Enter the following code early in your App's life cycle, for example in the App's Application class.

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
            "<Your secure key>",
            style)
            .build()

val cpx = CPXResearch.init(context, config)
```

## Easy mode

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

## Expert mode

In your Activity set the delegate to handle CPX Research events.

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

Tell the library to show the surveys list, call

```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().openSurvey(this)
}
```

Show a specific survey

```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().openSurvey(this, surveyId)
}
```

Mark a transaction as paid

```kotlin
(activity?.application as? CPXApplication)?.let { app ->
    app.cpxResearch().markTransactionAsPaid(transactionId, messageId)
}
```
