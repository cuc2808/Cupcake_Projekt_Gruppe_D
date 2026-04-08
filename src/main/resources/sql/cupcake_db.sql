
-- Navn på database SKAL være cupcake_db
-- Hvis der kommer nye konstante dataer ind i db, så skal sql script opdateres. Således at alle kan få samme db-data.

BEGIN;


CREATE TABLE IF NOT EXISTS public.users
(
    user_id serial NOT NULL,
    username character varying,
    password character varying,
    balance bigint,
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

END;