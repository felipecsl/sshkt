package sshkt

class SshKtConfig(
    val keysOnly: Boolean = true,
    val password: String? = null) {

  companion object {
    val DEFAULT = SshKtConfig()
  }
}