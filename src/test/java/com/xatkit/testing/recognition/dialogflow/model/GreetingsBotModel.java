package com.xatkit.testing.recognition.dialogflow.model;

import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.intent.IntentDefinition;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import lombok.Getter;
import lombok.val;

import static com.xatkit.dsl.DSL.eventIs;
import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.state;

public class GreetingsBotModel extends ExecutionModelImpl {

    @Getter
    private IntentDefinition greetings;

    @Getter
    private IntentDefinition howAreYou;

    @Getter
    private IntentDefinition fine;

    @Getter
    private IntentDefinition sad;

    public GreetingsBotModel() {

        /*
         * Define the intents our bot will react to.
         */
        greetings = intent("Greetings")
                .trainingSentence("Hi")
                .trainingSentence("Hello")
                .trainingSentence("Good morning")
                .trainingSentence("Good afternoon")
                .getIntentDefinition();

        howAreYou = intent("HowAreYou")
                .trainingSentence("How are you?")
                .trainingSentence("What's up?")
                .trainingSentence("How do you feel?")
                .getIntentDefinition();

        fine = intent("Fine")
                .trainingSentence("I am fine")
                .trainingSentence("Fine")
                .trainingSentence("fine")
                .getIntentDefinition();

        sad = intent("Sad")
                .trainingSentence("Not so good")
                .trainingSentence("I am sad")
                .trainingSentence("Sad")
                .getIntentDefinition();

        /*
         * Instantiate the platform we will use in the bot definition.
         */
        /*
         * Similarly, instantiate the intent/event providers we want to use.
         */
        ReactPlatform reactPlatform = new ReactPlatform();
        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();


        /*
         * Create the states we want to use in our bot.
         */
        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val handleWelcome = state("HandleWelcome");
        val handleWhatsUp = state("HandleWhatsUp");
        val handleFine = state("HandleFine");
        val handleSad = state("HandleSad");

        /*
         * Specify the content of the bot states (i.e. the behavior of the bot).
         */
        init
                .next()
                /*
                 * We check that the received event matches the ClientReady event defined in the
                 * ReactEventProvider. The list of events defined in a provider is available in the provider's
                 * wiki page.
                 */
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);

        awaitingInput
                .next()
                /*
                 * The Xatkit DSL offers dedicated predicates (intentIs(IntentDefinition) and eventIs
                 * (EventDefinition) to check received intents/events.
                 * <p>
                 * You can also check a condition over the underlying bot state using the following syntax:
                 * <pre>
                 * {@code
                 * .when(context -> [condition manipulating the context]).moveTo(state);
                 * }
                 * </pre>
                 */
                .when(intentIs(greetings)).moveTo(handleWelcome)
                .when(intentIs(howAreYou)).moveTo(handleWhatsUp);

        handleWelcome
                .body(context -> reactPlatform.reply(context, "Hi, nice to meet you!"))
                .next()
                /*
                 * A transition that is automatically navigated: in this case once we have answered the user we
                 * want to go back in a state where we wait for the next intent.
                 */
                .moveTo(awaitingInput);

        handleWhatsUp
                .body(context -> reactPlatform.reply(context, "I am fine and you?"))
                .next()
                .when(intentIs(fine)).moveTo(handleFine)
                .when(intentIs(sad)).moveTo(handleSad);

        handleFine
                .body(context -> reactPlatform.reply(context, "Great!"))
                .next()
                .moveTo(awaitingInput);

        handleSad
                .body(context -> reactPlatform.reply(context, "Oh, let me know if I can do something for you :/"))
                .next()
                .moveTo(awaitingInput);

        val defaultFallback = fallbackState()
                .body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it"));

        this.getUsedPlatforms().add(reactPlatform);
        this.getUsedProviders().add(reactEventProvider);
        this.getUsedProviders().add(reactIntentProvider);
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());
    }
}
