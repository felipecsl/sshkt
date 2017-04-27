package sshkt.util

import org.ietf.jgss.*

class BogusGSSContext : org.ietf.jgss.GSSContext {

  private var initialized = false
  private var accepted = false
  private var integState = false
  private var mutualAuthState = false

  @Throws(org.ietf.jgss.GSSException::class)
  override fun initSecContext(inputBuf: ByteArray, offset: Int, len: Int): ByteArray {
    initialized = true
    return sshkt.util.BogusGSSContext.Companion.INIT_TOKEN
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun initSecContext(inStream: java.io.InputStream, outStream: java.io.OutputStream): Int {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun acceptSecContext(inToken: ByteArray, offset: Int, len: Int): ByteArray {
    accepted = java.util.Arrays.equals(sshkt.util.BogusGSSContext.Companion.INIT_TOKEN, java.util.Arrays.copyOfRange(inToken, offset, offset + len))
    return sshkt.util.BogusGSSContext.Companion.ACCEPT_TOKEN
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun acceptSecContext(inStream: java.io.InputStream, outStream: java.io.OutputStream) {
    throw BogusGSSManager.Companion.unavailable()
  }

  override fun isEstablished(): Boolean {
    return initialized || accepted
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun dispose() {
    // Nothing to do
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getWrapSizeLimit(qop: Int, confReq: Boolean, maxTokenSize: Int): Int {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun wrap(inBuf: ByteArray, offset: Int, len: Int, msgProp: org.ietf.jgss.MessageProp): ByteArray {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun wrap(inStream: java.io.InputStream, outStream: java.io.OutputStream, msgProp: org.ietf.jgss.MessageProp) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun unwrap(inBuf: ByteArray, offset: Int, len: Int, msgProp: org.ietf.jgss.MessageProp): ByteArray {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun unwrap(inStream: java.io.InputStream, outStream: java.io.OutputStream, msgProp: org.ietf.jgss.MessageProp) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getMIC(inMsg: ByteArray, offset: Int, len: Int, msgProp: org.ietf.jgss.MessageProp): ByteArray {
    return sshkt.util.BogusGSSContext.Companion.MIC
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getMIC(inStream: java.io.InputStream, outStream: java.io.OutputStream, msgProp: org.ietf.jgss.MessageProp) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun verifyMIC(inToken: ByteArray, tokOffset: Int, tokLen: Int, inMsg: ByteArray,
      msgOffset: Int, msgLen: Int, msgProp: org.ietf.jgss.MessageProp) {
    if (!java.util.Arrays.equals(sshkt.util.BogusGSSContext.Companion.MIC, java.util.Arrays.copyOfRange(inToken, tokOffset, tokOffset + tokLen))) {
      throw org.ietf.jgss.GSSException(org.ietf.jgss.GSSException.BAD_MIC)
    }
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun verifyMIC(tokStream: java.io.InputStream, msgStream: java.io.InputStream, msgProp: org.ietf.jgss.MessageProp) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun export(): ByteArray {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestMutualAuth(state: Boolean) {
    this.mutualAuthState = state
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestInteg(state: Boolean) {
    this.integState = state
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestReplayDet(state: Boolean) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestSequenceDet(state: Boolean) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestCredDeleg(state: Boolean) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestAnonymity(state: Boolean) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestConf(state: Boolean) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun requestLifetime(lifetime: Int) {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun setChannelBinding(cb: org.ietf.jgss.ChannelBinding) {
    throw BogusGSSManager.Companion.unavailable()
  }

  override fun getMutualAuthState(): Boolean {
    return mutualAuthState
  }

  override fun getIntegState(): Boolean {
    return integState
  }

  override fun getCredDelegState(): Boolean {
    return false
  }

  override fun getReplayDetState(): Boolean {
    return false
  }

  override fun getSequenceDetState(): Boolean {
    return false
  }

  override fun getAnonymityState(): Boolean {
    return false
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun isTransferable(): Boolean {
    return false
  }

  override fun isProtReady(): Boolean {
    return false
  }

  override fun getConfState(): Boolean {
    return false
  }

  override fun getLifetime(): Int {
    return org.ietf.jgss.GSSContext.INDEFINITE_LIFETIME
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getSrcName(): org.ietf.jgss.GSSName {
    try {
      val hostname = java.net.InetAddress.getLocalHost().canonicalHostName
      return BogusGSSName("user@" + hostname, GSSName.NT_HOSTBASED_SERVICE)
    } catch (e: java.net.UnknownHostException) {
      throw IllegalStateException(e)
    }
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getTargName(): org.ietf.jgss.GSSName {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getMech(): org.ietf.jgss.Oid {
    return BogusGSSManager.Companion.KRB5_MECH
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun getDelegCred(): org.ietf.jgss.GSSCredential {
    throw BogusGSSManager.Companion.unavailable()
  }

  @Throws(org.ietf.jgss.GSSException::class)
  override fun isInitiator(): Boolean {
    return false
  }

  companion object {
    private val INIT_TOKEN = sshkt.util.BogusGSSContext.Companion.fromString("INIT")
    private val ACCEPT_TOKEN = sshkt.util.BogusGSSContext.Companion.fromString("ACCEPT")
    private val MIC = sshkt.util.BogusGSSContext.Companion.fromString("LGTM")

    private fun fromString(s: String): ByteArray {
      return s.toByteArray(net.schmizz.sshj.common.IOUtils.UTF8)
    }
  }
}