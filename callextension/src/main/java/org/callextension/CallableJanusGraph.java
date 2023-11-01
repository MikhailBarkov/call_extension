package org.callextension;

import org.janusgraph.graphdb.database.StandardJanusGraph;
import org.janusgraph.graphdb.configuration.GraphDatabaseConfiguration;
import org.apache.tinkerpop.gremlin.structure.service.ServiceRegistry;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.HashMap;


public class CallableJanusGraph extends StandardJanusGraph {

    private ServiceRegistry registry;

    public CallableJanusGraph(GraphDatabaseConfiguration configuration) {
        super(configuration);
        registry = new CallServiceRegistry(this);
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return registry;
    }
}
