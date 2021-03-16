package org.scd.repository;

import org.bson.types.ObjectId;
import org.scd.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends MongoRepository<Location, String> {

    Optional<Location> findById(Long id);

    Location save(Location location);
}
