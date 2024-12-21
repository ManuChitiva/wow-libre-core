package com.register.wowlibre.application.services.dashboard;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.dashboard.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.server.*;
import com.register.wowlibre.domain.port.in.server_services.*;
import com.register.wowlibre.domain.port.out.credit_loans.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class DashboardService implements DashboardPort {

    /**
     * CreditLoans
     **/
    private final ObtainCreditLoans obtainCreditLoans;
    /**
     * ServerPort
     **/
    private final ServerPort serverPort;
    private final ServerServicesPort serverServicesPort;
    /**
     * Integrator Port
     **/
    private final IntegratorPort integratorPort;


    public DashboardService(ObtainCreditLoans obtainCreditLoans, ServerPort serverPort,
                            ServerServicesPort serverServicesPort, IntegratorPort integratorPort) {
        this.obtainCreditLoans = obtainCreditLoans;
        this.serverPort = serverPort;
        this.serverServicesPort = serverServicesPort;
        this.integratorPort = integratorPort;
    }

    @Override
    public LoansDto creditLoans(Long userId, Long serverId, int size, int page, String filter, boolean asc,
                                String transactionId) {

        Optional<ServerEntity> server = serverPort.findByIdAndUserId(serverId, userId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The server is not found or is not available please contact support.",
                    transactionId);
        }

        LoansDto loansDto = new LoansDto();
        loansDto.setLoans(Optional.ofNullable(serverServicesPort.findByNameAndServerId(ServerServices.BANK.getName(),
                serverId,
                transactionId)).map(ServerServicesModel::amount).orElse(null));
        loansDto.setUsers(obtainCreditLoans.findByServerIdAndPagination(serverId, size, page, filter, asc,
                transactionId).stream().map(credit -> new LoansDto.UsersCreditLoans(credit.getId(),
                credit.getUserId().getEmail(),
                credit.getTransactionDate(), credit.getDebtToPay() > 0, credit.getPaymentDate(),
                credit.getDebtToPay() / 10000.0)).toList());

        return loansDto;
    }

    @Override
    public void enableLoan(Long userId, Long serverId, Double loans, String service, String transactionId) {
        Optional<ServerEntity> server = serverPort.findByIdAndUserId(serverId, userId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The server is not found or is not available please contact support.",
                    transactionId);
        }

        serverServicesPort.updateOrCreateAmountByServerId(ServerServices.getName(service, transactionId).getName(),
                server.get(),
                loans,
                transactionId);
    }

    @Override
    public Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>> groupLoansAndPaymentsByDate(Long userId,
                                                                                                     Long serverId,
                                                                                                     String transactionId) {

        Optional<ServerEntity> server = serverPort.findByIdAndUserId(serverId, userId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The server is not found or is not available please contact support.",
                    transactionId);
        }

        List<CreditLoansEntity> creditLoansEntities = obtainCreditLoans.findByServerIdAndPagination(serverId, 500, 0,
                "ALL", true, transactionId);

        Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>> data = new TreeMap<>();

        for (CreditLoansEntity creditLoans : creditLoansEntities) {
            int year = creditLoans.getTransactionDate().getYear();

            String month = creditLoans.getTransactionDate().getMonth().toString();
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            int day = creditLoans.getTransactionDate().getDayOfMonth();

            data.computeIfAbsent(year, k -> new TreeMap<>());

            data.get(year).computeIfAbsent(month, k -> new TreeMap<>());

            data.get(year).get(month).computeIfAbsent(day, k -> new HashMap<>());

            Map<String, Integer> dayData = data.get(year).get(month).get(day);
            dayData.put("loans",
                    dayData.getOrDefault("loans", 0) + creditLoans.getAmountTransferred().intValue() / 10000);
            if (creditLoans.getDebtToPay() != null) {
                dayData.put("payments",
                        dayData.getOrDefault("payments", 0) + (creditLoans.getDebtToPay().intValue()) / 10000);
            }
        }

        return data;
    }

    @Override
    public AccountsGameDto accountsServer(Long userId, Long serverId, String filter, int size, int page,
                                          String transactionId) {


        Optional<ServerEntity> server = serverPort.findByIdAndUserId(serverId, userId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The server is not found or is not available please contact support.",
                    transactionId);
        }

        AccountsResponse accountsServer = integratorPort.accountsServer(server.get().getIp(),
                server.get().getJwt(), size, page, filter,
                transactionId);


        return new AccountsGameDto(accountsServer.getAccounts().stream().map(account ->
                new AccountGameDto(account.getId(), account.getUsername(), account.getEmail(),
                        Expansion.getById(Integer.parseInt(account.getExpansion())).getDisplayName()
                        , account.isOnline(), account.getFailedLogins(), account.getJoinDate(), account.getLastIp(),
                        account.getMuteReason(), account.getMuteBy(), account.isMute(), account.getLastLogin(),
                        account.getOs())).toList(), accountsServer.getSize());
    }
}
