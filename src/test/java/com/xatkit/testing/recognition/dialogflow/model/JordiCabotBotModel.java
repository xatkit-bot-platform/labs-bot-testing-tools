package com.xatkit.testing.recognition.dialogflow.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import lombok.val;


import java.io.InputStream;

import static com.xatkit.dsl.DSL.*;

/**
 * This is an example greetings bot designed with Xatkit.
 * <p>
 * You can check our <a href="https://github.com/xatkit-bot-platform/xatkit/wiki">wiki</a>
 * to learn more about bot creation, supported platforms, and advanced usage.
 */
public class JordiCabotBotModel extends ExecutionModelImpl {

    /*
     * Your bot is a plain Java application: you need to define a main method to make the created jar executable.
     */
    public JordiCabotBotModel() {

        val contact = intent("contact")
                .trainingSentence("How can I contact you")
                .trainingSentence("Can I talk to you?")
                .trainingSentence("What is your email address")
                .trainingSentence("Do you have an email?")
                .trainingSentence("How can I get in touch?")
                .trainingSentence("I want to write you a mail")
                .trainingSentence("Can I send you an email?")
                .trainingSentence("What's your email");

        val bio = intent("Bio")
                .trainingSentence("Who are you")
                .trainingSentence("Who is Jordi Cabot")
                .trainingSentence("Where can I read more about you")
                .trainingSentence("I would like to know more about you");

        val hiringMe = intent("HiringMe")
                .trainingSentence("I have an offer for you")
                .trainingSentence("I would like to hire you")
                .trainingSentence("I have a project for you")
                .trainingSentence("I want to collaborate with you")
                .trainingSentence("I want to propose you something")
                .trainingSentence("I have a proposition")
                .trainingSentence("I want to talk to you about a consulting gig")
                .trainingSentence("Do you want work as a consultant?")
                .trainingSentence("There is a consulting opportunity for you");

        val hiringMeRates = intent("HiringMeRates")
                .trainingSentence("How much do you charge?")
                .trainingSentence("What are your fees")
                .trainingSentence("What is your hourly fee?")
                .trainingSentence("What is your budget?");

        val hiringThem = intent("HiringThem")
                .trainingSentence("I would like to work for you")
                .trainingSentence("Do you have open positions for me?")
                .trainingSentence("I would like to join your team")
                .trainingSentence("Are you hiring?")
                .trainingSentence("Are you accepting applications?")
                .trainingSentence("I want to start a PhD")
                .trainingSentence("I would like to collaborate with you")
                .trainingSentence("Do you have a list of open positions?")
                .trainingSentence("I want to do a postdoc with you");

        val findingMeOnline = intent("FindingMeOnline")
                .trainingSentence("Do you have a twitter account?")
                .trainingSentence("What is your LinkedIn account?")
                .trainingSentence("Are you on Instagram?")
                .trainingSentence("How can I find you online?")
                .trainingSentence("Are you on social media?")
                .trainingSentence("Where can I find you online?");

        val listOfTopics = intent("ListOfTopics")
                .trainingSentence("What topic do you work on?")
                .trainingSentence("What is your expertise?")
                .trainingSentence("What do you know about?")
                .trainingSentence("What are you expert on?");


        val canYouHelp = intent("CanYouHelp")
                .trainingSentence("Can you read my paper?")
                .trainingSentence("Can you review my paper?")
                .trainingSentence("Can you revise my work?")
                .trainingSentence("I need your help")
                .trainingSentence("I have a problem")
                .trainingSentence("Can you help me?")
                .trainingSentence("I need your expertise");

        val downloadCV = intent("downloadCV")
                .trainingSentence("Is your CV online")
                .trainingSentence("Can I get your CV?")
                .trainingSentence("Where can I read your Curriculum?")
                .trainingSentence("Can you send me your Curriculum Vitae?")
                .trainingSentence("Can I get a copy of your CV?");


        val whereDoYouWork = intent("Where do you work?")
                .trainingSentence("Where do you work?")
                .trainingSentence("Where are you located?")
                .trainingSentence("Where is your office")
                .trainingSentence("Where can I visit you?");

        val startup = intent("Startup")
                .trainingSentence("Do you have a startup?")
                .trainingSentence("Have you created a startup?")
                .trainingSentence("Do you have your own company?")
                .trainingSentence("Did you create a spinoff?");

        val chatbot = intent("Chatbot")
                .trainingSentence("Do you know how to create chatbots?")
                .trainingSentence("Can you help me create a bot?")
                .trainingSentence("How can I learn more about chatbot development?")
                .trainingSentence("What is the best chatbot platform")
                .trainingSentence("Can you explain me how to develop a bot?");

        val team = intent("Team")
                .trainingSentence("What is your team?")
                .trainingSentence("What is your team's website?")
                .trainingSentence("Who works in your team?")
                .trainingSentence("Who are the members of your team?")
                .trainingSentence("Who is your team?")
                .trainingSentence("How big is your team?");

        val books = intent("Books")
                .trainingSentence("Have you written a book?")
                .trainingSentence("Did you authored a book?")
                .trainingSentence("Where can I read your book?")
                .trainingSentence("How many books have you written?");

        val metrics = intent("Metrics")
                .trainingSentence("How many papers have you published?")
                .trainingSentence("What is your h-index?")
                .trainingSentence("Are you successful as a researcher?")
                .trainingSentence("How many papers have you published?");


        val giveATalk = intent("GiveATalk")
                .trainingSentence("Can I invite you to give a talk?")
                .trainingSentence("Can you give us a talk?")
                .trainingSentence("Do you talk at company events?")
                .trainingSentence("Can you give us a seminar?")
                .trainingSentence("Can you give a talk to my students?")
                .trainingSentence("Can you give a lecture in my course?");

        val tool = intent("Tools")
                .trainingSentence("Where can I download your tools?")
                .trainingSentence("What tools have you built?")
                .trainingSentence("Do you have tools available?")
                .trainingSentence("What is your GitHub repository?");

        val scientificWorks = intent("ScientificWorks")
                .trainingSentence("Have you worked on TOPIC?")
                .trainingSentence("Have you written a paper on TOPIC?")
                .trainingSentence("What do you know about TOPIC?")
                .trainingSentence("Do you know about TOPIC?")
                .trainingSentence("What do you think of TOPIC?")
                .trainingSentence("Are you an expert on TOPIC?")
                .parameter("topic")
                .fromFragment("TOPIC")
                .entity(any());

        val paperRequest = intent("PaperRequest")
                .trainingSentence("Can I read your paper PAPER?")
                .trainingSentence("Can I get a free version of the paper PAPER?")
                .trainingSentence("Where can I download your article PAPER?")
                .trainingSentence("Send me your article PAPER")
                .trainingSentence("Can I access your PAPER for free?")
                .parameter("paper")
                .fromFragment("PAPER")
                .entity(any());

        val email = intent("Email")
                .trainingSentence("My email is EMAIL")
                .trainingSentence("email EMAIL")
                .trainingSentence("EMAIL")
                .parameter("email")
                .fromFragment("EMAIL")
                .entity(email());

        val greetings = intent("Greetings")
                .trainingSentence("Hi")
                .trainingSentence("Hello")
                .trainingSentence("Good morning")
                .trainingSentence("Good afternoon")
                ;

        val goodbye = intent("Goodbye")
                .trainingSentence("Bye")
                .trainingSentence("See you")
               ;

        ReactPlatform reactPlatform = new ReactPlatform();
        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();


        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val handleGreetings = state("HandleGreetings");
        val handleGreetingsBye = state("HandleGreetingsBye");
        val handleHelpRequest = state("HandleHelpRequest");
        val handleGiveATalk = state("HandleGiveATalk");
        val handleHiringMe = state("HandleHiringMe");
        val handleHiringThem = state("HandleHiringThem");
        val handleFindingMeOnline = state("HandleFindingMeOnline");
        val handleScientificWork = state("HandleScientificWork");
        val handleDownloadCV = state("HandleDownloadCV");
        val handleWhereDoYouWork = state("HandleWhereDoYouWork");
        val handleTools = state("HandleTools");
        val handleStartup = state("HandleStartup");
        val handleTeam = state("HandleTeam");
        val handlePaperRequest = state("HandlePaperRequest");
        val handlePaperEmail = state("HandlePaperEmail");
        val handleMetrics = state("HandleMetrics");
        val handleListOfTopics = state("HandleListOfTopics");
        val handleChatbot = state("HandleChatbot");
        val handleBio = state("HandleBio");
        val handleBook = state("HandleBook");


        init
                .next()
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);

        awaitingInput
                .next()
                .when(intentIs(greetings)).moveTo(handleGreetings)
                .when(intentIs(goodbye)).moveTo(handleGreetingsBye)
                .when(intentIs(contact)).moveTo(handleHelpRequest)
                .when(intentIs(canYouHelp)).moveTo(handleHelpRequest)
                .when(intentIs(giveATalk)).moveTo(handleGiveATalk)
                .when(intentIs(hiringMe)).moveTo(handleHiringMe)
                .when(intentIs(hiringMeRates)).moveTo(handleHiringMe)
                .when(intentIs(hiringThem)).moveTo(handleHiringThem)
                .when(intentIs(findingMeOnline)).moveTo(handleFindingMeOnline)
                .when(intentIs(scientificWorks)).moveTo(handleScientificWork)
                .when(intentIs(downloadCV)).moveTo(handleDownloadCV)
                .when(intentIs(whereDoYouWork)).moveTo(handleWhereDoYouWork)
                .when(intentIs(tool)).moveTo(handleTools)
                .when(intentIs(startup)).moveTo(handleStartup)
                .when(intentIs(team)).moveTo(handleTeam)
                .when(intentIs(paperRequest)).moveTo(handlePaperRequest)
                .when(intentIs(metrics)).moveTo(handleMetrics)
                .when(intentIs(listOfTopics)).moveTo(handleListOfTopics)
                .when(intentIs(chatbot)).moveTo(handleChatbot)
                .when(intentIs(bio)).moveTo(handleBio)
                .when(intentIs(books)).moveTo(handleBook);


        handleGreetings
                .body(context -> reactPlatform.reply(context, "Hi, how can I help you?"))
                .next()
                .moveTo(awaitingInput);
        handleGreetingsBye
                .body(context -> reactPlatform.reply(context, "Thanks for stopping by!"))
                .next()
                .moveTo(awaitingInput);
        handleHelpRequest
                .body(context -> reactPlatform.reply(context, "You can write to jordi.cabot@icrea.cat with the details on how I can help you and why"))
                .next()
                .moveTo(awaitingInput);
        handleGiveATalk
                .body(context -> reactPlatform.reply(context, "Take a look at my [Speaking](https://jordicabot.com/talks/) page and feel free to reach out to jordi.cabot@icrea.cat with the details"))
                .next()
                .moveTo(awaitingInput);
        handleHiringMe
                .body(context -> reactPlatform.reply(context, "You can write to jordi.cabot@icrea.cat with the details"))
                .next()
                .moveTo(awaitingInput);
        handleHiringThem
                .body(context -> reactPlatform.reply(context, "Our Open Positions are listed [here](https://som-research.uoc.edu/join-our-team/)"))
                .next()
                .moveTo(awaitingInput);
        handleFindingMeOnline
                .body(context -> reactPlatform.reply(context, "Feel free to follow me on social media. Check the list of [social media profiles](https://jordicabot.com/online-profiles/)"))
                .next()
                .moveTo(awaitingInput);
        handleScientificWork
                .body(context -> reactPlatform.reply(context, "Check out my [list of publications](https://dblp.uni-trier.de/pers/hd/c/Cabot:Jordi)"))
                .next()
                .moveTo(awaitingInput);
        handleDownloadCV
                .body(context -> reactPlatform.reply(context, "Download it from [here](https://jordicabot.com/FullCV)"))
                .next()
                .moveTo(awaitingInput);
        handleWhereDoYouWork
                .body(context -> reactPlatform.reply(context, "I'm in Barcelona. More specifically [here](https://jordicabot.com/visit-me-in-barcelona/)"))
                .next()
                .moveTo(awaitingInput);
        handleTools
                .body(context -> reactPlatform.reply(context, "You can see my personal tools [here](https://github.com/jcabot). Check also my [team tools](https://som-research.uoc.edu/research-tools/)"))
                .next()
                .moveTo(awaitingInput);
        handleStartup
                .body(context -> reactPlatform.reply(context, "My current startup is [Xatkit](https://xatkit.com/), the easiest way to create advanced chatbots"))
                .next()
                .moveTo(awaitingInput);
        handleTeam
                .body(context -> reactPlatform.reply(context, "Check my [SOM team page](https://som-research.uoc.edu/)"))
                .next()
                .moveTo(awaitingInput);
        handleMetrics
                .body(context -> reactPlatform.reply(context, "\"Download my CV to get a deep collection of metrics and information about my research career. Download it from [here](https://jordicabot.com/FullCV)"))
                .next()
                .moveTo(awaitingInput);
        handleListOfTopics
                .body(context -> reactPlatform.reply(context, "My research falls into the broad area of systems and software engineering," +
                        " especially promoting the rigorous use of software models in all software tasks while keeping an eye on the most unpredictable element " +
                        "in any project: the people involved in it. Current research topics include pragmatic formal verification techniques," +
                        " analysis of open source communities, open data exploitation and the role AI can" +
                        " play in software development (and vice versa)"))
                .next()
                .moveTo(awaitingInput);
        handleChatbot
                .body(context -> reactPlatform.reply(context, "Check my platform [Xatkit](https://xatkit.com/), the easiest way to create advanced chatbots"))
                .next()
                .moveTo(awaitingInput);
        handleBio
                .body(context -> reactPlatform.reply(context, "I’m Jordi Cabot, an ICREA Research Professor at IN3, the Research center of the Open University of Catalonia (UOC) where I’m leading the SOM Research Lab." +
                        " Previously, I’ve been at École des Mines de Nantes, Inria, University of Toronto, Politecnico di Milano and the Technical University of Catalonia.\n" +
                        " My research falls into the broad area of systems and software engineering, especially promoting the rigorous use of software models" +
                        " in all software tasks while keeping an eye on the most unpredictable element in any project: the people involved in it. " +
                        "Current research topics include pragmatic formal verification techniques, analysis of open source communities, " +
                        "open data exploitation and the role AI can play in software development (and vice versa)"))
                .next()
                .moveTo(awaitingInput);

        handlePaperRequest
                .body(context ->
                {
                    context.getSession().put("paper", context.getIntent().getValue("paper"));//We store the name of the requested paper
                    context.getSession().put("fullrequest", context.getIntent().getMatchedInput()); //We store the full text as a backup
                    reactPlatform.reply(context, "Sure, give me your email address and I'll send it as soon as possible");
                })
                .next()
                .when(intentIs(email)).moveTo(handlePaperEmail);

        handlePaperEmail
                .body(context ->
                {
                    context.getSession().put("email", context.getIntent().getValue("email")); //We store the name of the requested paper
                    if (sendEmail((String) context.getSession().get("email"), (String) context.getSession().get("paper"), (String) context.getSession().get("fullrequest"))) {

                        reactPlatform.reply(context, "Perfect, sending your request for paper " + context.getSession().get("paper") +
                                " from " + context.getSession().get("email") + ". We'll get back to you as soon as possible");
                    } else {
                        reactPlatform.reply(context, "We seem to have trouble passing on your request, please write directly to jordi.cabot@icrea.cat");
                    }
                })
                .next()
                .moveTo(awaitingInput);


        val defaultFallback = fallbackState()
                .body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it. Try again. Or just write me an email to jordi.cabot@icrea.cat if you have a very specific question to ask"));



        this.getUsedPlatforms().add(reactPlatform);
        //this.getUsedPlatforms().add(restPlatform);
        this.getUsedProviders().add(reactEventProvider);
        this.getUsedProviders().add(reactIntentProvider);
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());

    }

    public static boolean sendEmail(String email, String paper, String fullrequest) {
        boolean success = false;
        Gson gson = new Gson();
        JsonObject requestBody = new JsonObject();
        JsonArray toField = new JsonArray();
        toField.add("jcabotsagrera@gmail.com");
        requestBody.add("to", toField);
        requestBody.addProperty("subject", "Paper request from a visitor");
        requestBody.addProperty("content", "Visitor " + email + " is interested in the paper " + paper + ".\r\n" + " Full request for reference " + fullrequest);

        try {

            HttpResponse<InputStream> response = Unirest.post("https://mail.xatkit.com/send")
                    .basicAuth("xatkit-mail", "Ha%u~{Ac@S<JG99s")
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(requestBody))
                    .asBinary();

// Manage the response from the server
            switch (response.getStatus()) {
                case 200:
                    System.out.println("Mail sent");
                    success = true;
                    break;
                case 401:
                    System.out.println("Invalid credentials");
                    break;
                default:
                    System.out.println("Unexpected response from the server: " + response.getStatus());
            }

        } catch (Exception e) {
            System.err.println("Exception when sending email " + e.getMessage());
            success = false;

        }


        return success;
    }

}
