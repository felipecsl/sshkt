package sshkt.util

class BogusGSSName(private val name: String, private val oid: org.ietf.jgss.Oid) : org.ietf.jgss.GSSName {
  @Throws(org.ietf.jgss.GSSException::class)
  override fun equals(another: org.ietf.jgss.GSSName): Boolean {
    if (another !is sshkt.util.BogusGSSName) {
      throw org.ietf.jgss.GSSException(org.ietf.jgss.GSSException.BAD_NAMETYPE)
    }
    val otherName = another
    return name == otherName.name && oid == otherName.oid
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun canonicalize(mech: org.ietf.jgss.Oid): org.ietf.jgss.GSSName {
    return this
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun export(): ByteArray {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getStringNameType(): org.ietf.jgss.Oid {
    return oid
  }

  override fun isAnonymous(): Boolean {
    return false
  }

  override fun isMN(): Boolean {
    return false
  }

  override fun toString(): String {
    return name
  }
}