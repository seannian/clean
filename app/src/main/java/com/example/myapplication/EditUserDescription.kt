package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.ForestGreen
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditUserDescription(navController: NavController, user: User) {

    var description by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Introduce yourself to everyone!",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 48.sp,
                modifier = Modifier.width(350.dp),
                color = Color.Black
            )
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Drawer",
                    tint = BratGreen,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        HorizontalDivider(
            color = ForestGreen,
            thickness = 5.dp,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .width(100.dp)
        )

        CreateEventLabel("Description")
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            minLines = 7,
            placeholder = { Text("Give a brief description about yourself") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BratGreen,
                unfocusedBorderColor = ForestGreen
            )
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        FilledButton(
            onClick = {
                db.collection("Users").whereEqualTo("username", user.username)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userId = querySnapshot.documents[0].id
                            db.collection("Users").document(userId)
                                .update("description", description)
                                .addOnSuccessListener {
                                    Log.d("edit_description", "Attribute updated successfully!")
                                }
                        }
                        navController.popBackStack()
                    }.addOnFailureListener {
                        Log.d("edit_description", "Description could not be saved")
                    }
            },
            msg = "Save",
            modifierWrapper = Modifier
                .width(96.dp)
                .align(Alignment.End)
        )
    }
}