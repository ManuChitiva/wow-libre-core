package com.register.wowlibre.domain.dto;

import lombok.*;

import java.util.*;

@Builder
public class ServerVdpDto {
    public final String realmlist;
    public final String name;
    public final String type;
    public final String disclaimer;
    public final Map<String, String> information;
    public final Long accounts;
    public final Long onlineUsers;
    public final Long guilds;
    public final List<Card> cards;
    public final List<Event> events;

    @AllArgsConstructor
    public static class Card {
        public int id;
        public String value;
        public int icon;
        public String description;
    }

    @AllArgsConstructor
    public static class Event {
        public Long id;
        public String img;
        public String title;
        public String description;
        public String disclaimer;
    }
}
