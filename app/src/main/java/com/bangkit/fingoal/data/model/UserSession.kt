package com.bangkit.fingoal.data.model

import com.bangkit.fingoal.data.remote.response.Token
import com.bangkit.fingoal.data.remote.response.UserId

data class UserSession (
    val userLoginToken: Token,
    val usernameId: UserId
)