package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concursoacm.domain.models.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {
}

