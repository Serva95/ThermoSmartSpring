CREATE TABLE thermo.authorities (
    id smallint NOT NULL auto_increment primary key,
    username varchar(64) NOT NULL,
    authority varchar(32) NOT NULL
);

CREATE TABLE thermo.orarionoff (
    id integer NOT NULL auto_increment primary key,
    roomid int NOT NULL,
    giorno smallint NOT NULL,
    orarioaccensionea time NOT NULL,
    orariospegnimentoa time NOT NULL,
    orarioaccensioneb time,
    orariospegnimentob time,
    orarioaccensionec time,
    orariospegnimentoc time
);

CREATE TABLE thermo.rooms (
    id integer NOT NULL auto_increment primary key,
    nome varchar(64) NOT NULL,
    maxtemp numeric(3,1) NOT NULL,
    mintemp numeric(3,1) NOT NULL,
    absolutemin numeric(3,1) NOT NULL,
    sensorid varchar(64),
    manualactive boolean DEFAULT false NOT NULL,
    manualinactive boolean DEFAULT false NOT NULL,
    manualoff boolean DEFAULT false NOT NULL,
    actualstatus boolean DEFAULT false NOT NULL
);

CREATE TABLE thermo.sensors (
    id varchar(64) not null primary key,
    nome varchar(64) NOT NULL,
    location varchar(64) NOT NULL
);

CREATE TABLE thermo.temps (
    id bigint NOT NULL auto_increment primary key,
    temp numeric(3,1) NOT NULL,
    createdat timestamp NOT NULL,
    sensorid varchar(64) DEFAULT NULL
);

CREATE TABLE thermo.users (
    id integer NOT NULL auto_increment primary key,
    email varchar(64) NOT NULL unique,
    password varchar(255) NOT NULL,
    username varchar(64) NOT NULL unique,
    enabled boolean DEFAULT true NOT NULL
);

CREATE TABLE thermo.vmcs (
    id character varying(64) NOT NULL primary key,
    statoattuale boolean DEFAULT false NOT NULL,
    impostazionefunzione boolean DEFAULT false NOT NULL,
    programmedontime time,
    programmedofftime time
);

CREATE TABLE thermo.info (
    id varchar(64) NOT NULL primary key,
    value varchar(1024) NOT NULL,
    time timestamp
);

ALTER TABLE thermo.authorities
    add FOREIGN KEY (username) REFERENCES thermo.users(username) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE thermo.orarionoff
    ADD FOREIGN KEY (roomid) REFERENCES thermo.rooms(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE thermo.rooms
    ADD FOREIGN KEY (sensorid) REFERENCES thermo.sensors(id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE thermo.temps
    ADD FOREIGN KEY (sensorid) REFERENCES thermo.sensors(id) ON UPDATE CASCADE ON DELETE CASCADE;