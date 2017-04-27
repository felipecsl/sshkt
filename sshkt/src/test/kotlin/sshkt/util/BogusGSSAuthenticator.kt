package sshkt.util

class BogusGSSAuthenticator : org.apache.sshd.server.auth.gss.GSSAuthenticator() {
  private val manager = BogusGSSManager()

  override fun getGSSManager(): org.ietf.jgss.GSSManager {
    return manager
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getGSSCredential(mgr: org.ietf.jgss.GSSManager): org.ietf.jgss.GSSCredential {
    return manager.createCredential(org.ietf.jgss.GSSCredential.ACCEPT_ONLY)
  }
}

