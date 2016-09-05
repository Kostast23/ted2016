--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: item; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE item (
    id integer NOT NULL,
    name text,
    description text,
    buyprice integer,
    currentbid integer,
    firstbid integer,
    location text,
    lat double precision,
    lon double precision,
    country text,
    startdate date,
    enddate date,
    owner integer NOT NULL
);


ALTER TABLE public.item OWNER TO ted;

--
-- Name: bid; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE bid (
    id integer NOT NULL,
    item integer NOT NULL,
    "time" date,
    amount integer,
    owner integer NOT NULL
);


ALTER TABLE public.bid OWNER TO ted;

--
-- Name: bid_id_seq; Type: SEQUENCE; Schema: public; Owner: ted
--

CREATE SEQUENCE bid_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bid_id_seq OWNER TO ted;

--
-- Name: bid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ted
--

ALTER SEQUENCE bid_id_seq OWNED BY bid.id;


--
-- Name: category; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE category (
    id integer NOT NULL,
    name text
);


ALTER TABLE public.category OWNER TO ted;

--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: ted
--

CREATE SEQUENCE category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_id_seq OWNER TO ted;

--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ted
--

ALTER SEQUENCE category_id_seq OWNED BY category.id;


--
-- Name: item_id_seq; Type: SEQUENCE; Schema: public; Owner: ted
--

CREATE SEQUENCE item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.item_id_seq OWNER TO ted;

--
-- Name: item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ted
--

ALTER SEQUENCE item_id_seq OWNED BY item.id;


--
-- Name: item_pictures; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE item_pictures (
    filename text NOT NULL,
    image bytea NOT NULL,
    item integer
);


ALTER TABLE public.item_pictures OWNER TO ted;

--
-- Name: items_categories; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE items_categories (
    item integer NOT NULL,
    category integer NOT NULL
);


ALTER TABLE public.items_categories OWNER TO ted;

--
-- Name: message; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE message (
    id integer NOT NULL,
    message text,
    from_user integer NOT NULL,
    to_user integer NOT NULL
);


ALTER TABLE public.message OWNER TO ted;

--
-- Name: table_name_id_seq; Type: SEQUENCE; Schema: public; Owner: ted
--

CREATE SEQUENCE table_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.table_name_id_seq OWNER TO ted;

--
-- Name: table_name_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ted
--

ALTER SEQUENCE table_name_id_seq OWNED BY message.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: ted; Tablespace: 
--

CREATE TABLE "user" (
    id integer NOT NULL,
    username text,
    password text,
    salt text,
    admin boolean DEFAULT false,
    name text,
    surname text,
    email text,
    phone text,
    afm text,
    location text,
    lat double precision,
    lon double precision,
    country text,
    buyerrating integer DEFAULT 0,
    sellerrating integer DEFAULT 0,
    validated boolean DEFAULT false
);


ALTER TABLE public."user" OWNER TO ted;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: ted
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO ted;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ted
--

ALTER SEQUENCE users_id_seq OWNED BY "user".id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ted
--

ALTER TABLE ONLY bid ALTER COLUMN id SET DEFAULT nextval('bid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ted
--

ALTER TABLE ONLY category ALTER COLUMN id SET DEFAULT nextval('category_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ted
--

ALTER TABLE ONLY item ALTER COLUMN id SET DEFAULT nextval('item_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ted
--

ALTER TABLE ONLY message ALTER COLUMN id SET DEFAULT nextval('table_name_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ted
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Data for Name: bid; Type: TABLE DATA; Schema: public; Owner: ted
--



--
-- Name: bid_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ted
--

SELECT pg_catalog.setval('bid_id_seq', 1, false);


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: ted
--

INSERT INTO category VALUES (1, 'hey');
INSERT INTO category VALUES (2, 'hey');
INSERT INTO category VALUES (3, 'helo');


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ted
--

SELECT pg_catalog.setval('category_id_seq', 3, true);


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: ted
--

INSERT INTO item VALUES (5, 'itemmm', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO item VALUES (6, 'werld!!', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);


--
-- Name: item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ted
--

SELECT pg_catalog.setval('item_id_seq', 6, true);


--
-- Data for Name: item_pictures; Type: TABLE DATA; Schema: public; Owner: ted
--



--
-- Data for Name: items_categories; Type: TABLE DATA; Schema: public; Owner: ted
--

INSERT INTO items_categories VALUES (5, 2);
INSERT INTO items_categories VALUES (6, 3);


--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: ted
--



--
-- Name: table_name_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ted
--

SELECT pg_catalog.setval('table_name_id_seq', 1, false);


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: ted
--

INSERT INTO "user" VALUES (21, 'a', 'f3f3740d75620c361bde90930e26ffd6c4518263', '7f923b26eebaae47', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "user" VALUES (0, 'admin', 'e3ac012321d0c481cfcef761ac195f028be01b0e', '229e2ddd369d93b6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO "user" VALUES (22, 'user', '288ca4fcc489b8e68e064a213283a3da38835221', '79d55d1a6a0492fd', NULL, 'a', 'a', 'a@a.a', 'a', 'da', 'a', 123, 323, 'a', 0, 0, false);
INSERT INTO "user" VALUES (23, 'user1', '377b57fab63ba6bb9b2623099c05cf5934e44582', '56054919c5e20fa0', NULL, 'a', 'a', 'a@a.a', 'a', 'da', 'a', 123, 323, 'a', 0, 0, false);
INSERT INTO "user" VALUES (24, 'aa', '22626f50c662280e9eac3af528f57e0da379cac4', '4a4ea4ccbe8099df', NULL, 'a', 'a', 'aesmade@gmail.com', '+306948507664', 'sdaf', 'ευντου', 13, 13, 'Greece', 0, 0, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ted
--

SELECT pg_catalog.setval('users_id_seq', 24, true);


--
-- Name: bid_pkey; Type: CONSTRAINT; Schema: public; Owner: ted; Tablespace: 
--

ALTER TABLE ONLY bid
    ADD CONSTRAINT bid_pkey PRIMARY KEY (id);


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: ted; Tablespace: 
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: item_pictures_pkey; Type: CONSTRAINT; Schema: public; Owner: ted; Tablespace: 
--

ALTER TABLE ONLY item_pictures
    ADD CONSTRAINT item_pictures_pkey PRIMARY KEY (filename);


--
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: ted; Tablespace: 
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- Name: table_name_pkey; Type: CONSTRAINT; Schema: public; Owner: ted; Tablespace: 
--

ALTER TABLE ONLY message
    ADD CONSTRAINT table_name_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: ted; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: bid_id_uindex; Type: INDEX; Schema: public; Owner: ted; Tablespace: 
--

CREATE UNIQUE INDEX bid_id_uindex ON bid USING btree (id);


--
-- Name: category_id_uindex; Type: INDEX; Schema: public; Owner: ted; Tablespace: 
--

CREATE UNIQUE INDEX category_id_uindex ON category USING btree (id);


--
-- Name: item_id_uindex; Type: INDEX; Schema: public; Owner: ted; Tablespace: 
--

CREATE UNIQUE INDEX item_id_uindex ON item USING btree (id);


--
-- Name: item_pictures_filename_uindex; Type: INDEX; Schema: public; Owner: ted; Tablespace: 
--

CREATE UNIQUE INDEX item_pictures_filename_uindex ON item_pictures USING btree (filename);


--
-- Name: table_name_id_uindex; Type: INDEX; Schema: public; Owner: ted; Tablespace: 
--

CREATE UNIQUE INDEX table_name_id_uindex ON message USING btree (id);


--
-- Name: users_username_uindex; Type: INDEX; Schema: public; Owner: ted; Tablespace: 
--

CREATE UNIQUE INDEX users_username_uindex ON "user" USING btree (username);


--
-- Name: bid_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ted
--

ALTER TABLE ONLY bid
    ADD CONSTRAINT bid_user_id_fk FOREIGN KEY (owner) REFERENCES "user"(id);


--
-- Name: item_pictures_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ted
--

ALTER TABLE ONLY item_pictures
    ADD CONSTRAINT item_pictures_item_id_fk FOREIGN KEY (item) REFERENCES item(id);


--
-- Name: items_categories_category_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ted
--

ALTER TABLE ONLY items_categories
    ADD CONSTRAINT items_categories_category_id_fk FOREIGN KEY (category) REFERENCES category(id);


--
-- Name: items_categories_item_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ted
--

ALTER TABLE ONLY items_categories
    ADD CONSTRAINT items_categories_item_id_fk FOREIGN KEY (item) REFERENCES item(id);


--
-- Name: message_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ted
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_user_id_fk FOREIGN KEY (from_user) REFERENCES "user"(id);


--
-- Name: message_user_id_fk2; Type: FK CONSTRAINT; Schema: public; Owner: ted
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_user_id_fk2 FOREIGN KEY (to_user) REFERENCES "user"(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

