package com.example.a216696_wan_project2

// ================================================================
// FILE: EncryptionManager.kt
// Handles AES-256 encryption using Android Jetpack Security.
// Encrypts sensitive user data (name, translation history) before
// storing locally. Uses EncryptedSharedPreferences under the hood.
// ================================================================

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionManager {

    private const val KEY_ALIAS      = "linguaquest_master_key"
    private const val PREFS_NAME     = "linguaquest_encrypted_prefs"
    private const val ALGORITHM      = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    // ── Build EncryptedSharedPreferences ────────────────────────
    // Uses Android Keystore to protect the master key on-device.
    fun getEncryptedPrefs(context: Context): android.content.SharedPreferences {
        val masterKey = MasterKey.Builder(context, KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // ── AES encrypt a plain string ───────────────────────────────
    // Returns Base64-encoded "IV:CipherText" string.
    fun encrypt(plainText: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv         = cipher.iv
        val encrypted  = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val ivB64      = Base64.encodeToString(iv,        Base64.NO_WRAP)
        val cipherB64  = Base64.encodeToString(encrypted, Base64.NO_WRAP)
        return "$ivB64:$cipherB64"
    }

    // ── AES decrypt a Base64 "IV:CipherText" string ─────────────
    fun decrypt(encryptedText: String, secretKey: SecretKey): String {
        return try {
            val parts      = encryptedText.split(":")
            val iv         = Base64.decode(parts[0], Base64.NO_WRAP)
            val cipherText = Base64.decode(parts[1], Base64.NO_WRAP)
            val cipher     = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            String(cipher.doFinal(cipherText), Charsets.UTF_8)
        } catch (e: Exception) {
            encryptedText // return as-is if decryption fails (e.g. not encrypted)
        }
    }

    // ── Generate a random AES-256 key (for session use) ─────────
    fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(256)
        return keyGen.generateKey()
    }

    // ── Rebuild key from stored bytes ───────────────────────────
    fun keyFromBytes(bytes: ByteArray): SecretKey =
        SecretKeySpec(bytes, ALGORITHM)
}