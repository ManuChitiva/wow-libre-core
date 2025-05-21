-- Crear el esquema 'platform' si no existe
CREATE SCHEMA IF NOT EXISTS platform;

-- Crear la tabla 'rol'
CREATE TABLE platform.rol
(
    id     bigint AUTO_INCREMENT NOT NULL,
    name   varchar(50) NOT NULL,
    status boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_rol_name UNIQUE (name)
);

INSERT INTO platform.rol (name, status)
VALUES ('ADMIN', TRUE);
INSERT INTO platform.rol (name, status)
VALUES ('CLIENT', TRUE);
INSERT INTO platform.rol (name, status)
VALUES ('SUPPORT', TRUE);

CREATE TABLE platform.user
(
    id            bigint auto_increment NOT NULL,
    country       varchar(30) NOT NULL,
    date_of_birth DATE        NOT NULL,
    first_name    varchar(30) NOT NULL,
    last_name     varchar(30) NOT NULL,
    cell_phone    varchar(20) NOT NULL,
    password      text        not null,
    status        boolean,
    verified      boolean,
    avatar_url    text,
    email         varchar(40) NOT NULL,
    rol_id        BIGINT      NOT null,
    language      varchar(10) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email),
    CONSTRAINT fk_user_rol_id FOREIGN KEY (rol_id) REFERENCES platform.rol (id),
    PRIMARY KEY (id)
);

CREATE TABLE platform.realm
(
    id                bigint auto_increment NOT NULL,
    name              varchar(40) NOT NULL,
    emulator          varchar(40) NOT NULL,
    expansion_id      integer     NOT NULL,
    type              varchar(40) NOT NULL,
    host              text        NOT NULL,
    port              integer     NOT NULL,
    api_key           varchar(80) NOT NULL,
    api_secret        varchar(80) NOT NULL,
    password          text        NOT NULL,
    jwt               text,
    expiration_date   date,
    refresh_token     text,
    avatar_url        text,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    web               text,
    realmlist         varchar(80) NOT NULL,
    status            boolean     NOT NULL,
    external_username varchar(50) NOT NULL,
    external_password text        NOT NULL,
    salt              VARBINARY(16),
    retry             integer,
    disclaimer        varchar(80),

    CONSTRAINT uq_realm_name_expansion UNIQUE (name, expansion_id),
    CONSTRAINT uq_realm_api_key UNIQUE (api_key),
    PRIMARY KEY (id)
);

CREATE TABLE platform.account_game
(
    id         bigint auto_increment NOT NULL,
    username   varchar(40) NOT NULL,
    game_email varchar(80) NOT NULL,
    status     boolean,
    account_id bigint      NOT NULL,
    realm_id   bigint      NOT NULL,
    user_id    bigint      NOT NULL,

    CONSTRAINT fk_account_game_user_id FOREIGN KEY (user_id) REFERENCES platform.user (id),
    CONSTRAINT fk_account_game_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id),
    CONSTRAINT uq_account_game_realm_account UNIQUE (realm_id, account_id),
    PRIMARY KEY (id)
);

CREATE TABLE platform.otp_verification
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      text NOT NULL,
    code       text,
    otp        text,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE platform.machine
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT NOT NULL,
    realm_id BIGINT NOT NULL,
    points   INT    NOT NULL,
    last_win TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_coins_user_realm UNIQUE (user_id, realm_id)
);

CREATE TABLE platform.realm_services
(
    id       bigint auto_increment NOT NULL,
    name     ENUM('BANK', 'SEND_LEVEL') NOT NULL,
    amount double NOT NULL,
    realm_id bigint NOT NULL,

    CONSTRAINT fk_realm_services_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id),
    CONSTRAINT uq_realm_services_name_realm UNIQUE (name, realm_id),
    PRIMARY KEY (id)
);

CREATE TABLE platform.realm_resources
(
    id            bigint AUTO_INCREMENT PRIMARY KEY,
    realm_id      bigint NOT NULL,
    resource_type ENUM('HEADER_LEFT', 'HEADER_CENTER', 'HEADER_RIGHT', 'LOGO', 'YOUTUBE_URL') NOT NULL,
    url           TEXT   NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_realm_resources_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id) ON DELETE CASCADE
);

CREATE TABLE platform.realm_events
(
    id          bigint AUTO_INCREMENT PRIMARY KEY,
    img         text         NOT NULL,
    title       VARCHAR(50)  NOT NULL,
    description VARCHAR(120) NOT NULL,
    disclaimer  varchar(200) NOT NULL,
    realm_id    bigint       NOT NULL,
    CONSTRAINT fk_realm_events_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id) ON DELETE CASCADE
);

CREATE TABLE platform.realm_details
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    realm_id   bigint      NOT NULL,
    `key`      VARCHAR(30) NOT NULL,
    `value`    VARCHAR(80) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_realm_details_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id) ON DELETE CASCADE
);

CREATE TABLE platform.credit_loans
(
    id               bigint auto_increment NOT NULL,
    account_game_id  bigint                              NOT NULL,
    character_id     bigint,
    realm_id         bigint,
    reference_serial varchar(60)                         NOT NULL,
    amount_transferred double NOT NULL,
    debt_to_pay double NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    payment_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    interests        INTEGER                             NOT NULL,
    status           boolean                             NOT NULL,
    send             boolean                             NOT NULL,

    CONSTRAINT uq_credit_loans_reference UNIQUE (reference_serial),
    CONSTRAINT fk_credit_loans_account_game_id FOREIGN KEY (account_game_id) REFERENCES platform.account_game (id),
    PRIMARY KEY (id)
);

CREATE TABLE platform.benefit_guild
(
    id         bigint auto_increment NOT NULL,
    realm_id   bigint      NOT NULL,
    guild_name varchar(70) NOT NULL,
    guild_id   bigint      NOT NULL,
    benefit_id bigint      NOT NULL,
    status     boolean     NOT NULL,
    CONSTRAINT uq_benefit_guild_realm_guild_benefit UNIQUE (realm_id, guild_id, benefit_id),
    CONSTRAINT fk_benefit_guild_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id),
    PRIMARY KEY (id)
);

CREATE TABLE platform.character_benefit_guild
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    account_id       BIGINT NOT NULL,
    character_id     BIGINT NOT NULL,
    benefit_guild_id BIGINT NOT NULL,
    benefit_send     BOOLEAN,

    CONSTRAINT uq_character_benefit_guild UNIQUE (character_id, account_id, benefit_guild_id),
    CONSTRAINT fk_character_benefit_guild_id FOREIGN KEY (benefit_guild_id) REFERENCES platform.benefit_guild (id),
    PRIMARY KEY (id)
);

CREATE TABLE platform.promotion
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference       varchar(40) NOT NULL,
    img_url         text        NOT NULL,
    name            varchar(30) NOT NULL,
    description     varchar(80) NOT NULL,
    btn_text        varchar(30) NOT NULL,
    send_item       boolean     NOT NULL,
    realm_id        bigint      NOT NULL,
    min_level       integer     NOT NULL,
    max_level       integer     NOT NULL,
    type            varchar(30) NOT NULL,
    amount double NOT NULL,
    class_character varchar(10) NOT NULL,
    level           integer,
    status          boolean     NOT NULL,
    language        varchar(2)  NOT NULL,
    CONSTRAINT uq_promotion_reference UNIQUE (reference)
);

CREATE TABLE platform.user_promotion
(
    id              bigint AUTO_INCREMENT NOT NULL,
    account_game_id bigint                              NOT NULL,
    character_id    bigint                              NOT NULL,
    promotion_id    bigint                              NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    realm_id        bigint                              NOT NULL,
    user_id         bigint                              NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_user_promotion_user_account_promo_realm UNIQUE (user_id, account_game_id, promotion_id, realm_id),
    CONSTRAINT fk_user_promotion_promotion_id FOREIGN KEY (promotion_id) REFERENCES platform.promotion (id),
    CONSTRAINT fk_user_promotion_account_game_id FOREIGN KEY (account_game_id) REFERENCES platform.account_game (id)
);

CREATE TABLE platform.promotion_item
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    code         varchar(30) NOT NULL,
    quantity     integer     NOT NULL,
    promotion_id bigint      NOT NULL,
    CONSTRAINT fk_promotion_item_promotion_id FOREIGN KEY (promotion_id) references platform.promotion (id)
);

CREATE TABLE platform.teleport
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    img_url text NOT NULL,
    name    VARCHAR(80) NOT NULL,
    position_x DOUBLE NOT NULL,
    position_y DOUBLE NOT NULL,
    position_z DOUBLE NOT NULL ,
    map     INT NOT NULL,
    orientation DOUBLE NOT NULL,
    zone    INT NOT NULL,
    area DOUBLE,
    realm_id BIGINT NOT NULL,
    faction ENUM('HORDE', 'ALLIANCE','ALL') NOT NULL,
    CONSTRAINT fk_teleport_realm_id FOREIGN KEY (realm_id) REFERENCES platform.realm (id),
    CONSTRAINT uq_teleport_name_realm UNIQUE (name, realm_id)
);

CREATE TABLE platform.faqs
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    question   TEXT         NOT NULL,
    answer     TEXT         NOT NULL,
    language   VARCHAR(255) NOT NULL,
    type ENUM('SUPPORT', 'SUBSCRIPTION') NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);
