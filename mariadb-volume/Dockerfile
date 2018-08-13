FROM mariadb:10.1.35-bionic as build
LABEL maintainer="luk.zim91@gmail.com"

USER root
COPY init_db.sh /tmp/init_db.sh
RUN chmod +x /tmp/init_db.sh && \
    su -c '/tmp/init_db.sh' mysql && sync

######################################################################
FROM alpine:3.8 as data
COPY --from=build /tmp/datadir /opt/data
COPY entrypoint.sh /
ENTRYPOINT [ "/entrypoint.sh" ]
