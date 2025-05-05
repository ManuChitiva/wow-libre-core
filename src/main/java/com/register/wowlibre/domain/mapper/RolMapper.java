package com.register.wowlibre.domain.mapper;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

public class RolMapper {
    public static RolModel toModel(RolEntity rolEntity) {
        if (rolEntity == null) {
            return null;
        }
        return new RolModel(rolEntity.getId(), rolEntity.getName(), rolEntity.isStatus());
    }

    public static RolEntity toEntity(RolModel rolModel) {
        if (rolModel == null) {
            return null;
        }
        RolEntity rolEntity = new RolEntity();
        rolEntity.setId(rolModel.id());
        rolEntity.setName(rolModel.name());
        rolEntity.setStatus(rolModel.status());
        return rolEntity;
    }
}
