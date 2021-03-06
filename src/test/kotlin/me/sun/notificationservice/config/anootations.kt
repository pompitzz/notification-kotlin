package me.sun.notificationservice.config

import io.mockk.junit5.MockKExtension
import me.sun.notificationservice.common.config.SpringConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

// Unit Test 시 MockK Annotation을 사용하기 위함
@ExtendWith(MockKExtension::class)
annotation class MockKTest


// @DataJpaTest로 테스트 시 JPAQueryFactory를 빈으로 등록시키기 위함
@DataJpaTest
@Import(SpringConfig::class)
annotation class DataJpaTestWithQueryDsl
