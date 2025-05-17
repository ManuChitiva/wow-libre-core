package com.register.wowlibre.infrastructure.repositories.faqs;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface FaqsRepository extends CrudRepository<FaqsEntity, Long> {

    List<FaqsEntity> findByTypeAndLanguage(FaqType type, String language);
}
