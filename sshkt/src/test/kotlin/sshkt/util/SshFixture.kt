package sshkt.util

import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import org.apache.sshd.common.util.SecurityUtils
import org.apache.sshd.server.CommandFactory
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.password.PasswordAuthenticator
import org.apache.sshd.server.scp.ScpCommandFactory
import org.apache.sshd.server.shell.ProcessShellFactory
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory
import org.junit.rules.ExternalResource
import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean


/** Can be used as a rule to ensure the server is teared down after each test. */
class SshFixture(private val autoStart: Boolean = true) : ExternalResource() {
  val server = defaultSshServer()
  private val started = AtomicBoolean(false)

  @Throws(Throwable::class)
  override fun before() {
    if (autoStart) {
      start()
    }
  }

  override fun after() {
    stopServer()
  }

  @Throws(IOException::class)
  fun start() {
    if (!started.getAndSet(true)) {
      server.start()
    }
  }

  fun setupClient(config: Config): SSHClient {
    val client = SSHClient(config)
    client.addHostKeyVerifier(fingerprint)
    return client
  }

  private fun defaultSshServer(): SshServer {
    val sshServer = SshServer.setUpDefaultServer()
    sshServer.port = randomPort()
    val fileKeyPairProvider = SecurityUtils.createClassLoadableResourceKeyPairProvider()
    fileKeyPairProvider.resources = listOf(hostkey)
    sshServer.keyPairProvider = fileKeyPairProvider
    sshServer.passwordAuthenticator = PasswordAuthenticator { _, _, _ -> true }
    sshServer.gssAuthenticator = BogusGSSAuthenticator()
    sshServer.subsystemFactories = listOf(SftpSubsystemFactory())
    val commandFactory = ScpCommandFactory()
    commandFactory.delegateCommandFactory = CommandFactory { command ->
      ProcessShellFactory(command.split(" ")).create()
    }
    sshServer.commandFactory = commandFactory
    return sshServer
  }

  private fun randomPort(): Int {
    try {
      var s: ServerSocket? = null
      try {
        s = ServerSocket(0)
        return s.localPort
      } finally {
        if (s != null)
          s.close()
      }
    } catch (e: IOException) {
      throw RuntimeException(e)
    }
  }

  fun stopServer() {
    if (started.getAndSet(false)) {
      try {
        server.stop(true)
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }

  companion object {
    val hostkey = "hostkey.pem"
    val fingerprint = "ce:a7:c1:cf:17:3f:96:49:6a:53:1a:05:0b:ba:90:db"
  }
}