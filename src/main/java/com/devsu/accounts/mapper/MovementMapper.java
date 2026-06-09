package com.devsu.accounts.mapper;

import com.devsu.accounts.domain.model.Movement;
import com.devsu.accounts.dto.MovementResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementResponse toResponse(Movement movement);

    List<MovementResponse> toResponseList(List<Movement> movements);
}