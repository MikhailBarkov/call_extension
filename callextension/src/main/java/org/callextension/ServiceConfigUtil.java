package org.callextension;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import org.apache.tinkerpop.gremlin.structure.service.Service;
import org.janusgraph.util.system.ConfigurationUtil;
import org.janusgraph.graphdb.database.StandardJanusGraph;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.Class;


public class ServiceConfigUtil {

    private static String SERVICES_CONFIG_PATH = "conf/services/services.yaml";
    private static String FACTORY_CLASS_FIELD_NAME = "service";

    private static class ServicesConfig {
        public ArrayList<String> services;
    }

    public static ArrayList<String> loadServicesConfiguration() {
        ArrayList<String> services = new ArrayList();

        try {
            InputStream stream = new FileInputStream(SERVICES_CONFIG_PATH);
            Objects.requireNonNull(stream);

            Yaml yaml = new Yaml(new Constructor(ServicesConfig.class, new LoaderOptions()));

            ServicesConfig servicesConfig = yaml.loadAs(stream, ServicesConfig.class);
            services = servicesConfig.services;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }

    public static ServiceFactory getServiceFactory(String serviceConfigPath, StandardJanusGraph graph) throws ConfigurationException, IllegalArgumentException {
        PropertiesConfiguration properties = ConfigurationUtil.loadPropertiesConfig(serviceConfigPath);
        return getServiceFactory(properties, graph);
    }

    public static ServiceFactory getServiceFactory(PropertiesConfiguration properties, StandardJanusGraph graph) throws ConfigurationException, IllegalArgumentException {
        String serviceFactoryClassName = properties.getString(FACTORY_CLASS_FIELD_NAME);

        int size = properties.size();

        Object[] constructorArgs = new Object[size];
        Class[] constructorClasses = new Class[size];

        Iterator<String> keys = properties.getKeys();

        for (int i = 0; i < size; i++) {
            String argName = keys.next();

            Object arg = properties.getProperty(argName);
            constructorArgs[i] = arg;
            constructorClasses[i] = arg.getClass();
        }

        Object[] withGraphArgs = new Object[size+1];
        System.arraycopy(constructorArgs, 0, withGraphArgs, 1, size);
        withGraphArgs[0] = graph;

        Class[] withGraphClasses = new Class[size+1];
        System.arraycopy(constructorClasses, 0, withGraphClasses, 1, size);
        withGraphClasses[0] = StandardJanusGraph.class;

        if (ConfigurationUtil.hasConstructor(serviceFactoryClassName, withGraphClasses)) {
            return ConfigurationUtil.instantiate(serviceFactoryClassName, withGraphArgs, withGraphClasses);
        } else {
            return ConfigurationUtil.instantiate(serviceFactoryClassName, constructorArgs, constructorClasses);
        }
    }
}
