CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR(250)                                        NOT NULL,
    email VARCHAR(254)                                        NOT NULL
);
create unique index if not exists USER_EMAIL_UINDEX on USERS (email);

CREATE TABLE IF NOT EXISTS categories
(
    id   bigint generated by default as identity primary key not null,
    name VARCHAR(50)                                         NOT NULL
);
create unique index if not exists CATEGORIES_NAME_UINDEX on categories (name);

create table if not exists events
(
    id                 bigint generated always as identity primary key not null,
    annotation         varchar(2000)                                   not null,
    category           bigint references categories,
    create_date        timestamp without time zone,
    description        varchar(7000),
    event_date         timestamp without time zone                     not null,
    initiator          bigint references users,
    lat                float4                                          not null,
    lon                float4                                          not null,
    paid               boolean                                         not null,
    participant_limit  bigint,
    published_date     timestamp without time zone,
    request_moderation boolean,
    state              varchar(33),
    title              varchar(120)                                    not null,
    FOREIGN KEY (category) REFERENCES categories (id) ON DELETE CASCADE,
    FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE
);

create table if not exists comments
(
    id          bigint generated always as identity primary key not null,
    comment     varchar(9999)                                   not null,
    commentator bigint references users                         not null,
    create_date timestamp without time zone                     not null
);

CREATE TABLE IF NOT EXISTS comments_events
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    comment BIGINT                                          NOT NULL,
    event   BIGINT                                          NOT NULL,
    FOREIGN KEY (comment) REFERENCES comments (id) ON DELETE CASCADE,
    FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id  BIGINT references users             NOT NULL,
    event_id BIGINT references events            NOT NULL,
    created  TIMESTAMP                           NOT NULL,
    status   VARCHAR(50)                         NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(50)                             NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS compilations_events
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    compilation BIGINT                                          NOT NULL,
    event       BIGINT                                          NOT NULL,
    FOREIGN KEY (compilation) REFERENCES compilations (id) ON DELETE CASCADE,
    FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE
);