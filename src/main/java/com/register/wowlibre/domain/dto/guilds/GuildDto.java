package com.register.wowlibre.domain.dto.guilds;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.model.resources.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class GuildDto {
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
    private Cta cta;
    private String formattedBankMoney;
    private String serverName;
    private Long serverId;
    private boolean isLeader;
    private String discord;
    private List<BenefitModel> benefits;

    public GuildDto(GuildResponse response, String serverName, Long serverId) {
        this.id = response.getId();
        this.name = response.getName();
        this.leaderName = response.getLeaderName();
        this.emblemStyle = response.getEmblemStyle();
        this.emblemColor = response.getEmblemColor();
        this.borderColor = response.getBorderColor();
        this.borderStyle = response.getBorderStyle();
        this.info = response.getInfo();
        this.motd = response.getMotd();
        this.createDate = response.getCreateDate();
        this.bankMoney = response.getBankMoney();
        this.members = response.getMembers();
        this.publicAccess = response.isPublicAccess();
        this.cta = response.getCta();
        this.formattedBankMoney = response.getFormattedBankMoney();
        this.serverName = serverName;
        this.discord = response.getDiscord();
        this.serverId = serverId;
    }

    public GuildDto(GuildModel response, String serverName, Long serverId) {
        this.id = response.getId();
        this.name = response.getName();
        this.leaderName = response.getLeaderName();
        this.emblemStyle = response.getEmblemStyle();
        this.emblemColor = response.getEmblemColor();
        this.borderColor = response.getBorderColor();
        this.borderStyle = response.getBorderStyle();
        this.info = response.getInfo();
        this.motd = response.getMotd();
        this.createDate = response.getCreateDate();
        this.bankMoney = response.getBankMoney();
        this.members = response.getMembers();
        this.publicAccess = response.isPublicAccess();
        this.cta = response.getCta();
        this.formattedBankMoney = response.getFormattedBankMoney();
        this.serverName = serverName;
        this.discord = response.getDiscord();
        this.serverId = serverId;
    }
}
