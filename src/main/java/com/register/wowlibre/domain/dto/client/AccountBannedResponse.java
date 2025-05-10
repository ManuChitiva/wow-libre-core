package com.register.wowlibre.domain.dto.client;

import java.util.*;

public record AccountBannedResponse(Long id, Date bandate, Date unbandate, String bannedBy, String banReason,
                                    boolean active) {
}
