# bonify
backend challenge

1.Create database:

CREATE DATABASE banks
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

2. Create table "banks"

CREATE TABLE public.banks
(
    id text,
    name text,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.banks
    OWNER to postgres;

user: "postgres"
password: ""

3. sbt clean
4. sbt compile
5. sbt run
6. URL: http://localhost:9000/bank 
