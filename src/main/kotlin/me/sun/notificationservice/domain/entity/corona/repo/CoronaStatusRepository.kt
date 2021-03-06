package me.sun.notificationservice.domain.entity.corona.repo

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CoronaStatusRepository : JpaRepository<CoronaStatus, Long>, CoronaStatusRepositoryCustom
