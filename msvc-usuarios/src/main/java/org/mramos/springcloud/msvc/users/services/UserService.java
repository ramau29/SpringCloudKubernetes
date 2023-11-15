package org.mramos.springcloud.msvc.users.services;

import org.mramos.springcloud.msvc.users.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> listAll();
    Optional<User> byId(Long id);
    User save(User user);
    void delete(Long id);

    Optional<User> getByEmail(String email);
    boolean existsByEmail(String email);

    List<User> listByIds(Iterable<Long> ids);

}
