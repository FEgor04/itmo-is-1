package com.jellyone.lab1.service

import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Component
class SHACoder {

    fun getSHA512SecurePassword(passwordToHash: String, salt: String): String {
        try {
            val md = MessageDigest.getInstance("SHA-512")
            md.update(salt.toByteArray(StandardCharsets.UTF_8))
            val bytes = md.digest(passwordToHash.toByteArray(StandardCharsets.UTF_8))
            val sb = StringBuilder()
            for (b in bytes) {
                sb.append(Integer.toString((b.toInt() and 0xff) + 0x100, 16).substring(1))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return "";
    }
}