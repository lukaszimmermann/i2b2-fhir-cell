.PHONY: init_volume

init_volume:
	docker volume create i2b2_fhir_mariadb
	docker build --pull --no-cache --rm -t fhir-cell-mariadb-volume mariadb-volume
	docker run --rm -v i2b2_fhir_mariadb:/tmp/volume fhir-cell-mariadb-volume

