package sshkt.util

class BogusGSSCredential(private val name: org.ietf.jgss.GSSName?, private val usage: Int) : org.ietf.jgss.GSSCredential {
  @Throws(org.ietf.jgss.GSSException::class)
  override fun dispose() {
    // Nothing to do
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getName(): org.ietf.jgss.GSSName? {
    return name
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getName(mech: org.ietf.jgss.Oid): org.ietf.jgss.GSSName {
    return name!!.canonicalize(mech)
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getRemainingLifetime(): Int {
    return org.ietf.jgss.GSSCredential.INDEFINITE_LIFETIME
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getRemainingInitLifetime(mech: org.ietf.jgss.Oid): Int {
    return org.ietf.jgss.GSSCredential.INDEFINITE_LIFETIME
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getRemainingAcceptLifetime(mech: org.ietf.jgss.Oid): Int {
    return org.ietf.jgss.GSSCredential.INDEFINITE_LIFETIME
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getUsage(): Int {
    return usage
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getUsage(mech: org.ietf.jgss.Oid): Int {
    return usage
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getMechs(): Array<org.ietf.jgss.Oid> {
    return arrayOf(BogusGSSManager.Companion.KRB5_MECH)
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun add(name: org.ietf.jgss.GSSName, initLifetime: Int, acceptLifetime: Int, mech: org.ietf.jgss.Oid, usage: Int) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(CloneNotSupportedException::class)
  override fun clone(): Any {
    return super.clone()
  }

  override fun hashCode(): Int {
    return name?.hashCode() ?: 0
  }

  override fun equals(obj: Any?): Boolean {
    if (obj !is sshkt.util.BogusGSSCredential) {
      return false
    }
    val otherName = obj.name
    return if (name == null) otherName == null else name == otherName as Any?
  }
}