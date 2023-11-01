package org.callextension;

import org.apache.tinkerpop.gremlin.structure.service.ServiceRegistry;

import java.util.ArrayList;


class CallServiceRegistry extends ServiceRegistry {

    public CallServiceRegistry(CallableJanusGraph graph) {
        ArrayList<String> services = ServiceConfigUtil.loadServicesConfiguration();

        services.forEach(serviceConfigPath -> {
            try {
                registerService(ServiceConfigUtil.getServiceFactory(serviceConfigPath, graph));
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        });
    }
}
