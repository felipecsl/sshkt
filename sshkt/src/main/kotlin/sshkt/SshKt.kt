package sshkt

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory

class SshKt(
    private val sshClient: SSHClient = SSHClient(),
    private val loggerFactory: LoggerFactory = LoggerFactory.DEFAULT) {
  fun on(servers: List<String>, block: (Host) -> Unit) {
    servers.forEach { host ->
      val parser = findSuitableParser(host)
      sshClient.connect(parser.hostname(), parser.port())
      // TODO: Figure out authentication
//      sshClient.authPublickey("root")
      sshClient.authPassword(parser.username(), parser.username())
      sshClient.use {
        it.startSession().use { session ->
          block.invoke(Host(host, loggerFactory, session))
        }
      }
    }
  }

  private fun findSuitableParser(hostString: String): HostParser {
    return HOST_PARSERS
        .map { it.constructors[0] }
        .map { it.newInstance(hostString) as HostParser }
        .firstOrNull { it.suitable() }
        ?: throw UnparseableHostStringException("Cannot parse host string $hostString")
  }

  companion object {
    val HOST_PARSERS: List<Class<out HostParser>> = listOf(
        SimpleHostParser::class.java,
        HostWithUsernameAndPortParser::class.java)
  }
}