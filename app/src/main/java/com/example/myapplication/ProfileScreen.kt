package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    var user = User().apply {
        description = "teehee"
        joinDate = "01/01/2003"
        profilePicture = "" // Assuming an empty string for now
        totalNumberOfCleanups = 0
        email = "" // Assuming an empty string for now
        score = 0
        username = "User"
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText("My Profile", 16.dp)

        UserTile(user)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End // Align content to the right
        ) {
            Button(
                onClick = {}, // to be added
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Edit")
            }
        }

        TitleText("Friend Requests", 16.dp)
        friendRequest(user)
    }
}

@Composable
fun friendRequest(user: User) {
    // Info Date

    val imageURL = user.profilePicture
    val painter = rememberImagePainter(imageURL)

    val userName = user.username
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(80.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text("$userName wants to send you a Friend Request!",
            minLines = 2,
            maxLines = 2,
            modifier = Modifier.width(160.dp))


        Spacer(modifier = Modifier.weight(1f))

        Column (
            modifier = Modifier
                .padding(8.dp) // Padding around the buttons
                .align(Alignment.CenterVertically)
                .width(80.dp)
        )
        {
            FilledButton(
                {},
                msg ="Accept",
                modifierWrapper = Modifier.height(32.dp)

            )
            UnfilledButton(
                {},
                msg ="Decline",
                modifierWrapper = Modifier.height(32.dp)
            )
        }


    }
}

@Composable
@Preview
fun ProfileScreenView() {
    ProfileScreen()
}