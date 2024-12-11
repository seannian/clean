package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.Grey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserTile(user: User, loggedInUser: User, navController: NavController) {

    // Info Date
    // need to retrieve current logged in user
    // check if logged in user is same as this user, Edit button
    // if logged in user is friends with this user, Unfriend button
    // if logged in user is not friends with this user, Friend button
    var buttonMsg = "Friend"
    if (loggedInUser.username == user.username) {
        buttonMsg = "Edit"
    }
    // need to add friend list to user class
    val imageURL = user.profilePicture
    val painter = rememberImagePainter(imageURL)

    val userName = user.username
    val dateMade = user.joinDate
    val cleanups = user.totalNumberOfCleanups
    val points = user.score
    val description = user.description

    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            //modifier = Modifier.fillMaxWidth()
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

            Text(userName)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // join date
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "Date Icon",
                tint = Grey
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(dateMade)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // events attended
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.clock_icon),
                contentDescription = "Clock Icon",
                tint = Grey
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Complete $cleanups cleanups")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // points
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Clock Icon",
                tint = Grey
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text("Earned $points points")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "About Me",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .width(20.dp)
                .padding(bottom = 10.dp)
        )
        SubText(
            if (description == "") "This user has not set a description yet." else description, Grey
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FilledButton(onClick = {
                if (buttonMsg == "Edit") {
                    navController.navigate("edit_description")
                } else if (buttonMsg == "Friend") {
                    sendFriendRequest()
                } else {
                    unFriend()
                }
            }, msg = buttonMsg, modifierWrapper = Modifier.width(100.dp))
        }
    }
}

fun sendFriendRequest() {

}

fun unFriend() {

}