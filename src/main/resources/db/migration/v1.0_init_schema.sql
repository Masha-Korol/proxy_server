CREATE TABLE "proxy_servers" (
"proxy_server_id" serial NOT NULL,
"ip" varchar(255) NOT NULL,
"port" integer NOT NULL,
"type" integer NOT NULL,
"anonymity" varchar(255) NOT NULL,
"uptime" FLOAT NOT NULL,
"available" varchar(255) NOT NULL,
"country_id" integer NOT NULL,
CONSTRAINT "proxy_servers_pk" PRIMARY KEY ("proxy_server_id")
);



CREATE TABLE "countries" (
"country_id" serial NOT NULL,
"name_en" varchar(255) NOT NULL,
CONSTRAINT "countries_pk" PRIMARY KEY ("country_id")
);



ALTER TABLE "proxy_servers" ADD CONSTRAINT "proxy_servers_fk0" FOREIGN KEY ("country_id") REFERENCES "countries"("country_id");

