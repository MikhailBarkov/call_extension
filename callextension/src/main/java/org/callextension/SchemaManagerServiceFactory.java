package org.callextension;

import org.apache.tinkerpop.gremlin.structure.service.Service;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;


public class SchemaManagerServiceFactory implements Service.ServiceFactory {

    private String NAME;

    public SchemaManagerServiceFactory(String serviceName, String serviceURL) {
        this.serviceURL = serviceURL;
        this.NAME = serviceName;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Set<Service.Type> getSupportedTypes() {
        HashSet<Service.Type> types = new HashSet<Service.Type>();
        types.add(Service.Type.Start);
        return types;
    }

    @Override
    public PredictService createService(final boolean isStart, final Map params) {
        if (isStart) {
            return new SchemaManagerService(params);
        }
        else {
            throw new UnsupportedOperationException(Service.Exceptions.cannotUseMidTraversal);
        }
    }
}