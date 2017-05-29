package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory

class Host internal constructor(
    private val pwd: List<String> = linkedListOf(),
    private val user: String? = null,
    private val sshKtConfig: SshKtConfig,
    private val hostSpec: HostSpec,
    private val loggerFactory: LoggerFactory,
    private val config: Config,
    private val sshClientFactory: (Config) -> SSHClient) : Transport {

  override fun test(vararg args: String) =
      newCommand().use {
        it.test(*args)
      }

  override fun execute(vararg args: String) =
      newCommand().use {
        it.execute(*args)
      }


  override fun capture(vararg args: String) =
      newCommand().use {
        it.capture(*args)
      }

  fun within(path: String, block: Host.() -> Unit) =
      block(newHost(pwd = linkedListOf(pwd.plus(path))))

  fun asUser(user: String, block: Host.() -> Unit) =
      block(newHost(user = user))

  private fun newHost(pwd: List<String> = this.pwd, user: String? = this.user) =
      Host(pwd, user, sshKtConfig, hostSpec, loggerFactory, config, sshClientFactory)

  private fun newCommand() =
      SshJRunner(
          inPath = pwd.joinToString("/"),
          asUser = user,
          sshKtConfig = sshKtConfig,
          hostSpec = hostSpec,
          loggerFactory = loggerFactory,
          sshJConfig = config,
          sshJClientFactory = sshClientFactory)
}

