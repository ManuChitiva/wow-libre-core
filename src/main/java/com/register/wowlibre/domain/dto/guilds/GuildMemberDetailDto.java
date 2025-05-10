package com.register.wowlibre.domain.dto.guilds;

import lombok.*;

import java.util.*;

@Getter
@Builder
public class GuildMemberDetailDto {
    private Long id;
    private String name;
    private String leaderName;
    private Long emblemStyle;
    private Long emblemColor;
    private Long borderStyle;
    private Long borderColor;
    private String info;
    private String motd;
    private Date createDate;
    private Long bankMoney;
    private Long members;
    private boolean publicAccess;
    private String formattedBankMoney;
    private Boolean isLeader;
    private Boolean multiFaction;
    private String discord;
    private Integer claimedBenefits;
    private Integer availableBenefits;

}
