package com.xatkit.testing.intentMatcher;

import com.xatkit.core.recognition.EntityMapper;
import com.xatkit.intent.CustomEntityDefinition;
import com.xatkit.intent.EntityType;

import java.text.MessageFormat;

public class ConcreteEntityReferenceMapper extends EntityMapper {

    public ConcreteEntityReferenceMapper() {
        this.registerDateTimeEntities();
        this.registerNumberEntities();
        this.registerAmountEntities();
        this.registerGeographyEntities();
        this.registerContactEntities();
        this.registerNamesEntities();
        this.registerMusicEntities();
        this.registerOtherEntities();
        this.registerGenericEntities();
        this.setFallbackEntityMapping("AnyString");
    }

    private void registerDateTimeEntities() {
        this.addEntityMapping(EntityType.DATE_TIME, "at 9 pm");
        this.addEntityMapping(EntityType.DATE, "Monday");
        this.addEntityMapping(EntityType.DATE_PERIOD, "next week");
        this.addEntityMapping(EntityType.TIME, "9 pm");
        this.addEntityMapping(EntityType.TIME_PERIOD, "tomorrow morning");
    }

    private void registerNumberEntities() {
        this.addEntityMapping(EntityType.NUMBER, "1.23");
        this.addEntityMapping(EntityType.CARDINAL, "1");
        this.addEntityMapping(EntityType.ORDINAL, "first");
        this.addEntityMapping(EntityType.INTEGER, "1");
        this.addEntityMapping(EntityType.NUMBER_SEQUENCE, "1, 2, 3");
        this.addEntityMapping(EntityType.FLIGHT_NUMBER, "AA 120");
    }

    private void registerAmountEntities() {
        this.addEntityMapping(EntityType.UNIT_AREA, "90 square meters");
        this.addEntityMapping(EntityType.UNIT_CURRENCY, "1000 Euros");
        this.addEntityMapping(EntityType.UNIT_LENGTH, "2 kilometers");
        this.addEntityMapping(EntityType.UNIT_SPEED, "50 km/h");
        this.addEntityMapping(EntityType.UNIT_VOLUME, "5 liters");
        this.addEntityMapping(EntityType.UNIT_WEIGHT, "70 kilograms");
        this.addEntityMapping(EntityType.PERCENTAGE, "12%");
        this.addEntityMapping(EntityType.TEMPERATURE, "19ºC");
        this.addEntityMapping(EntityType.DURATION, "20 seconds");
        this.addEntityMapping(EntityType.AGE, "2 years");
    }

    private void registerGeographyEntities() {
        this.addEntityMapping(EntityType.ADDRESS, "221B Baker St, London");
        this.addEntityMapping(EntityType.STREET_ADDRESS, "221B Baker St");
        this.addEntityMapping(EntityType.ZIP_CODE, "28080");
        this.addEntityMapping(EntityType.CAPITAL, "Madrid");
        this.addEntityMapping(EntityType.COUNTRY, "Spain");
        this.addEntityMapping(EntityType.CITY, "Barcelona");
        this.addEntityMapping(EntityType.STATE, "California");
        this.addEntityMapping(EntityType.CITY_US, "San Francisco");
        this.addEntityMapping(EntityType.STATE_US, "California");
        this.addEntityMapping(EntityType.CITY_GB, "London");
        this.addEntityMapping(EntityType.STATE_GB, "England");
        this.addEntityMapping(EntityType.AIRPORT, "Madrid-Barajas");
        this.addEntityMapping(EntityType.LOCATION, "51.5237439,-0.1602449");
    }

    private void registerContactEntities() {
        this.addEntityMapping(EntityType.EMAIL, "email@example.org");
        this.addEntityMapping(EntityType.PHONE_NUMBER, "+1 800 444 4444");
    }

    private void registerNamesEntities() {
        this.addEntityMapping(EntityType.GIVEN_NAME, "John");
        this.addEntityMapping(EntityType.LAST_NAME, "Doe");
    }

    private void registerMusicEntities() {
        this.addEntityMapping(EntityType.MUSIC_ARTIST, "Rolling Stones");
        this.addEntityMapping(EntityType.MUSIC_GENRE, "rock");
    }

    private void registerOtherEntities() {
        this.addEntityMapping(EntityType.COLOR, "green");
        this.addEntityMapping(EntityType.LANGUAGE, "en-US");
    }

    private void registerGenericEntities() {
        this.addEntityMapping(EntityType.ANY, "AnyString");
        this.addEntityMapping(EntityType.URL, "https://example.org");
    }

    public void addCustomEntityMapping(CustomEntityDefinition entityDefinition, String concreteEntity) {
        throw new UnsupportedOperationException(MessageFormat.format("{0} does not allow to register custom entity mappings, use getMappingFor(EntityDefinition) to get DialogFlow-compatible mapping of {1}", this.getClass().getSimpleName(), CustomEntityDefinition.class.getSimpleName()));
    }

    protected String getMappingForCustomEntity(CustomEntityDefinition customEntityDefinition) {
        return customEntityDefinition.getName();
    }
}
