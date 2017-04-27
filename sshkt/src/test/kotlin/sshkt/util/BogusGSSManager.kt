package sshkt.util

import org.apache.sshd.server.auth.gss.UserAuthGSS
import org.ietf.jgss.*
import org.slf4j.LoggerFactory
import java.security.Provider

/**
 * Implements a fake Kerberos 5 mechanism. MINA only supports Kerberos 5 over
 * GSS-API, so we can't implement a separate mechanism.
 */
class BogusGSSManager : GSSManager() {
  override fun getMechs(): Array<Oid> {
    return arrayOf(KRB5_MECH)
  }

  @Throws(GSSException::class)
  override fun getNamesForMech(mech: Oid): Array<Oid> {
    return arrayOf(GSSName.NT_EXPORT_NAME, GSSName.NT_HOSTBASED_SERVICE)
  }

  override fun getMechsForName(nameType: Oid): Array<Oid> {
    return arrayOf(KRB5_MECH)
  }

  @Throws(GSSException::class)
  override fun createName(nameStr: String, nameType: Oid): GSSName {
    return BogusGSSName(nameStr, nameType)
  }

  @Throws(GSSException::class)
  override fun createName(name: ByteArray, nameType: Oid): GSSName {
    throw unavailable()
  }

  @Throws(GSSException::class)
  override fun createName(nameStr: String, nameType: Oid, mech: Oid): GSSName {
    return this.createName(nameStr, nameType)
  }

  @Throws(GSSException::class)
  override fun createName(name: ByteArray, nameType: Oid, mech: Oid): GSSName {
    throw unavailable()
  }

  @Throws(GSSException::class)
  override fun createCredential(usage: Int): GSSCredential {
    return BogusGSSCredential(null, usage)
  }

  @Throws(GSSException::class)
  override fun createCredential(name: GSSName, lifetime: Int, mech: Oid, usage: Int): GSSCredential {
    return BogusGSSCredential(name, usage)
  }

  @Throws(GSSException::class)
  override fun createCredential(name: GSSName, lifetime: Int, mechs: Array<Oid>, usage: Int): GSSCredential {
    return BogusGSSCredential(name, usage)
  }

  @Throws(GSSException::class)
  override fun createContext(peer: GSSName, mech: Oid, myCred: GSSCredential, lifetime: Int): GSSContext {
    return BogusGSSContext()
  }

  @Throws(GSSException::class)
  override fun createContext(myCred: GSSCredential): GSSContext {
    return BogusGSSContext()
  }

  @Throws(GSSException::class)
  override fun createContext(interProcessToken: ByteArray): GSSContext {
    throw unavailable()
  }

  @Throws(GSSException::class)
  override fun addProviderAtFront(p: Provider, mech: Oid) {
    throw unavailable()
  }

  @Throws(GSSException::class)
  override fun addProviderAtEnd(p: Provider, mech: Oid) {
    throw unavailable()
  }

  companion object {
    val KRB5_MECH = UserAuthGSS.KRB5_MECH!!

    private val log = LoggerFactory.getLogger(BogusGSSManager::class.java)

    @Throws(GSSException::class)
    internal fun unavailable(): GSSException {
      val e = GSSException(GSSException.UNAVAILABLE)
      log.error(e.message, e)
      throw e
    }
  }
}

