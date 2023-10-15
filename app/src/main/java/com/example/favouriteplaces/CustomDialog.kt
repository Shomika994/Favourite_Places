package com.example.favouriteplaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun MyDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {

    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Box(
                modifier = Modifier.background(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,

                    ) {
                    Text(text = "Material Dialog")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "This is a simple Material Dialog.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text("CANCEL")
                    }
                    Button(onClick = { onClick() },
                           modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "CONFIRM")
                    }
                }

            }

        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )

    )



}