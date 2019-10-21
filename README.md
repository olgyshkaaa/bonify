# bonify
backend challenge

1.Create database:

```sql
CREATE DATABASE banks
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
 ```

2. Create table "banks"

```sql
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
```

user: "postgres"
password: ""

3. Create resource folder inside the application folder

4.```sbt clean ```

5.  ```sbt compile ```

6.  ```sbt run ```

7. URL: http://localhost:9000/bank [POST, GET]
   Key for POST request: "fileupload" 
