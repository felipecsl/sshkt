package sshkt

import net.schmizz.sshj.SSHClient

internal class SimpleHostParser(hostString: String) : HostParser(hostString) {
  override fun suitable() =
      !hostString.contains(Regex("[:@]"))

  override fun username() = null

  override fun port() = SSHClient.DEFAULT_PORT

  override fun hostname() = hostString
}