package sshkt

import net.schmizz.sshj.SSHClient

internal class HostWithUsernameSpec(hostString: String) : HostSpec(hostString) {
  override fun username() =
      hostString.split("@")[0]

  override fun port() =
      SSHClient.DEFAULT_PORT

  override fun hostname() =
      hostString.split("@")[1]

  override fun suitable() =
      hostString.contains("@") && !hostString.contains(":")
}