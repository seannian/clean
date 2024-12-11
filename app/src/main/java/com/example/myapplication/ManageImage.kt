package com.example.myapplication

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import java.io.File
import java.util.UUID

fun uploadImage(uri: Uri, callback: (String?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imageRef = storageRef.child("thumbnails/${UUID.randomUUID()}")

    val uploadTask = imageRef.putFile(uri)

    // Handle upload success and failure
    uploadTask.addOnSuccessListener {
        // Retrieve the download URL
        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            val firebaseUri = downloadUri.toString()
            Log.d("ImageURI", "Download URL: $firebaseUri")
            callback(firebaseUri) // Return the URL via callback
        }.addOnFailureListener { e ->
            Log.e("ImageURI", "Failed to get download URL", e)
            callback(null) // Notify failure
        }
    }.addOnFailureListener { e ->
        Log.e("ImageUpload", "Failed to upload image", e)
        callback(null) // Notify failure
    }
}