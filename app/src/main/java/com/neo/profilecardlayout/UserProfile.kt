package com.neo.profilecardlayout

data class UserProfile constructor(val name: String, val status: Boolean, val drawableId: Int)


val userProfileList = arrayListOf<UserProfile>(
    UserProfile("Top Boy", true, R.drawable.profile_pic),
    UserProfile("Jamie", false, R.drawable.profile_pic) // use the same profile pic
)