package com.register.wowlibre.infrastructure.repositories.server_services;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ServerServicesRepository extends CrudRepository<RealmServicesEntity, Long> {

}
