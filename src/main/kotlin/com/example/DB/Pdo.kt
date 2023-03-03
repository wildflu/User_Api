package com.example.DB

import org.ktorm.database.Database

object Pdo {
    var connector = Database.connect (
        url = "jdbc:mysql://localhost:3306/controllapi",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = ""
    )
}