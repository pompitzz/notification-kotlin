package me.sun.notificationservice.domain.entity.corona

import org.springframework.data.jpa.repository.JpaRepository

interface CoronaStatusRepository: JpaRepository<CoronaStatus, Long>
