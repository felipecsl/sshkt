package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory
import net.schmizz.sshj.connection.channel.direct.Session
import java.io.Closeable
import java.util.concurrent.TimeUnit

internal class SshJRunner(
    sshKtConfig: SshKtConfig,
    parser: HostParser,
    loggerFactory: LoggerFactory,
    sshJConfig: Config,
    sshClientFactory: (Config) -> SSHClient) : Transport, Closeable {
  private val logger = loggerFactory.getLogger(javaClass)
  private val sshClient = sshClientFactory.invoke(sshJConfig)
  private val session: Session

  init {
    sshClient.connect(parser.hostname(), parser.port())
    if (sshKtConfig.keysOnly) {
      sshClient.authPublickey(parser.username())
    } else {
      sshClient.authPassword(parser.username(), sshKtConfig.password)
    }
    session = sshClient.startSession()
  }

  override fun execute(vararg args: String): Int {
    return newCommand(*args).let { command ->
      command.inputStream.bufferedReader().use {
        val output = it.readText()
        logger.info("[exec]: $output")
        command.join(5, TimeUnit.SECONDS)
        command.exitStatus
      }
    }
  }

  override fun capture(vararg args: String): String {
    return newCommand(*args).let { command ->
      command.inputStream.bufferedReader().use {
        val output = it.readText()
        command.join(5, TimeUnit.SECONDS)
        output.trim()
      }
    }
  }

  override fun test(vararg args: String): Boolean {
    return true
  }

  private fun newCommand(vararg args: String): Session.Command =
      session.exec(args.joinToString())

  override fun close() {
    session.close()
    sshClient.close()
  }
}