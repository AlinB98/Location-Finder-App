package org.scd.service;

import org.scd.config.exception.BusinessException;
import org.scd.model.User;
import org.scd.model.dto.UserLocationDTO;
import org.scd.model.dto.UserLoginDTO;
import org.scd.model.dto.UserRegisterDTO;
import org.scd.repository.LocationRepository;
import org.scd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;


public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,final BCryptPasswordEncoder passwordEncoder,final LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.locationRepository=locationRepository;
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User login(UserLoginDTO userLoginDTO) throws BusinessException {

        if (Objects.isNull(userLoginDTO)) {
            throw new BusinessException(401, "Body null !");
        }

        if (Objects.isNull(userLoginDTO.getEmail())) {
            throw new BusinessException(400, "Email cannot be null ! ");
        }

        if (Objects.isNull(userLoginDTO.getPassword())) {
            throw new BusinessException(400, "Password cannot be null !");
        }

        final User user = userRepository.findByEmail(userLoginDTO.getEmail());


        if (Objects.isNull(user)) {
            throw new BusinessException(404, "User not found !");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "Bad credentials !");
        }


        return user;
    }

    @Override
    public User register(UserRegisterDTO userRegisterDTO) throws BusinessException{

         if (Objects.isNull(userRegisterDTO.getEmail())|| userRegisterDTO.getEmail().length()==0){
             throw new BusinessException(400, "Email cannot be null ! ");
         }

        if (Objects.isNull(userRegisterDTO.getPassword()) || userRegisterDTO.getPassword().length()==0){
            throw new BusinessException(400, "Password cannot be null ! ");
        }

        if (Objects.isNull(userRegisterDTO.getFirstName()) || userRegisterDTO.getFirstName().length()==0){
            throw new BusinessException(400, "Firstname cannot be null ! ");
        }

        if (Objects.isNull(userRegisterDTO.getLastName()) || userRegisterDTO.getLastName().length()==0){
            throw new BusinessException(400, "Lastname cannot be null ! ");
        }

        if(Objects.nonNull(userRepository.findByEmail(userRegisterDTO.getEmail()))){
            throw new BusinessException(409, "Email already used ! ");
        }

        final User unsavedUser = new User(userRegisterDTO.getFirstName(),userRegisterDTO.getLastName(),userRegisterDTO.getEmail(),passwordEncoder.encode(userRegisterDTO.getPassword()));
        List<String> roles = new ArrayList<>();
        roles.add("BASIC_USER");
        unsavedUser.setRole(roles);
        userRepository.save(unsavedUser);

        final User savedUser = userRepository.findByEmail(userRegisterDTO.getEmail());

        return savedUser;
    }

}
