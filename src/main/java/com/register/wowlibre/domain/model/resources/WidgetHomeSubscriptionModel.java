package com.register.wowlibre.domain.model.resources;

import java.util.*;

public class WidgetHomeSubscriptionModel {
    public String title;
    public String description;
    public String btn;
    public List<Benefits> benefits;

    public static class Benefits {
        public String img;
        public String alt;
        public String title;
    }
}
