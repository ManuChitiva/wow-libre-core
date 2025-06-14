package com.register.wowlibre.infrastructure.repositories.banners;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface BannersRepository extends CrudRepository<BannersEntity, Long> {

    List<BannersEntity> findByLanguage(String language);

}
