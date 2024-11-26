package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.Grey
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventComponent(event: Event) {
    val placeholderPainter = painterResource(id = R.drawable.cs_160_project_logo)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp),
    ) {
        Column(modifier = Modifier.height(100.dp)) {
            Box(
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .background(Color.Gray)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                if (event.eventPicUri.isNotEmpty()) {
                    AsyncImage(
                        model = event.eventPicUri,
                        contentDescription = "Event picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = placeholderPainter,
                        error = placeholderPainter
                    )
                } else {
                    Image(
                        painter = placeholderPainter,
                        contentDescription = "Placeholder Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            EventTitleText(event.title)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "User Icon",
                    tint = Grey
                )
                Spacer(modifier = Modifier.width(10.dp))
                SubText(event.author, Grey)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date Icon", tint = Grey)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(event.date?.toFormattedString("MM-dd-yyyy") ?: "Date not set", Grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClockIcon()
        Spacer(modifier = Modifier.width(10.dp))
        SubText(event.startTime?.toFormattedString("hh:mm a") ?: "Start time not set", Grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClockIcon()
        Spacer(modifier = Modifier.width(10.dp))
        SubText(event.endTime?.toFormattedString("hh:mm a") ?: "End time not set", Grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Place, contentDescription = "Location Icon", tint = Grey)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(if (event.location.isNotEmpty()) event.location else "Location not set", Grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Person, contentDescription = "Attendee Icon", tint = Grey)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(
            if (event.maxAttendees == 1) "${event.maxAttendees} person maximum" else "${event.maxAttendees} people maximum",
            Grey
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star Icon", tint = BratGreen)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(
            if (event.points == 1) "${event.points} point" else "${event.points} points",
            Color.Black
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 32.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.End
    ) {
        PrimaryButton("Join", onClick = {
            Log.d("backend fraud", "button was pushed")
        })
    }
}

/**
 * Extension function to convert Timestamp to a formatted String
 */
fun Timestamp.toFormattedString(format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(this.toDate())
}