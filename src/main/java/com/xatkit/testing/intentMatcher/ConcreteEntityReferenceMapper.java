package com.xatkit.testing.intentMatcher;

import com.xatkit.core.XatkitException;
import com.xatkit.core.recognition.EntityMapper;
import com.xatkit.intent.*;
import com.xatkit.intent.impl.MappingEntityDefinitionImpl;
import fr.inria.atlanmod.commons.Preconditions;
import fr.inria.atlanmod.commons.log.Log;
import org.eclipse.emf.common.util.EList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ConcreteEntityReferenceMapper extends EntityMapper {

    private final Random rng = new Random(42);

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
        EList<MappingEntityDefinitionEntry> entries = ((MappingEntityDefinitionImpl) customEntityDefinition).getEntries();
        return entries.get(rng.nextInt(entries.size())).getReferenceValue();
    }

    protected List<String> getAllMappingsForCustomEntity(CustomEntityDefinition customEntityDefinition) {
        List<String> customEntities = new ArrayList<>();
        for (MappingEntityDefinitionEntry entry: ((MappingEntityDefinitionImpl) customEntityDefinition).getEntries()){
            customEntities.add(entry.getReferenceValue());
        }
        return customEntities;
    }


    public List<String> getAllMappingsFor(EntityDefinition abstractEntity) {
        Preconditions.checkNotNull(abstractEntity, "Cannot retrieve the concrete mapping for the provided %s %s", new Object[]{EntityDefinition.class.getSimpleName(), abstractEntity});
        if (abstractEntity instanceof BaseEntityDefinition) {
            BaseEntityDefinition coreEntity = (BaseEntityDefinition)abstractEntity;
            Preconditions.checkArgument(Objects.nonNull(coreEntity.getEntityType()), "Cannot retrieve the concrete mapping for the provided %s: %s needs to define a valid %s", new Object[]{BaseEntityDefinition.class.getSimpleName(), BaseEntityDefinition.class.getSimpleName(), EntityType.class.getSimpleName()});
            List<String> mappings = new ArrayList<>();
            mappings.add(this.getMappingFor(((BaseEntityDefinition)abstractEntity).getEntityType()));
            return mappings;
        } else if (abstractEntity instanceof CustomEntityDefinition) {
            return this.getAllMappingsForCustomEntity((CustomEntityDefinition)abstractEntity);
        } else {
            throw new XatkitException(MessageFormat.format("{0} does not support the provided {1} {2}", this.getClass().getSimpleName(), EntityDefinition.class.getSimpleName(), abstractEntity.getClass().getSimpleName()));
        }
    }
}
