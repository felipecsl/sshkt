package sshkt

import com.google.common.truth.Truth.assertThat
import net.schmizz.sshj.SSHClient
import org.junit.Test

class HostSpecSelectionTest {
  @Test fun simple() {
    val spec = HostSpecSelection.parse("localhost")
    assertThat(spec.hostname()).isEqualTo("localhost")
    assertThat(spec.port()).isEqualTo(SSHClient.DEFAULT_PORT)
    assertThat(spec.username()).isEqualTo(null)
  }

  @Test fun ipWithUsername() {
    val spec = HostSpecSelection.parse("user@127.0.0.1")
    assertThat(spec.hostname()).isEqualTo("127.0.0.1")
    assertThat(spec.port()).isEqualTo(SSHClient.DEFAULT_PORT)
    assertThat(spec.username()).isEqualTo("user")
  }

  @Test fun ipWithUsernameAndPort() {
    val spec = HostSpecSelection.parse("foo@10.2.41.4:32")
    assertThat(spec.hostname()).isEqualTo("10.2.41.4")
    assertThat(spec.port()).isEqualTo(32)
    assertThat(spec.username()).isEqualTo("foo")
  }

  @Test fun ipWithPort() {
    val spec = HostSpecSelection.parse("20.2.41.4:32")
    assertThat(spec.hostname()).isEqualTo("20.2.41.4")
    assertThat(spec.port()).isEqualTo(32)
    assertThat(spec.username()).isEqualTo(null)
  }

  @Test fun hostWithPort() {
    val spec = HostSpecSelection.parse("example.com:332")
    assertThat(spec.hostname()).isEqualTo("example.com")
    assertThat(spec.port()).isEqualTo(332)
    assertThat(spec.username()).isEqualTo(null)
  }
}