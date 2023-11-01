FROM maven:3.6.0-jdk-8-slim as java_builder

COPY /callextension/ /callextension/
RUN mvn -f /callextension/pom.xml package


FROM janusgraph/janusgraph:1.0.0-rc1

ENV JANUS_HOME=/opt/janusgraph \
    JANUS_CONFIG_DIR=/etc/opt/janusgraph \
    JANUS_LIB_DIR=/opt/janusgraph/lib/ \
    JAVA_OPTIONS=-Xms512m

COPY --from=java_builder /callextension/target/janusgraph-callextension-0.0.1.jar ${JANUS_LIB_DIR}/janusgraph-callextension-0.0.1.jar

USER root

COPY conf/gremlin-server/gremlin-server.yaml ${JANUS_CONFIG_DIR}/janusgraph-server.yaml
COPY conf/services/services.yaml ${JANUS_CONFIG_DIR}/services.yaml
COPY conf/janusgraph.properties ${JANUS_CONFIG_DIR}/janusgraph.properties
