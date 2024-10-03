package com.example.MyStick.data.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WalletViewModel : ViewModel() {
    private val _walletAmount = MutableStateFlow(0.0)
    val walletAmount: StateFlow<Double> = _walletAmount

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = firestore.collection("users")

    init {
        auth.addAuthStateListener { authState ->
            val currentUser = authState.currentUser
            if (currentUser != null) {
                loadWalletAmount()
            } else {
                _walletAmount.value = 0.0 // Reset wallet amount on logout
            }
        }
    }

    fun loadWalletAmount() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            viewModelScope.launch {
                try {
                    val document = usersCollection.document(userId).get().await()
                    if (document.exists()) {
                        _walletAmount.value = document.getDouble("walletAmount") ?: 0.0
                    } else {
                        // Initialize wallet amount to 5 for new users
                        _walletAmount.value = 5.0
                        usersCollection.document(userId).set(mapOf("walletAmount" to 5.0)).await()
                    }
                } catch (e: Exception) {
                    // Handle any exceptions here
                }
            }
        }
    }

    fun addToWallet(amount: Double) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            viewModelScope.launch {
                val newAmount = _walletAmount.value + amount
                _walletAmount.value = newAmount
                usersCollection.document(userId).update("walletAmount", newAmount).await()
            }
        }
    }

    fun deductFromWallet(amount: Double) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            viewModelScope.launch {
                val newAmount = _walletAmount.value - amount
                _walletAmount.value = newAmount
                usersCollection.document(userId).update("walletAmount", newAmount).await()
            }
        }
    }

    fun updateWalletAmount(amount: Double) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            viewModelScope.launch {
                _walletAmount.value = amount
                usersCollection.document(userId).update("walletAmount", amount).await()
            }
        }
    }
}
