package it.srv.ThermoSmartSpring.dto

import it.srv.ThermoSmartSpring.model.User

class UserDTO(
    var username: String = "",
    var password: String = "",
    var matchingPassword: String = "",
    var email: String = "",
    var oldPassword: String = ""
) {
    fun userDTOtoUser(): User {
        val user = User()
        user.email = this.email
        user.username = this.username
        user.password = this.password
        return user
    }
}
