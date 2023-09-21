CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT 50;

CREATE TABLE addresses
(
    user_id  UUID NOT NULL,
    address  VARCHAR(255),
    address2 VARCHAR(255),
    city     VARCHAR(255),
    country  VARCHAR(255),
    zip_code VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (user_id)
);

CREATE TABLE contacts
(
    user_id  UUID       NOT NULL,
    email    VARCHAR(255) NOT NULL,
    phone    VARCHAR(255),
    skype    VARCHAR(255),
    facebook VARCHAR(255),
    linkedin VARCHAR(255),
    website  VARCHAR(255),
    note     VARCHAR(255),
    CONSTRAINT pk_contacts PRIMARY KEY (user_id)
);

CREATE TABLE permissions
(
    id         UUID       NOT NULL,
    permission VARCHAR(255) NOT NULL,
    enabled    BOOLEAN,
    note       VARCHAR(255),
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE permissions_roles
(
    permission_id UUID NOT NULL,
    role_id       UUID NOT NULL
);

CREATE TABLE roles
(
    id   UUID       NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE users
(
    id          UUID       NOT NULL,
    username    VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    surname     VARCHAR(255) NOT NULL,
    gender      SMALLINT,
    birth_date  date,
    enabled     BOOLEAN,
    note        VARCHAR(255),
    creation_dt TIMESTAMP WITHOUT TIME ZONE,
    updated_dt  TIMESTAMP WITHOUT TIME ZONE,
    login_dt    TIMESTAMP WITHOUT TIME ZONE,
    secured     BOOLEAN,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    role_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (role_id, user_id)
);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE contacts
    ADD CONSTRAINT FK_CONTACTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE permissions_roles
    ADD CONSTRAINT fk_perrol_on_permission FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE permissions_roles
    ADD CONSTRAINT fk_perrol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);


-- create the enabled_users view
create view enabled_users as
select users.username,
       contacts.email,
       contacts.phone,
       users.creation_dt,
       users.updated_dt,
       users.login_dt,
       users.secured
from users
         inner join contacts on contacts.user_id = users.id
where users.enabled is true;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO permissions(id, permission, note) VALUES ('5a753403-d616-4b60-bc45-205ca614b669', 'LOGIN', 'User Login');
INSERT INTO permissions(id, permission, note) VALUES ('ada87f83-23a5-48ed-9a84-e55dcd7c6e05', 'VIEW_PROFILE', 'View user profile');
INSERT INTO permissions(id, permission, note) VALUES ('a3e7c5b4-d29d-4635-843c-d20053336d7e', 'ADMIN_USER_DATA', 'Manage user data');

INSERT INTO permissions(id, permission, note, enabled) VALUES ('67311ef8-d86e-4e2f-8289-2a5cbd142a0c', 'ADMIN_STATISTICS', 'View statistical graphs', false);

INSERT INTO roles(id, role) VALUES ('477ede88-03a8-4702-b8aa-670497771c28', 'USER');
INSERT INTO roles(id, role) VALUES ('2dd89f24-4d96-47fc-8a62-c15e2228d8aa', 'ADMINISTRATOR');

INSERT INTO permissions_roles(permission_id, role_id) VALUES ('5a753403-d616-4b60-bc45-205ca614b669', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO permissions_roles(permission_id, role_id) VALUES ('ada87f83-23a5-48ed-9a84-e55dcd7c6e05', '477ede88-03a8-4702-b8aa-670497771c28');

INSERT INTO permissions_roles(permission_id, role_id) VALUES ('5a753403-d616-4b60-bc45-205ca614b669', '2dd89f24-4d96-47fc-8a62-c15e2228d8aa');
INSERT INTO permissions_roles(permission_id, role_id) VALUES ('ada87f83-23a5-48ed-9a84-e55dcd7c6e05', '2dd89f24-4d96-47fc-8a62-c15e2228d8aa');
INSERT INTO permissions_roles(permission_id, role_id) VALUES ('a3e7c5b4-d29d-4635-843c-d20053336d7e', '2dd89f24-4d96-47fc-8a62-c15e2228d8aa');


INSERT INTO users(id, username, password, name, surname, gender) VALUES ('1af36f5b-19ae-40ff-a9ae-ed64c91d2204', 'andrea', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Andrea', 'Test', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('2d7bb435-ce39-4bbd-9fd8-44377a4680dd', 'mario', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Mario', 'Rossi', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('44fe4a75-edc8-4c9f-95ce-aaa3b34dce46', 'stefania', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Stefania', 'Verdi', 1);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('bf062eb0-bf71-4f1d-8a51-f16d0f86f759', 'veronica', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Veronica', 'Gialli', 1);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('c4032405-86e5-4802-99d9-00a727287d87', 'mark', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Mark', 'Green', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('770bddb0-d79a-414a-9852-8e9d8262fbb6', 'paul', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Paul', 'Ludwing', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('770f219c-c59b-4403-9ca1-3cd5bc9f5901', 'jennifer', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Jennifer', 'Red', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES ('77a03320-7df8-4b0a-bdab-968d44b3b7ee', 'karina', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Karina', 'Yellow', 1);

UPDATE users SET ENABLED = false WHERE id = '770bddb0-d79a-414a-9852-8e9d8262fbb6';

UPDATE users SET birth_date = '1977-08-14' WHERE id = '1af36f5b-19ae-40ff-a9ae-ed64c91d2204';
UPDATE users SET secured = true WHERE id = '1af36f5b-19ae-40ff-a9ae-ed64c91d2204';

INSERT INTO users_roles(user_id, role_id) VALUES ('1af36f5b-19ae-40ff-a9ae-ed64c91d2204', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('1af36f5b-19ae-40ff-a9ae-ed64c91d2204', '2dd89f24-4d96-47fc-8a62-c15e2228d8aa');

INSERT INTO users_roles(user_id, role_id) VALUES ('2d7bb435-ce39-4bbd-9fd8-44377a4680dd', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('44fe4a75-edc8-4c9f-95ce-aaa3b34dce46', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('bf062eb0-bf71-4f1d-8a51-f16d0f86f759', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('c4032405-86e5-4802-99d9-00a727287d87', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('770bddb0-d79a-414a-9852-8e9d8262fbb6', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('770f219c-c59b-4403-9ca1-3cd5bc9f5901', '477ede88-03a8-4702-b8aa-670497771c28');
INSERT INTO users_roles(user_id, role_id) VALUES ('77a03320-7df8-4b0a-bdab-968d44b3b7ee', '477ede88-03a8-4702-b8aa-670497771c28');



INSERT INTO contacts(user_id, email, phone, note) VALUES ('1af36f5b-19ae-40ff-a9ae-ed64c91d2204', 'andrea.test@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES ('2d7bb435-ce39-4bbd-9fd8-44377a4680dd', 'mario.rossi@gmail.com', NULL, 'test contact note on mario rossi');
INSERT INTO contacts(user_id, email, phone, note) VALUES ('44fe4a75-edc8-4c9f-95ce-aaa3b34dce46', 'stefania.verdi@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES ('bf062eb0-bf71-4f1d-8a51-f16d0f86f759', 'veronica.gialli@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES ('c4032405-86e5-4802-99d9-00a727287d87', 'mark.green@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES ('770bddb0-d79a-414a-9852-8e9d8262fbb6', 'paul.ludwing@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES ('770f219c-c59b-4403-9ca1-3cd5bc9f5901', 'jennifer.red@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES ('77a03320-7df8-4b0a-bdab-968d44b3b7ee', 'karina.yellow@gmail.com', NULL, NULL);

insert into addresses(user_id, address, address2, city, country, zip_code) values ('2d7bb435-ce39-4bbd-9fd8-44377a4680dd', 'Via Filzi 2', 'Borgo Teresiano', 'Florence', 'Italy', '50100');
insert into addresses(user_id, address, address2, city, country, zip_code) values ('770f219c-c59b-4403-9ca1-3cd5bc9f5901', 'Piazza Grande 12', 'Gran canal', 'Venice', 'Italy', '30100');
insert into addresses(user_id, address, address2, city, country, zip_code) values ('77a03320-7df8-4b0a-bdab-968d44b3b7ee', 'Via Roma 2', 'Borgo Teresiano', 'Trieste', 'Italy', '34100');
