package edu.skku.cs.pa3

data class User(val username: String? = null,
                val age: Int? = null,
                val gender: String? = null,
                val height: Double? = null,
                val weight: Double? = null) {}

data class isFirstLogin(val isfirst: Boolean? = false) {}