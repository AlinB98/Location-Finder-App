package org.scd.controller;

import org.bson.types.ObjectId;
import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.UserLocationDTO;
import org.scd.model.security.CustomUserDetails;
import org.scd.service.LocationService;
import org.scd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController()
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService){ this.locationService=locationService; }

    @GetMapping()
    public ResponseEntity<List<Location>> getUserLocation(@RequestParam("username") final String username, @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate ) throws BusinessException {
        return ResponseEntity.ok(locationService.getUserLocations(username,startDate,endDate));
    }

    @PostMapping(path = "/current-location")
    public ResponseEntity<Location> createLocation(@RequestBody final UserLocationDTO userLocationDTO) throws BusinessException {

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        locationService.createLocation(userLocationDTO,currentUser);

        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @GetMapping(path = "/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable("id") String id) throws BusinessException {
        return ResponseEntity.ok(locationService.getLocation(id));
    }

    @PutMapping(path = "/current-location/{id}")
    public  ResponseEntity<Location> updateLocation(@PathVariable("id") String id,@RequestBody final UserLocationDTO userLocationDTO) throws BusinessException {
        return ResponseEntity.ok(locationService.updateLocation(id, userLocationDTO));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Location> deleteLocation(@PathVariable("id") String id) throws BusinessException {
        locationService.deleteLocation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
