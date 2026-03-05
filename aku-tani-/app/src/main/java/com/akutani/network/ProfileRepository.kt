package com.akutani.network

import android.content.Context
import android.net.Uri
import com.akutani.network.Api.ProfileApiClient
import com.akutani.utils.uriToMultipart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepository(
    private val context: Context
) {

    suspend fun updateProfile(
        name: String,
        email: String,
        avatarUri: Uri?
    ) {
        val api = ProfileApiClient.create(context)

        val nameRb = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRb = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val avatarPart = avatarUri?.let {
            uriToMultipart(context, it, "avatar")
        }

        // ⬇️ LANGSUNG ProfileResponse (BUKAN Response<>)
        val response = api.updateProfile(
            name = nameRb,
            email = emailRb,
            avatar = avatarPart
        )

        // ⬇️ simpan hasil
        val user = response.data
        val prefs = context.getSharedPreferences("aku_tani_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("username", user.name)
            .putString("email", user.email)
            .putString("avatar_url", user.avatarUrl)
            .apply()
    }
}
