package sshkt

import com.google.common.truth.Truth.assertThat
import net.schmizz.sshj.DefaultConfig
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import sshkt.util.SshFixture
import java.io.File

class IntegrationTest {
  @Rule @JvmField var fixture = SshFixture()
  @Rule @JvmField var temp = TemporaryFolder()

  @Test fun integrationTest() {
    val sshClient = fixture.setupClient(DefaultConfig())
    val server = fixture.server
    val file = File(temp.root, "testdir")
    val sshKt = SshKt(sshClient)
    sshKt.on(listOf("kurt@127.0.0.1:${server.port}")) {
      it.execute("mkdir ${file.path} &")
    }
    assertThat(file.exists()).isTrue()
    assertThat(file.isDirectory).isTrue()
  }
}