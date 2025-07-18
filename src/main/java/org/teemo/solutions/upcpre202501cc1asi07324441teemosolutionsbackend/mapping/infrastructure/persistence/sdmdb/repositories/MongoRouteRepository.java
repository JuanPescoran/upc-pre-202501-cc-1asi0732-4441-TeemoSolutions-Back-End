package org.teemo.solutions.upcpre202501cc1asi07324441teemosolutionsbackend.mapping.infrastructure.persistence.sdmdb.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.teemo.solutions.upcpre202501cc1asi07324441teemosolutionsbackend.mapping.infrastructure.domain.RouteDocument;

import java.util.List;

@Repository
public class MongoRouteRepository implements RouteRepository {

    MongoTemplate mongoTemplate;

    public MongoRouteRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public RouteDocument getBetweenPorts(String port1, String port2) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("Home Port").is(port1).and("Destination Port").is(port2),
                Criteria.where("Home Port").is(port2).and("Destination Port").is(port1)
        );

        return mongoTemplate.findOne(
                Query.query(criteria),
                RouteDocument.class
        );
    }

    @Override
    public void saveAll(List<RouteDocument> routes) {
        // Inserta todos los documentos en una sola operación de lote.
        // Se añade una comprobación para evitar enviar una lista vacía.
        if (routes != null && !routes.isEmpty()) {
            mongoTemplate.insert(routes, RouteDocument.class);
            // Alternativamente: mongoTemplate.insertAll(routes);
        }
    }

    @Override
    public List<RouteDocument> getAll() {
        return mongoTemplate.findAll(RouteDocument.class);
    }

    @Override
    public boolean existsByHomePortAndDestinationPort(String homePort, String destinationPort) {
        Criteria criteria = Criteria.where("Home Port").is(homePort)
                .and("Destination Port").is(destinationPort);
        Query query = Query.query(criteria);
        return mongoTemplate.exists(query, RouteDocument.class);
    }
}
