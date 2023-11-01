package org.callextension;

import org.apache.tinkerpop.gremlin.structure.service.Service;

import java.util.LinkedHashMap;


public abstract class CallServiceFactory implements ServiceFactory {
    protected final CallableJanusGraph graph;
    protected final String name;
    protected final Map describeParams = new LinkedHashMap();

    protected CallServiceFactory(final CallJanusGraph graph, final String name) {
        this.graph = graph;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public TinkerServiceFactory addDescribeParams(final Map describeParams) {
        this.describeParams.putAll(describeParams);
        return this;
    }

    @Override
    public Map describeParams() {
        return describeParams;
    }
}
