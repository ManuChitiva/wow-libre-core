package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.banners.*;
import com.register.wowlibre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/banners")
public class BannersController {

    private final BannersPort bannersPort;

    public BannersController(BannersPort bannersPort) {
        this.bannersPort = bannersPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<BannerModel>>> banners(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        List<BannerModel> banners = bannersPort.findByLanguage(locale.getLanguage());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(banners, transactionId).ok().build());
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Void>> saveBanner(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody BannerDto bannerDto) {
        bannersPort.saveBanner(bannerDto, transactionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }

    @DeleteMapping("/{bannerId}")
    public ResponseEntity<GenericResponse<Void>> deleteBanner(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable Long bannerId) {
        bannersPort.deleteBanner(bannerId, transactionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }
}
