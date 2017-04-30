package sshkt

import net.schmizz.sshj.Config
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory

class Host internal constructor(
    private val pwd: List<String> = linkedListOf(),
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
      block(newHost(path))

  private fun newHost(newDir: String) =
      Host(linkedListOf(pwd.plus(newDir)),
          sshKtConfig,
          hostSpec,
          loggerFactory,
          config,
          sshClientFactory)

  private fun newCommand() =
      SshJRunner(
          inPath = pwd.joinToString("/"),
          sshKtConfig = sshKtConfig,
          hostSpec = hostSpec,
          loggerFactory = loggerFactory,
          sshJConfig = config,
          sshJClientFactory = sshClientFactory)
}

