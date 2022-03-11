# Xatkit's Bot Testing Tools
This repository contains utility classes to do advanced testing on
[Xatkit](https://github.com/xatkit-bot-platform/xatkit) chatbots.

## Installation

- You need to have [Xatkit installed locally](https://github.com/xatkit-bot-platform/xatkit/wiki/Build-Xatkit) to use 
this project.

- Install this project in your local maven repository:
  ```bash
  git clone https://github.com/xatkit-bot-platform/labs-bot-testing-tools
  cd labs-bot-testing-tools
  mvn clean install
  ```

- Add the dependency to your bot project:
  ```xml
  <dependency>
      <groupId>com.xatkit</groupId>
      <artifactId>labs-bot-testing-tools</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </dependency>
  ```
  
## How to use it

### IntentMatchingTesting

This test is used to check if the detected intent for a given utterance is the expected one (i.e. to test if a 
chatbot detects the correct intents). This code snippet can be used as an example:

```java
public void intentsTest() {
    testIntentMatching(
            xatkitBot,
            "myFile.csv",
            state);
}
```

- `xatkitBot` must be a valid `com.xatkit.core.XatkitBot`

- `state` must be a valid `com.xatkit.execution.State`, containing a set of intents that can be recignized through it 
  (alternatively, instead of passing a state as a parameter, you can pass a list of
  `com.xatkit.intent.IntentDefinition` directly)

- `"myFile.csv` must be a csv file (located in the `src/test/resources/` folder of your project) containing a table 
  with this structure:

| input        | expected_intent | detected_intent |
|--------------|-----------------|-----------------|
| Hello        | Greetings       || 
| How are you? | HowAreYou       || 

After running this code, a file `myFileResult.csv` will be created in the `src/test/resources/` folder of your project, 
filling the missing values of the `detected_intent` column. If the detected intents match with the expected intents, 
then your bot does what you want!

> 📚 Note that the intent detection will depend primarily on the intent recognition provider you use within your bot 
> (e.g. [Dialogflow](https://github.com/xatkit-bot-platform/xatkit/wiki/Integrating-DialogFlow) or
> [NLP.js](https://github.com/xatkit-bot-platform/xatkit/wiki/Using-NLP.js))

