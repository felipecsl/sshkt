package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.DefaultConfig
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory

class SshKt(
    private val sshKtConfig: SshKtConfig = SshKtConfig.DEFAULT,
    private val config: Config = DefaultConfig(),
    private val sshClientFactory: (Config) -> SSHClient = { c -> SSHClient(c) },
    private val loggerFactory: LoggerFactory = LoggerFactory.DEFAULT,
    block: SshKt.() -> Unit) {
  init {
    block()
  }

  fun on(vararg servers: String, block: Host.() -> Unit) {
    servers.forEach {
      val hostSpec = HostSpecSelection.parse(it)
      Host(emptyList(), null, sshKtConfig, hostSpec, loggerFactory, config, sshClientFactory)
          .block()
    }
  }
}