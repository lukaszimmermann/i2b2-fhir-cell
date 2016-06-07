create table IF NOT EXISTS Bundle_Status (
	id identity,
	patient_id varchar(140) not null,
	bundle_status_level varchar(20) not null, 
	created_date timestamp not null
);