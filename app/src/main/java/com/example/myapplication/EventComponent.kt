package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.Grey

@Composable
fun EventComponent(event: Event) {
    val svgPainter = painterResource(id = R.drawable.cs_160_project_logo)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp),
    ) {
        Column(modifier = Modifier.height(100.dp)) {

//                Image(
//                    painter = rememberAsyncImagePainter(if (event.eventPicUri == "") svgPainter else event.eventPicUri),
//                    contentDescription = "Event picture",
//                    modifier = Modifier.fillMaxSize()
//                        .clip(RoundedCornerShape(10.dp)),
//                    contentScale = ContentScale.Crop
//                )
            Box(
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .background(Color.Gray)
                    .clip(RoundedCornerShape(10.dp))
            ) {

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
        SubText(event.date, Grey)
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
        SubText(event.time, Grey)
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
        SubText(event.location, Grey)
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
            if (event.maxAttendees == 1) event.maxAttendees.toString() + " person maximum" else event.maxAttendees.toString() + " people maximum",
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
            if (event.points == 1) event.points.toString() + " point" else event.points.toString() + " points",
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




