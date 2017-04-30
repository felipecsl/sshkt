package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory
import net.schmizz.sshj.connection.channel.direct.Session
import java.io.Closeable
import java.util.concurrent.TimeUnit

internal class SshJRunner(
    private val inPath: String,
    sshKtConfig: SshKtConfig,
    hostSpec: HostSpec,
    loggerFactory: LoggerFactory,
    sshJConfig: Config,
    sshJClientFactory: (Config) -> SSHClient) : Transport, Closeable {
  private val logger = loggerFactory.getLogger(javaClass)
  private val sshClient = sshJClientFactory.invoke(sshJConfig)
  private val session: Session

  init {
    sshClient.connect(hostSpec.hostname(), hostSpec.port())
    if (sshKtConfig.keysOnly) {
      sshClient.authPublickey(hostSpec.username())
    } else {
      sshClient.authPassword(hostSpec.username(), sshKtConfig.password)
    }
    session = sshClient.startSession()
  }

  override fun execute(vararg args: String): Int {
    return newCommand(*args).let { command ->
      command.inputStream.bufferedReader().use {
        val output = it.readText()
        logger.info("[output]: $output")
        command.join(5, TimeUnit.SECONDS)
        command.exitStatus
      }
    }
  }

  override fun capture(vararg args: String): String {
    return newCommand(*args).let { command ->
      command.inputStream.bufferedReader().use {
        val output = it.readText()
        logger.info("[output]: $output")
        command.join(5, TimeUnit.SECONDS)
        output.trim()
      }
    }
  }

  override fun test(vararg args: String): Boolean {
    return true
  }

  private fun newCommand(vararg args: String): Session.Command {
    val cmd = if (inPath.any()) {
      "cd $inPath && ${args.joinToString()}"
    } else {
      args.joinToString()
    }
    logger.info("[exec]: $cmd")
    return session.exec(cmd)
  }

  override fun close() {
    session.close()
    sshClient.close()
  }
}