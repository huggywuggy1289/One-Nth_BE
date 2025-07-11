package com.onenth.OneNth.domain.region.repository;

import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByRegionName(String regionName);
}
