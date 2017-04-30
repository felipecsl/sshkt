package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.DefaultConfig
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory

class SshKt(
    private val sshKtConfig: SshKtConfig = SshKtConfig.DEFAULT,
    private val config: Config = DefaultConfig(),
    private val sshClientFactory: (Config) -> SSHClient = { c -> SSHClient(c) },
    private val loggerFactory: LoggerFactory = LoggerFactory.DEFAULT) {
  fun on(servers: List<String>, block: (Host) -> Unit) {
    servers.forEach {
      val parser = findSuitableParser(it)
      val host = Host(sshKtConfig, parser, loggerFactory, config, sshClientFactory)
      block.invoke(host)
    }
  }

  private fun findSuitableParser(hostString: String): HostSpec {
    return HOST_PARSERS
        .map { it.constructors[0] }
        .map { it.newInstance(hostString) as HostSpec }
        .firstOrNull { it.suitable() }
        ?: throw UnparseableHostStringException("Cannot parse host string $hostString")
  }

  companion object {
    internal val HOST_PARSERS: List<Class<out HostSpec>> = listOf(
        SimpleHostSpec::class.java,
        HostWithUsernameAndPortSpec::class.java)
  }
}