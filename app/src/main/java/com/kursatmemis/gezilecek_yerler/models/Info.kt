package com.kursatmemis.gezilecek_yerler.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Info(
    val title: String? = null,
    val city: String? = null,
    val note: String? = null
)
