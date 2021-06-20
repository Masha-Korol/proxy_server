CREATE TABLE "proxy_servers" (
"proxy_server_id" serial NOT NULL,
"ip" varchar(255) NOT NULL,
"port" integer,
"type" integer NOT NULL default 15,
"anonymity" varchar(255) NOT NULL default 'All',
"uptime" FLOAT,
"available" varchar(255) NOT NULL default 'Yes',
"country_id" integer,
CONSTRAINT "proxy_servers_pk" PRIMARY KEY ("proxy_server_id")
);



CREATE TABLE "countries" (
"country_id" serial NOT NULL,
"name_en" varchar(255) NOT NULL,
CONSTRAINT "countries_pk" PRIMARY KEY ("country_id")
);



ALTER TABLE "proxy_servers" ADD CONSTRAINT "proxy_servers_fk0" FOREIGN KEY ("country_id") REFERENCES "countries"("country_id");

