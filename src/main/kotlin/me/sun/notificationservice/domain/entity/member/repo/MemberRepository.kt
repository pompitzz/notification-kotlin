package me.sun.notificationservice.domain.entity.member.repo

import me.sun.notificationservice.domain.entity.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>
