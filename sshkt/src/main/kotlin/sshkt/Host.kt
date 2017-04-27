package sshkt

import net.schmizz.sshj.common.LoggerFactory
import net.schmizz.sshj.connection.channel.direct.Session
import java.util.concurrent.TimeUnit

class Host(
    val hostname: String,
    loggerFactory: LoggerFactory,
    private val session: Session) {
  private val logger = loggerFactory.getLogger(javaClass)

  fun capture(vararg args: String) {

  }

  fun test(vararg args: String) {

  }

  fun execute(vararg args: String): Int {
    return session.exec(args.joinToString()).let { command ->
      command.inputStream.bufferedReader().use {
        val output = it.readText()
        logger.info("[exec]: $output")
        command.join(5, TimeUnit.SECONDS)
        command.exitStatus
      }
    }
  }
}