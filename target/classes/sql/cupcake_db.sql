
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

END;