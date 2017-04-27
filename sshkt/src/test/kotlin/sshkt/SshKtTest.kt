package sshkt

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.Session
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SshKtTest {
  @Test fun testOn() {
    val sshClient = mock(SSHClient::class.java)
    val session = mock(Session::class.java)
    given(sshClient.startSession()).willReturn(session)
    val sshKt = SshKt(sshClient)
    sshKt.on(listOf("google.com", "yahoo.com")) {
      it.execute("whoami ${it.hostname}")
    }
    verify(session).exec("whoami google.com")
    verify(session).exec("whoami yahoo.com")
  }
}