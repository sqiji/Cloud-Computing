package com.cst323.eventsapp.data;

import java.util.List;

import com.cst323.eventsapp.models.EventEntity;



public interface EventRepositoryInterface {

    List<EventEntity> findAll();
    void deleteById(Long id);
    EventEntity save(EventEntity event);
    EventEntity findById(Long id);
    boolean existsById(Long id);
    List<EventEntity> findByDescription(String description);
}
