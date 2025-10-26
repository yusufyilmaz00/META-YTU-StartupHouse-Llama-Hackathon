package com.balikllama.xpguiderdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balikllama.xpguiderdemo.data.local.entity.UserStatusEntity

@Dao
interface UserStatusDao {

    // Yeni bir kullanıcı durumu ekler veya mevcut olanı günceller
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserStatus(userStatus: UserStatusEntity)

    // Belirli bir email'e sahip kullanıcının durumunu getirir
    @Query("SELECT * FROM user_status WHERE email = :email LIMIT 1")
    suspend fun getUserStatus(email: String): UserStatusEntity?

    // data/local/dao/UserStatusDao.kt
    @Query("DELETE FROM user_status WHERE email = :email")
    suspend fun deleteUserStatus(email: String)
}
