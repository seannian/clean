package com.example.myapplication

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import java.io.File
import java.util.UUID

fun uploadImage(uri: Uri): String {
    var firebaseUri = ""
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imageRef = storageRef.child("thumbnails/${UUID.randomUUID()}")
    val uploadTask = imageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            // Store the download URL as the eventPicUri
            firebaseUri = downloadUri.toString()
            Log.d("ImageURI", "Download URL: $uri")
        }
    }.addOnFailureListener { e ->
        Log.e("ImageUpload", "Failed to upload image", e)
    }
    return firebaseUri
}