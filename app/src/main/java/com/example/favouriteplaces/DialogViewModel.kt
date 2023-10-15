package com.example.favouriteplaces

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DialogViewModel: ViewModel() {

    var isDialogShown by mutableStateOf(false)
        private set

    fun onClick(){
        isDialogShown = true
    }

    fun onDismiss(){
        isDialogShown = false
    }
}