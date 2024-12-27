package com.register.wowlibre.domain.port.in.google;

public interface GooglePort {
    boolean verifyCaptcha(String apiSecret, String token, String ip, String transactionId);
}
