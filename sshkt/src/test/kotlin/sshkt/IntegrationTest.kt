package sshkt

import com.google.common.truth.Truth.assertThat
import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import sshkt.util.SshFixture
import java.io.File

class IntegrationTest {
  @Rule @JvmField val fixture = SshFixture()
  @Rule @JvmField val temp = TemporaryFolder()

  @Test fun integrationTest() {
    val server = fixture.server
    val file = File(temp.root, "testdir")
    val host = listOf("kurt@127.0.0.1:${server.port}")
    val sshKtConfig = SshKtConfig(keysOnly = false, password = "kurt")
    SshKt(sshKtConfig, sshClientFactory = newClient()).on(host) {
      it.execute("mkdir ${file.path}")
    }
    assertThat(file.exists()).isTrue()
    assertThat(file.isDirectory).isTrue()
    File(file, "foo.txt").writeText("hello world")
    SshKt(sshKtConfig, sshClientFactory = newClient()).on(host) {
      assertThat(it.capture("ls ${file.path}")).isEqualTo("foo.txt")
      assertThat(it.capture("cat ${file.path}/foo.txt")).isEqualTo("hello world")
    }
  }

  @Test fun realServer() {
    val clientFactory: (Config) -> SSHClient = { c ->
      SSHClient(c).also {
        it.loadKnownHosts()
      }
    }
    val sshKt = SshKt(sshClientFactory = clientFactory)
    sshKt.on(listOf("root@107.170.28.142:22")) {
      assertThat(it.capture("whoami")).isEqualTo("root")
    }
  }

  private fun newClient() = fixture::setupClient
}