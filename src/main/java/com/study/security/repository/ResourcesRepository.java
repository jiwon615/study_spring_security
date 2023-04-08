package com.study.security.repository;

import com.study.security.domain.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {
    List<Resources> findAllResources();
}
