package com.register.wowlibre.domain.port.in.dashboard;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;

import java.util.*;

public interface DashboardPort {
    LoansDto creditLoans(Long userId, Long serverId, int size, int page, String filter, boolean asc,
                         String transactionId);

    void enableLoan(Long userId, Long serverId, Double loans, String service, String transactionId);

    Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>> groupLoansAndPaymentsByDate(Long userId,
                                                                                              Long serverId,
                                                                                              String transactionId);

    AccountsGameDto accountsServer(Long userId, Long serverId, String filter, int size, int page,
                                   String transactionId);

    DashboardMetricsResponse metrics(Long userId, Long serverId, String transactionId);
}
