package org.scd.service;

import org.bson.types.ObjectId;
import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.UserLocationDTO;
import org.scd.repository.LocationRepository;
import org.scd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class LocationServiceImpl implements LocationService{

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(final UserRepository userRepository,final LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.locationRepository=locationRepository;
    }

    @Override
    public List<Location> getUserLocations(String username,String startDate, String endDate) throws BusinessException{

        if(username.isEmpty()){
            throw new BusinessException(400,"Username cannot be null ! ");
        }

        if(startDate.isEmpty()){
            throw new BusinessException(400,"Starting date cannot be null ! ");
        }

        if(endDate.isEmpty()){
            throw new BusinessException(400,"Ending date cannot be null ! ");
        }

        Date sdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(startDate,new ParsePosition(0));
        Date edate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(endDate,new ParsePosition(0));

        final User user = userRepository.findByEmail(username);
        final List<Location> allLocations = locationRepository.findAll();

        if(allLocations.isEmpty()){
            throw new BusinessException(404,"Locations not found ! ");
        }

        List<Location> filteredLocations = new ArrayList();

        for(Location loc : allLocations){
            if(loc.getUserId().toString().equals(user.getId().toString())) {
                if (loc.string2date(loc.getDate()).after(sdate) && loc.string2date(loc.getDate()).before(edate)) {
                    filteredLocations.add(loc);
                }
            }
        }
        return filteredLocations;
    }

    @Override
    public void createLocation(UserLocationDTO userLocationDTO,User user) throws BusinessException {

        if (Objects.isNull(userLocationDTO)) {
            throw new BusinessException(401, "Bad request !");
        }

        if (Objects.isNull(userLocationDTO.getDate())){
            throw new BusinessException(400, "Date cannot be null ! ");
        }

        if (Objects.isNull(userLocationDTO.getLat())){
            throw new BusinessException(400, "Latitude cannot be null ! ");
        }

        if (Objects.isNull(userLocationDTO.getLng())){
            throw new BusinessException(400, "Longitude cannot be null ! ");
        }


        final Location location = new Location(userLocationDTO.getLat(),userLocationDTO.getLng(),userLocationDTO.getDate());
        location.setUserId(user.getId());
        locationRepository.save(location);
    }

    @Override
    public Location getLocation(String id) throws BusinessException {

        if (Objects.isNull(id)) {
            throw new BusinessException(401, "Bad request !");
        }

        final Optional<Location> locationContainer = locationRepository.findById(id);

        if (!locationContainer.isPresent()) {
            throw new BusinessException(404, "Location not found !");
        }

        Location location = locationContainer.get();

        return  location;
    }

    @Override
    public Location updateLocation(String id, UserLocationDTO userLocationDTO) throws BusinessException {

        if (Objects.isNull(userLocationDTO)) {
            throw new BusinessException(401, "Bad request !");
        }

        if (Objects.isNull(userLocationDTO.getDate())){
            throw new BusinessException(400, "Date cannot be null ! ");
        }

        if (Objects.isNull(userLocationDTO.getLat())){
            throw new BusinessException(400, "Latitude cannot be null ! ");
        }

        if (Objects.isNull(userLocationDTO.getLng())){
            throw new BusinessException(400, "Longitude cannot be null ! ");
        }

        final Optional<Location> locationContainer = locationRepository.findById(id);

        if (!locationContainer.isPresent()) {
            throw new BusinessException(404, "Location not found  !");
        }

        Location location = locationContainer.get();
        location.setDate(userLocationDTO.getDate());
        location.setLat(userLocationDTO.getLat());
        location.setLng(userLocationDTO.getLng());
        locationRepository.save(location);

        return location;
    }

    @Override
    public void deleteLocation(String id) throws BusinessException {

        if (Objects.isNull(id)) {
            throw new BusinessException(401, "Bad request !");
        }

        Optional<Location> location = locationRepository.findById(id);

        if (!location.isPresent()) {
            throw new BusinessException(404, "Location not found  !");
        }

        locationRepository.deleteById(id);
    }
}
