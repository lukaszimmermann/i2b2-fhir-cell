.PHONY: init_volume

init_volume:
	docker volume create i2b2_fhir_mariadb
	docker build --pull --no-cache --rm -t fhir-cell-mariadb-volume mariadb-volume
	docker run --rm -v i2b2_fhir_mariadb:/tmp/volume fhir-cell-mariadb-volume

fhir_cell:
	docker build --no-cache --pull --rm -t i2b2-fhir-cell:0.1 fhir-cell


