package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory

class Host internal constructor(
    private val sshKtConfig: SshKtConfig,
    private val spec: HostSpec,
    private val loggerFactory: LoggerFactory,
    private val config: Config,
    private val sshClientFactory: (Config) -> SSHClient) : Transport {

  override fun test(vararg args: String): Boolean {
    return newSshJRunner().use {
      it.test(*args)
    }
  }

  override fun execute(vararg args: String): Int {
    return newSshJRunner().use {
      it.execute(*args)
    }
  }

  override fun capture(vararg args: String): String {
    return newSshJRunner().use {
      it.capture(*args)
    }
  }

  private fun newSshJRunner() =
      SshJRunner(sshKtConfig, spec, loggerFactory, config, sshClientFactory)
}

