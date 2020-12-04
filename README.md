# Xatkit's DialogFlow Testing Tools
This repository contains utility classes to do advanced testing on top of the DialogFlow connector of Xatkit.

## Installation

You need to have [Xatkit installed locally](https://github.com/xatkit-bot-platform/xatkit/wiki/Build-Xatkit) to use this project. You can then import this project as a maven project in you preferred editor.



## How-Tos

### Configure the intents that can be matched by DialogFlow

The class `DialogFlowTestingContext` is what you need: it wraps an existing `DialogFlowStateContext` and provides a simple API to enable intents. The `DialogFlowTestingContext` can then be used to retrieve the intent from a given input:

```java
DialogFlowStateContext dfContext = 
    (DialogFlowStateContext) intentProvider.createContext("MyContext");
DialogFlowTestingContext testingContext = 
    new DialogFlowTestingContext(dfContext);
testingContext.enableIntents(greetingsIntent)
RecognizedIntent recoginzedIntent = intentProvider.getIntent("Hello", testingContext);
assertThat(recognizedIntent.getDefinition()).isEqualTo(greetingsIntent);
```

> 📚 Using `DialogFlowTestingContext` does not alter the underlying DialogFlow agent, so there is no need to retrain the agent nor redeploy the Xatkit bot.



Note that `DialogFlowTestingContext#enableIntents` fully overrides the default Xatkit behavior, and there is no easy way to retrieve which intents should have been matched in a standard scenario.

>  🛠 Check [this class](https://github.com/xatkit-bot-platform/labs-dialogflow-testing-tools/blob/main/src/test/java/com/xatkit/testing/recognition/dialogflow/EnableIntentExamples.java) for a few examples showing how to enable intents and check the value returned by `getIntent`. (make sure you properly edit the corresponding `.properties` file first! Check the class documentation for more information)

