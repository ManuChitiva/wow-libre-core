CREATE TABLE accounts.rol
(
    id     bigint AUTO_INCREMENT NOT NULL,
    name   varchar(50) NOT NULL,
    status boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT name_uq UNIQUE (name)
);

INSERT INTO accounts.rol (name, status)
VALUES ('ADMIN', TRUE);
INSERT INTO accounts.rol (name, status)
VALUES ('CLIENT', TRUE);
INSERT INTO accounts.rol (name, status)
VALUES ('SUPPORT', TRUE);
INSERT INTO accounts.rol (name, status)
VALUES ('SERVER', TRUE);


CREATE TABLE accounts.user
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
    language      varchar(50) NOT NULL,
    CONSTRAINT uq_email UNIQUE (email),
    CONSTRAINT uq_cell_phone UNIQUE (cell_phone),
    CONSTRAINT fk_user_rol_id FOREIGN KEY (rol_id) REFERENCES accounts.rol (id),
    PRIMARY KEY (id)
);

CREATE TABLE accounts.server
(
    id                bigint auto_increment NOT NULL,
    name              varchar(40) NOT NULL,
    emulator          varchar(40) NOT NULL,
    expansion         varchar(5)  NOT NULL,
    ip                text        NOT NULL,
    api_key           varchar(80) NOT NULL,
    api_secret        varchar(80) NOT NULL,
    password          text        NOT NULL,
    jwt               text,
    expiration_date   date,
    refresh_token     text,
    avatar            text,
    creation_date     date,
    web_site          text,
    realmlist         varchar(80),
    status            boolean,
    external_username varchar(50) NOT NULL,
    external_password varchar(50) NOT NULL,
    salt              VARBINARY(16),

    CONSTRAINT uq_email_server UNIQUE (name, expansion),
    CONSTRAINT uq_apikey_server UNIQUE (api_key),
    PRIMARY KEY (id)
);

CREATE TABLE accounts.account_game
(
    id         bigint auto_increment NOT NULL,
    server_id  bigint      NOT NULL,
    account_id bigint      NOT NULL,
    username   varchar(40) NOT NULL,
    status     boolean,
    user_id    bigint      NOT NULL,

    CONSTRAINT fk_account_game_user_id FOREIGN KEY (user_id) REFERENCES accounts.user (id),
    CONSTRAINT fk_account_game_server_id FOREIGN KEY (server_id) REFERENCES accounts.server (id),
    CONSTRAINT uq_server_and_account_and_user UNIQUE (server_id, account_id),
    PRIMARY KEY (id)
);



CREATE TABLE accounts.server_services
(
    id        bigint auto_increment NOT NULL,
    name      varchar(50) NOT NULL,
    amount double NOT NULL,
    server_id bigint      NOT NULL,

    CONSTRAINT fk_server_services_server_id FOREIGN KEY (server_id) REFERENCES accounts.server (id),
    CONSTRAINT uq_name_server_id UNIQUE (name, server_id),
    PRIMARY KEY (id)
);


CREATE TABLE accounts.credit_loans
(
    id               bigint auto_increment NOT NULL,
    user_id          bigint      NOT NULL,
    reference_serial varchar(60) NOT NULL,
    account_id       bigint,
    character_id     bigint,
    server_id        bigint,
    amount_transferred double NOT NULL,
    debt_to_pay double NOT NULL,
    transaction_date date        NOT NULL,
    payment_date     date        NOT NULL,
    interests        INTEGER     NOT NULL,
    status           boolean     NOT NULL,
    send             boolean     NOT NULL,

    CONSTRAINT fk_credit_loans_user_id FOREIGN KEY (user_id) REFERENCES accounts.user (id),
    CONSTRAINT uq_credit_loans_reference_serial UNIQUE (reference_serial),
    PRIMARY KEY (id)
);



create table benefit_guild
(
    id         bigint auto_increment NOT NULL,
    server_id  bigint      not null,
    guild_name varchar(70) not null,
    guild_id   bigint      not null,
    benefit_id bigint      not null,
    status     boolean     not null,
    CONSTRAINT uq_benefit_guild_server_id_guild_id_benefit_id UNIQUE (server_id, guild_id, benefit_id),
    CONSTRAINT fk_benefit_guild_server_id FOREIGN KEY (server_id) REFERENCES accounts.server (id),
    PRIMARY KEY (id)

);

create table character_benefit_guild
(
    id               bigint auto_increment NOT NULL,
    character_id     bigint not null,
    account_id       bigint not null,
    benefit_guild_id bigint not null,
    benefit_send     boolean,

    CONSTRAINT uq_character_and_account_and_benefit_id UNIQUE (character_id, account_id, benefit_guild_id),
    CONSTRAINT fk_benefit_guild_id FOREIGN KEY (benefit_guild_id) REFERENCES accounts.benefit_guild (id),
    PRIMARY KEY (id)

)

CREATE TABLE accounts.user_promotion
(
    id           bigint AUTO_INCREMENT NOT NULL,
    user_id      bigint NOT NULL,
    account_id   bigint NOT NULL,
    promotion_id bigint NOT NULL,
    created_at   date   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_promotion_uq UNIQUE (user_id, account_id, promotion_id)
);

CREATE TABLE user_validation
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY, -- Clave primaria con auto_increment
    email VARCHAR(255) NOT NULL,             -- Columna para el email (puedes ajustar el tamaño según sea necesario)
    code  VARCHAR(255),                      -- Columna para el código
    otp   VARCHAR(255)                       -- Columna para el OTP
)

ALTER TABLE accounts.user_promotion
    ADD COLUMN character_id bigint NOT NULL;


CREATE TABLE accounts.user_server_coins
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT NOT NULL,
    server_id BIGINT NOT NULL,
    coint     INT,
    last_win  DATETIME,
    UNIQUE (user_id, server_id)
);


ALTER TABLE accounts.server
    ADD COLUMN user_id bigint;

ALTER TABLE accounts.server
    ADD COLUMN type varchar(40);




CREATE TABLE accounts.promotion
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference       varchar(40) NOT NULL,
    img             text        NOT NULL,
    name            varchar(30) NOT NULL,
    description     varchar(80) NOT NULL,
    btn_text        varchar(30) NOT NULL,
    send_item       boolean NOT NULL,
    server_id       bigint      NOT NULL,
    min_level       integer     NOT NULL,
    max_level       integer     NOT NULL,
    type            varchar(30) NOT NULL,
    amount double NOT NULL,
    class_character varchar(10) NOT NULL,
    level integer,
    status boolean NOT NULL,
    language varchar (2) NOT NULL,
    CONSTRAINT uq_reference_promotion UNIQUE (reference)
)


CREATE TABLE accounts.promotion_item
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code varchar (30) NOT NULL,
    quantity integer NOT NULL,
    promotion_id bigint NOT NULL,
    CONSTRAINT fk_promotion_id FOREIGN KEY (promotion_id) references accounts.promotion (id)
)

ALTER TABLE accounts.server
    ADD retry integer;