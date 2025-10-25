package com.balikllama.xpguiderdemo.ui.screen.profile

// Bu veri sınıflarını da UIState dosyasına veya kendi model dosyalarına taşımak daha iyidir.
data class UserInfo(val label: String, val value: String)
data class ActivityItem(val id: String, val description: String)

data class ProfileUIState(
    val credit: Int = 0,
    val isLoading: Boolean = true,
    val userDetails: List<UserInfo> = emptyList(),
    val pastActivities: List<ActivityItem> = emptyList()
)
