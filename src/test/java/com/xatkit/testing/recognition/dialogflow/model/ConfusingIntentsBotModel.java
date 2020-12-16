package com.xatkit.testing.recognition.dialogflow.model;

import com.google.gson.JsonElement;
import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import com.xatkit.plugins.rest.platform.RestPlatform;
import com.xatkit.plugins.rest.platform.utils.ApiResponse;
import lombok.val;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.xatkit.dsl.DSL.*;

public class ConfusingIntentsBotModel extends ExecutionModelImpl {


    public ConfusingIntentsBotModel(){
        val reactPlatform = new ReactPlatform();
        val restPlatform = new RestPlatform();


        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val printHello = state("PrintHello");

        val whatsUpIntent = intent("WhatsUpIntent")
                .trainingSentence("What's up?")
                .trainingSentence("What's up")
                .getIntentDefinition()
                ;

        val helloIntent = intent("HelloIntent")
                .trainingSentence("Hello")
                .trainingSentence("Hello!")
                .getIntentDefinition()
                ;

        val greetingsIntent = intent("GreetingsIntent")
                .trainingSentence("Greetings!")
                .trainingSentence("Greetings")
                .getIntentDefinition()
                ;

        val hiIntent = intent("HiIntent")
                .trainingSentence("Hi")
                .trainingSentence("Hi!")
                .getIntentDefinition()
                ;

        val heyIntent = intent("HeyIntent")
                .trainingSentence("Hey")
                .trainingSentence("Hey!")
                .getIntentDefinition()
                ;


        init
                .next()
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);


        awaitingInput
                .next()
                .when(intentIs(helloIntent))
                .moveTo(printHello)
                .when(intentIs(whatsUpIntent))
                .moveTo(printHello)
                .when(intentIs(greetingsIntent))
                .moveTo(printHello)
                .when(intentIs(hiIntent))
                .moveTo(printHello)
                .when(intentIs(heyIntent))
                .moveTo(printHello)
                ;


        printHello
                .body(context -> {
                        reactPlatform.reply(context, "Hey!");
                })
                .next()
                .moveTo(awaitingInput);


        val defaultFallback = fallbackState()
                .body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it"));


        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();

        this.getUsedPlatforms().add(reactPlatform);
        this.getUsedPlatforms().add(restPlatform);
        this.getUsedProviders().add(reactEventProvider);
        this.getUsedProviders().add(reactIntentProvider);
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());
    }

}
