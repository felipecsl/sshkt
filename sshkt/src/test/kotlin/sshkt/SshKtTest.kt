package sshkt

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory
import net.schmizz.sshj.connection.channel.direct.Session
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.slf4j.Logger

class SshKtTest {
  @Test fun testOn() {
    val sshClient = mock(SSHClient::class.java)
    val loggerFactory = mock(LoggerFactory::class.java)
    val session = mock(Session::class.java)
    val googleCommand = mock(Session.Command::class.java)
    val yahooCommand = mock(Session.Command::class.java)
    val logger = mock(Logger::class.java)

    given(sshClient.startSession()).willReturn(session)
    given(session.exec("whoami google.com")).willReturn(googleCommand)
    given(session.exec("whoami yahoo.com")).willReturn(yahooCommand)
    given(loggerFactory.getLogger(any<Class<*>>())).willReturn(logger)
    given(googleCommand.inputStream).willReturn("foobar".byteInputStream())
    given(yahooCommand.inputStream).willReturn("barbaz".byteInputStream())

    val sshKt = SshKt(sshClient, loggerFactory)
    sshKt.on(listOf("google.com", "yahoo.com")) {
      it.execute("whoami ${it.hostname}")
    }

    verify(session).exec("whoami google.com")
    verify(session).exec("whoami yahoo.com")
    verify(logger).info("[exec]: foobar")
    verify(logger).info("[exec]: barbaz")
  }
}