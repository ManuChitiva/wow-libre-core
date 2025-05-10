package com.register.wowlibre.application.services.dashboard;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.dashboard.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.promotion.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.port.in.server_services.*;
import com.register.wowlibre.domain.port.in.user_promotion.*;
import com.register.wowlibre.domain.port.out.credit_loans.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.security.crypto.password.*;
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
    private final RealmPort realmPort;
    private final ServerServicesPort serverServicesPort;
    /**
     * Integrator Port
     **/
    private final IntegratorPort integratorPort;
    /**
     * UserPromotion Port
     **/
    private final UserPromotionPort userPromotionPort;

    private final PromotionPort promotionPort;
    private final PasswordEncoder passwordEncoder;

    public DashboardService(ObtainCreditLoans obtainCreditLoans, RealmPort realmPort,
                            ServerServicesPort serverServicesPort, IntegratorPort integratorPort,
                            UserPromotionPort userPromotionPort, PromotionPort promotionPort,
                            PasswordEncoder passwordEncoder) {
        this.obtainCreditLoans = obtainCreditLoans;
        this.realmPort = realmPort;
        this.serverServicesPort = serverServicesPort;
        this.integratorPort = integratorPort;
        this.userPromotionPort = userPromotionPort;
        this.promotionPort = promotionPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoansDto creditLoans(Long userId, Long serverId, int size, int page, String filter, boolean asc,
                                String transactionId) {

        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }

        LoansDto loansDto = new LoansDto();
        loansDto.setLoans(Optional.ofNullable(serverServicesPort.findByNameAndServerId(RealmServices.BANK,
                serverId,
                transactionId)).map(ServerServicesModel::amount).orElse(null));
        loansDto.setUsers(obtainCreditLoans.findByServerIdAndPagination(serverId, size, page, filter, asc,
                transactionId).stream().map(credit -> new LoansDto.UsersCreditLoans(credit.getId(),
                credit.getAccountGameId().getUserId().getEmail(),
                credit.getCreatedAt(), credit.getDebtToPay() > 0, credit.getPaymentDate(),
                credit.getDebtToPay() / 10000.0)).toList());

        return loansDto;
    }

    @Override
    public void enableLoan(Long userId, Long serverId, Double loans, String service, String transactionId) {
        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }

        serverServicesPort.updateOrCreateAmountByServerId(RealmServices.getName(service, transactionId),
                server.get(),
                loans,
                transactionId);
    }

    @Override
    public Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>> groupLoansAndPaymentsByDate(Long userId,
                                                                                                     Long serverId,
                                                                                                     String transactionId) {

        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }

        List<CreditLoansEntity> creditLoansEntities = obtainCreditLoans.findByServerIdAndPagination(serverId, 500, 0,
                "ALL", true, transactionId);

        Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>> data = new TreeMap<>();

        for (CreditLoansEntity creditLoans : creditLoansEntities) {
            int year = creditLoans.getCreatedAt().getYear();

            String month = creditLoans.getCreatedAt().getMonth().toString();
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            int day = creditLoans.getCreatedAt().getDayOfMonth();

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


        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }

        AccountsResponse accountsServer = integratorPort.accountsServer(server.get().getHost(),
                server.get().getJwt(), size, page, filter,
                transactionId);


        return new AccountsGameDto(accountsServer.getAccounts().stream().map(account ->
                new AccountGameDto(account.getId(), account.getUsername(), account.getEmail(),
                        Expansion.getById(Integer.parseInt(account.getExpansion())).getName()
                        , account.isOnline(), account.getFailedLogins(), account.getJoinDate(), account.getLastIp(),
                        account.getMuteReason(), account.getMuteBy(), account.isMute(), account.getLastLogin(),
                        account.getOs(), account.isBanned())).toList(), accountsServer.getSize());
    }

    @Override
    public DashboardMetricsDto metrics(Long userId, Long serverId, String transactionId) {

        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }

        DashboardMetricsResponse dashboard = integratorPort.dashboard(server.get().getHost(),
                server.get().getJwt(), transactionId);

        Long redeemedPromotion = userPromotionPort.countRedeemedPromotion(serverId, transactionId);

        return new DashboardMetricsDto(dashboard.getTotalUsers(), dashboard.getOnlineUsers(),
                dashboard.getTotalGuilds(), dashboard.getExternalRegistrations(), dashboard.getCharacterCount(),
                dashboard.getHordas(), dashboard.getAlianzas(), redeemedPromotion, dashboard.getRangeLevel());
    }

    @Override
    public void updateMail(Long userId, Long serverId, String username, String newMail, String transactionId) {

        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }


        integratorPort.updateMailAccount(
                server.get().getHost(), server.get().getJwt(),
                new AccountUpdateMailRequest(newMail, username),
                transactionId);
    }

    @Override
    public List<PromotionModel> getPromotions(Long userId, Long serverId, String transactionId) {

        Optional<RealmEntity> server = realmPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }


        return promotionPort.findByPromotionServerId(serverId, transactionId);
    }

    @Override
    public void bannedUser(AccountBanDto banDto, Long userId, String transactionId) {
        Optional<RealmEntity> serverProps = realmPort.findByIdAndUserId(banDto.getServerId(), userId, transactionId);

        if (serverProps.isEmpty() || !serverProps.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }
        RealmEntity server = serverProps.get();

        if (!passwordEncoder.matches(banDto.getPassword(), server.getPassword())) {
            throw new InternalException("The password is invalid", transactionId);
        }

        integratorPort.bannedUser(server.getHost(), server.getJwt(), banDto.getUsername(), banDto.getDays(),
                banDto.getHours(), banDto.getMinutes(), banDto.getSeconds(), banDto.getBannedBy(),
                banDto.getBanReason(), transactionId);
    }

    @Override
    public Map<String, String> getConfigs(Long userId, Long serverId, String url, boolean authServer,
                                          String transactionId) {

        Optional<RealmEntity> serverProps = realmPort.findByIdAndUserId(serverId, userId, transactionId);

        if (serverProps.isEmpty() || !serverProps.get().isStatus()) {
            throw new InternalException("The realm is not found or is not available please contact support.",
                    transactionId);
        }

        RealmEntity server = serverProps.get();


        return integratorPort.getConfigs(server.getHost(), server.getJwt(), url, authServer, transactionId);
    }
}
