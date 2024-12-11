package com.example.myapplication

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import java.io.File

fun uploadImage(uri: String = "storage/emulated/0/Pictures/IMG_20241210_154003.jpg") {
    // access to the database
    val storage = Firebase.storage("gs://cs160-4a42d.appspot.com")
    val storageRef = storage.reference
    val file = Uri.fromFile(File(uri))
    val fileToUpload = File(uri)

    // checks for valid uri
    if (!fileToUpload.exists()) {
        Log.e("ImageUpload", "File does not exist: $uri")
        return
    }

    val riversRef = storageRef.child("images/${file.lastPathSegment}")
    val uploadTask = riversRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
    uploadTask.addOnFailureListener { exception ->
        if (exception is StorageException) {
            val httpCode = exception.httpResultCode
            Log.e("ImageUpload", "HTTP Error: $httpCode", exception)
        } else {
            Log.e("ImageUpload", "Unknown error occurred", exception)
        }
    }.addOnSuccessListener {
        Log.d("ImageUpload", "Image successfully uploaded: ${fileToUpload.name}")
    }.addOnProgressListener { taskSnapshot ->
        val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
        Log.d("ImageUpload", "Upload progress: $progress%")
    }
}

// given the name of an image source, prints corresponding url in firebase
fun getImageURI(imageName: String = "Screenshot 2024-02-12 at 3.20.20 PM.png") {
    val storage = Firebase.storage("gs://cs160-4a42d.appspot.com")
    val storageRef = storage.reference
    val imageRef = storageRef.child("images/$imageName")

    // Get the download URL
    imageRef.downloadUrl
        .addOnSuccessListener { uri ->
            // Successfully got the URL
            Log.d("ImageURI", "Download URL: $uri")
        }
        .addOnFailureListener { exception ->
            // Handle any errors
            Log.e("ImageURI", "Error getting download URL", exception)
        }
}