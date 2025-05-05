package com.register.wowlibre.domain.enums;

public enum EmailTemplate {
    NEW_USER(1, "register.ftlh");

    private final int id;
    private final String templateName;

    EmailTemplate(int id, String templateName) {
        this.id = id;
        this.templateName = templateName;
    }

    public static String getTemplateNameById(int id) {
        for (EmailTemplate template : values()) {
            if (template.id == id) {
                return template.templateName;
            }
        }
        return NEW_USER.templateName;
    }
}
