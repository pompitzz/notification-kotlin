package me.sun.notificationservice

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import me.sun.notificationservice.config.MockKTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@MockKTest
class MockkTest {
    @MockK
    lateinit var myService: MyService

    @Test
    fun `stub, verify, confirmVerified가 정상적으로 동작한다`() {
        every { myService.getNumber() } returns 3

        assertThat(myService.getNumber()).isEqualTo(3)
        assertThat(myService.getNumber()).isEqualTo(3)

        verify(exactly = 2) { myService.getNumber() }
        confirmVerified(myService)
    }

    @Test
    fun `stubbing한 대상을 verify 하지 않았을 때 confirmVerified를 호출하면 AssertionError가 발생해야 한다`() {
        every { myService.getNumber() } returns 3
        myService.getNumber()

        assertThatThrownBy { confirmVerified(myService) }.isInstanceOf(AssertionError::class.java)
    }
}

class MyService {
    fun getNumber(): Int? = null
}
