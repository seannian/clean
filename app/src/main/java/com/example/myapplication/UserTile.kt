package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.Grey

@Composable
fun UserTile(user: User) {

    // Info Date

    val imageURL = user.profilePicture
    val painter = rememberAsyncImagePainter(
        model = imageURL,
        error = painterResource(R.drawable.ic_launcher_background), // Fallback image on error
        placeholder = painterResource(R.drawable.ic_launcher_background) // Placeholder during loading
    )

    val userName = "Name"
    val dateMade = "01/01/2003"
    val cleanups = 0
    val points = 0
    val description = "teehee"

    Column(modifier = Modifier.padding(all = 8.dp)) {
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
        Row {
            Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date Icon", tint = Grey)
            Spacer(modifier = Modifier.width(8.dp))
            Text(dateMade)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // events attended
        Row {
            Icon(
                painter = painterResource(R.drawable.clock_icon),
                contentDescription = "Clock Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Complete $cleanups cleanups")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // points
        Row{
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Clock Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Earned $points points")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text("About Me")

        Text(description)



    }
}