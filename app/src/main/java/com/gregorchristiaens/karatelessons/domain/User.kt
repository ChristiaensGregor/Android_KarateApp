package com.gregorchristiaens.karatelessons.domain

class User() {

    var id: String = ""
    var email: String = ""
    var userName: String = ""
    var karateClub: String = ""
    var grade: String = ""
    var progress: String = ""

    constructor(
        _id: String,
        _email: String,
        _username: String,
        _karateClub: String?,
        _grade: String?,
        _progress: String?
    ) : this() {
        id = _id
        email = _email
        userName = _username
        if (_karateClub != null) {
            karateClub = _karateClub
        }
        if (_grade != null) {
            grade = _grade
        }
        if (_progress != null) {
            progress = _progress
        }
    }

    override fun toString(): String {
        return "Username: $userName , Email: $email , Club: $karateClub , Progress: $progress"
    }
}