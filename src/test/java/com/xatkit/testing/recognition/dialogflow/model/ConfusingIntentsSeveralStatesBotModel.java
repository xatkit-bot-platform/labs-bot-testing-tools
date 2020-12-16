package com.xatkit.testing.recognition.dialogflow.model;

import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import com.xatkit.plugins.rest.platform.RestPlatform;
import lombok.val;

import static com.xatkit.dsl.DSL.*;

public class ConfusingIntentsSeveralStatesBotModel extends ExecutionModelImpl {


    public ConfusingIntentsSeveralStatesBotModel(){
        val reactPlatform = new ReactPlatform();
        val restPlatform = new RestPlatform();


        val init = state("Init");
        val awaitingHelloInput = state("AwaitingHelloInput");
        val awaitingOtherInput = state("AwaitingOtherInput");
        val printHello = state("PrintHello");
        val printFirstHello = state("PrintFirstHello");

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
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingHelloInput);


        awaitingOtherInput
                .next()
                .when(intentIs(helloIntent))
                .moveTo(printFirstHello)
                .when(intentIs(whatsUpIntent))
                .moveTo(printHello)
                .when(intentIs(greetingsIntent))
                .moveTo(printHello)
                .when(intentIs(hiIntent))
                .moveTo(printFirstHello)
                .when(intentIs(heyIntent))
                .moveTo(printHello)
                ;

        awaitingHelloInput
                .next()
                .when(intentIs(helloIntent))
                .moveTo(awaitingOtherInput)
                .when(intentIs(hiIntent))
                .moveTo(awaitingOtherInput)
                ;

        printHello
                .body(context -> {
                        reactPlatform.reply(context, "Hey!");
                })
                .next()
                .moveTo(awaitingOtherInput);

        printFirstHello
                .body(context -> {
                        reactPlatform.reply(context, "Welcome!");
                })
                .next()
                .moveTo(awaitingOtherInput);


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
