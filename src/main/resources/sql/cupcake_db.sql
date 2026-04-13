
-- Navn på database SKAL være "cupcake_db"
-- Hvis der kommer nye konstante dataer ind i db, så skal sql script opdateres. Således at alle kan få samme db-data.

BEGIN;


CREATE TABLE IF NOT EXISTS public.users
(
    user_id serial NOT NULL,
    username character varying,
    password character varying,
    balance double precision,
    administrator boolean,
    PRIMARY KEY (user_id)
    );


CREATE TABLE IF NOT EXISTS public.orders
(
    order_id serial,
    date date,
    user_id bigint,
    status character varying,
    PRIMARY KEY (order_id)
    );

CREATE TABLE IF NOT EXISTS public.orderlines
(
    order_id bigint NOT NULL,
    cupcake_name character varying,
    amount integer,
    total_price double precision
);

CREATE TABLE IF NOT EXISTS public.tops
(
    top_id serial,
    top_name character varying,
    description character varying,
    price double precision,
    PRIMARY KEY (top_id)
    );

CREATE TABLE IF NOT EXISTS public.bottoms
(
    bottom_id serial,
    bottom_name character varying,
    description character varying,
    price double precision,
    PRIMARY KEY (bottom_id)
    );

ALTER TABLE IF EXISTS public.orders
    ADD CONSTRAINT user_id FOREIGN KEY (user_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.orderlines
    ADD CONSTRAINT order_id FOREIGN KEY (order_id)
    REFERENCES public.orders (order_id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;

insert into bottoms (bottom_name, description, price) values ('Chocolate','Rig chokoladebund, blød og fyldig','5.00');
insert into bottoms (bottom_name, description, price) values ('Vanilla','Delikat vaniljebund, let og luftig','5.00');
insert into bottoms (bottom_name, description, price) values ('Nutmeg','Krydret muskatnøddesmag, varm og aromatisk','5.00');
insert into bottoms (bottom_name, description, price) values ('Pistacio','Pistaciebund med let saltet, nøddeagtig crunch','6.00');
insert into bottoms (bottom_name, description, price) values ('Almond','Ristet mandelbund, nøddeagtig og smøragtig','7.00');

INSERT INTO tops (top_name, description, price) VALUES ('Chocolate','Dekadent chokoladetopping, rig og cremet, perfekt til alle desserter','5.00');
INSERT INTO tops (top_name, description, price) VALUES ('Blueberry','Friske blåbær som topping, søde og let syrlige','5.00');
INSERT INTO tops (top_name, description, price) VALUES ('Raspberry','Saftige hindbær på toppen, giver en frisk frugtsmag','5.00');
INSERT INTO tops (top_name, description, price) VALUES ('Crispy','Knasende topping med sprød tekstur, tilføjer crunch til hver bid','6.00');
INSERT INTO tops (top_name, description, price) VALUES ('Strawberry','Lækre jordbær skiver som topping, sødmefuld og frisk','6.00');
INSERT INTO tops (top_name, description, price) VALUES ('Rum/Raisin','Rom- og rosintopping med aromatisk dybde og sødme','7.00');
INSERT INTO tops (top_name, description, price) VALUES ('Orange','Appelsinskiver og zest som topping, frisk og syrlig','8.00');
INSERT INTO tops (top_name, description, price) VALUES ('Lemon','Citrontopping med syrlig friskhed, perfekt til at løfte smagen','8.00');
INSERT INTO tops (top_name, description, price) VALUES ('Blue cheese','Intens blåskimmelost som topping, kraftfuld og karakterfuld','9.00');

ALTER TABLE IF EXISTS public.users
ALTER COLUMN balance SET DEFAULT 0.0;

ALTER TABLE IF EXISTS public.users
ALTER COLUMN administrator SET DEFAULT false;

insert into users (username, password, balance) values ('NicolaiNoah@gmail.com','12345', '200.0');
insert into users (username, password, balance, administrator) values ('NicolaiNoah@admin.com','12345', '1000.0', 'true');

insert into users (username, password, balance) values ('PeterS@gmail.com','12345', '300.0');
insert into users (username, password, balance, administrator) values ('PeterS@admin.com','12345', '100.0', 'true');

insert into users (username, password, balance) values ('Gabs@gmail.com','12345', '200.0');
insert into users (username, password, balance, administrator) values ('Gabs@admin.com','12345', '150.0', 'true');

insert into users (username, password, balance) values ('cuc@gmail.com','12345', '200.0');
insert into users (username, password, balance, administrator) values ('cuc@admin.com','12345', '500.0', 'true');

-- Order for users, with userId 1, 3, 5 and 7
INSERT INTO orders (user_id, status, date)
VALUES (1, 'complete', '2026-04-11')
    RETURNING order_id;

INSERT INTO orderlines (order_id, cupcake_name, amount, total_price)
VALUES
    (1, 'Chocolate, Chocolate', 2, 20),
    (1, 'Vanilla, Chocolate', 1, 10);


INSERT INTO orders (user_id, status, date)
VALUES (3, 'complete', '2026-04-10')
    RETURNING order_id;

INSERT INTO orderlines (order_id, cupcake_name, amount, total_price)
VALUES
    (2, 'Pistacio, Blueberry', 3, 33),
    (2, 'Vanilla, Lemon', 2, 22);


INSERT INTO orders (user_id, status, date)
VALUES (5, 'complete', '2026-04-13')
    RETURNING order_id;

INSERT INTO orderlines (order_id, cupcake_name, amount, total_price)
VALUES
    (3, 'Almond, Orange', 1, 15),
    (3, 'Chocolate, Raspberry', 2, 30);


INSERT INTO orders (user_id, status, date)
VALUES (7, 'complete', '2026-04-14')
    RETURNING order_id;

INSERT INTO orderlines (order_id, cupcake_name, amount, total_price)
VALUES
    (4, 'Nutmeg, Strawberry', 2, 22),
    (4, 'Pistacio, Crispy', 1, 12),
    (4, 'Pistacio, Blueberry', 2, 22),
    (4, 'Vanilla, Lemon', 1, 11);

END;

