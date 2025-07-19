package com.fsv.gestaodeeventosbackend.domain.repository;

import com.fsv.gestaodeeventosbackend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepositoty extends JpaRepository<Event, Long> {
}
