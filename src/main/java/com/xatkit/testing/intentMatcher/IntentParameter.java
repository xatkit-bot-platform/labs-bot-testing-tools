package com.xatkit.testing.intentMatcher;

import lombok.Getter;

import java.util.List;

class IntentParameter {
    @Getter
    private List<String> fragments;
    @Getter
    private String entity;
    IntentParameter(List<String> fragment, String entity){
        this.entity = entity;
        this.fragments = fragment;
    }
}
