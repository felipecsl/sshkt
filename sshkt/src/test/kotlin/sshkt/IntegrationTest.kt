package sshkt

import com.google.common.truth.Truth.assertThat
import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import org.junit.Test

class IntegrationTest {
  private val host = "vagrant@33.33.33.2"
  private val clientFactory: (Config) -> SSHClient = { c ->
    SSHClient(c).also {
      it.loadKnownHosts()
    }
  }

  @Test fun passwordAuth() {
    SshKt(SshKtConfig(false, "abacabb"), sshClientFactory = clientFactory) {
      on("supercow@33.33.33.2") {
        assertThat(capture("whoami")).isEqualTo("supercow")
      }
    }
  }

  @Test fun publicKeyAuth() {
    SshKt(sshClientFactory = clientFactory) {
      on(host) {
        assertThat(capture("whoami")).isEqualTo("vagrant")
      }
    }
  }

  @Test fun testOn() {
    val time = System.currentTimeMillis().toString()
    val dir = "/tmp/foo"
    SshKt(sshClientFactory = clientFactory) {
      on(host) {
        execute("mkdir $dir")
        assertThat(capture("ls -l $dir")).isEqualTo("total 0")
        execute("echo '$time' > $dir/bar.txt")
        assertThat(capture("ls $dir")).isEqualTo("bar.txt")
        assertThat(capture("cat $dir/bar.txt")).isEqualTo(time)
        execute("rm -rf $dir")
      }
    }
  }

  @Test fun testWithin() {
    val time = System.currentTimeMillis().toString()
    val dir = "/tmp/foo"
    SshKt(sshClientFactory = clientFactory) {
      on(host) {
        execute("mkdir /tmp/foo")
        within(dir) {
          assertThat(capture("ls -l")).isEqualTo("total 0")
          execute("echo '$time' > bar.txt")
          assertThat(capture("ls")).isEqualTo("bar.txt")
          assertThat(capture("cat bar.txt")).isEqualTo(time)
        }
        execute("rm -rf $dir")
      }
    }
  }
}