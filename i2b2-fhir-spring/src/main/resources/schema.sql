create table IF NOT EXISTS Spittle (
	id identity,
	message varchar(140) not null,
	created_at timestamp not null,
	latitude double,
	longitude double
);

create table IF NOT EXISTS Spitter (
	id identity,
	username varchar(20) unique not null,
	password varchar(20) not null,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	email varchar(30) not null
);

create table IF NOT EXISTS Bundle_Status (
	id identity,
	patient_id varchar(140) not null,
	bundle_status_level varchar(20) not null, 
	created_date timestamp not null
);