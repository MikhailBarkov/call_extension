package org.callextension;

import org.janusgraph.graphdb.management.JanusGraphManager;
import org.janusgraph.graphdb.database.StandardJanusGraph;
import org.janusgraph.graphdb.configuration.GraphDatabaseConfiguration;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.server.Settings;

import java.util.function.Function;


public class CallJanusGraphManager extends JanusGraphManager {

    public CallJanusGraphManager(Settings settings) {
        super(settings);
    }

    public void putGraph(String gName, Graph g) {
        System.out.println("Put Graph");

        if (((StandardJanusGraph) g).isOpen()) {((StandardJanusGraph) g).close();}
        super.putGraph(gName, new CallableJanusGraph(((StandardJanusGraph) g).getConfiguration()));
    }

    @Override
    public Graph openGraph(String gName, Function<String, Graph> thunk) {
        System.out.println("Open Graph");

        Graph graph = getGraph(gName);
        if (graph == null) {
            graph = thunk.apply(gName);
            putGraph(gName, graph);
        }
        return super.openGraph(gName, thunk);
    }
}
