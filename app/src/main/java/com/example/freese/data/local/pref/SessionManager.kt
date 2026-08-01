package com.example.freese.data.local.pref

import android.content.Context
import android.content.SharedPreferences
class SessionManager(context: Context) {

   private val prefs: SharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

   companion object {
      const val AUTH_TOKEN = "auth_token"
      const val USER_ROLE = "user_role"
   }

   fun saveAuthToken(token: String) {
      val editor = prefs.edit()
      editor.putString(AUTH_TOKEN, token)
      editor.apply()
   }

   fun fetchAuthToken(): String? {
      return prefs.getString(AUTH_TOKEN, null)
   }

   fun clearAuthToken() {
      val editor = prefs.edit()
      editor.remove(AUTH_TOKEN)
      editor.apply()
   }

   fun saveUserRole(role: String) {
      prefs.edit().putString(USER_ROLE, role).apply()
   }

   fun fetchUserRole(): String? {
      return prefs.getString(USER_ROLE, "buyer") // Defaultnya "user"
   }

   fun clearSession() { // Ganti nama clearAuthToken jadi clearSession agar sekalian
      prefs.edit().remove(AUTH_TOKEN).remove(USER_ROLE).apply()
   }
}