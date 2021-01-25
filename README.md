# Xatkit's DialogFlow Testing Tools
This repository contains utility classes to do advanced testing on top of a NLP engine of Xatkit.

## Installation

You need to have [Xatkit installed locally](https://github.com/xatkit-bot-platform/xatkit/wiki/Build-Xatkit) to use this project. You can then import this project as a maven project in you preferred editor.

## How-Tos

### Configure DialogFlow

- Set your DialogFlow project id at:

  - src > test > resources > greetings-bot.properties

```
xatkit.dialogflow.projectId=<your-dialogflow-project-id>
xatkit.dialogflow.language=en-US
xatkit.dialogflow.clean_on_startup=true
xatkit.dialogflow.credentials.path=key.json
```

- Include your key.json at 

  - src > test > resources > key.json


### Running tests

You can run the tests with the MatchIntentExample class, located in src > test > java

- The testMatchingIntents method will perform the intent matching from every state of the bot
  - The method will print all the found matching intents when it finishes in the format: 
  ```Intent "<intent-name>" was confused with intent "<intent-name>" from state "<state-name>" with the sentence "<failing-utterance>" and a confidence of <confidence-score>```

- The testStatelessMatchingIntents method will perform a stateless matching of the intents
  - The method will print all the found matching intents when it finishes in the format: 
  ```Intent "<intent-name>" was confused with intent "<intent-name>" with the sentence "<failing-utterance>" and a confidence of <confidence-score>```

- In order to change the bot model you can change the definition at: ```private static ExecutionModel botModel = new ChatBotCorpusBotModel();``` for any Bot Execution model.
