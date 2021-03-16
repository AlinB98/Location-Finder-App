package org.scd.service;

import org.bson.types.ObjectId;
import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.UserLocationDTO;

import java.util.List;
import java.util.Optional;

public interface LocationService {


    List<Location> getUserLocations(final String username,final String startDate,final String endDate) throws BusinessException;

    void createLocation(final UserLocationDTO userLocationDTO,final User user) throws BusinessException;

    Location getLocation(final String id) throws BusinessException;

    Location updateLocation(final String id, final UserLocationDTO userLocationDTO) throws BusinessException;

    void deleteLocation(final String id) throws BusinessException;


}
