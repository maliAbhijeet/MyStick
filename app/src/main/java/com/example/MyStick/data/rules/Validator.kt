package com.example.loginflow.data.rules

object Validator {
    fun validateFirstName(fName: String): ValidationResult {
        return if (fName.isNotEmpty()) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "First name cannot be empty")
        }
    }

    fun validateLastName(lName: String): ValidationResult {
        return if (lName.isNotEmpty()) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Last name cannot be empty")
        }
    }

    fun validateEmail(email: String): ValidationResult {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Invalid email address")
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return if (password.length >= 6) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Password must be at least 6 characters long")
        }
    }
}

data class ValidationResult(val status: Boolean, val errorMessage: String? = null)
