package kr.ac.kpu.red_lighthouse.placeReview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION_CODES.S
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kr.ac.kpu.red_lighthouse.user.User
import java.io.File

class PlaceReviewDao {
    private val firebase = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    val ref = firebase.collection("placeReviews")
    fun setDataToFirebase(placeReview: PlaceReview): Task<Void> {
        return  ref.document().set(placeReview)
    }
    suspend fun getDataFromFirebase(placeReviewId:String): PlaceReview?{
        return ref.document(placeReviewId).get().await().toObject(PlaceReview::class.java)
    }
    suspend fun getDataWithAddress(address:String): MutableList<PlaceReview> {
        return ref.whereEqualTo("address", address).get().await()
            .toObjects(PlaceReview::class.java)
    }
    fun deletePlaceReviewWithFirestore(placeReviewId:String): Task<Void> {
        return ref.document(placeReviewId).delete()
    }
    fun countOfReviewWithAddress(address:String): Int {
        var cnt:Int = 0
        ref.whereEqualTo("address", address)
            .get()
            .addOnSuccessListener { documents ->
                 cnt = documents.size();
            }
            .addOnFailureListener { exception ->
                Log.w("firebase", "Error getting documents: ", exception)
            }
        return cnt
    }

    //파이어베이스 스토리지에서 이미지 저장 및 가져오기
    suspend fun uploadPhoto (
        imageURI: Uri,
        name:String,
    ): String{
        val time = "${System.currentTimeMillis()}"
        val fileName = "${name}.png"
        var uri : String = ""
        return withContext(Dispatchers.IO) {
            val storage = FirebaseStorage.getInstance()
            storage.reference.child("review/photo/$time").child(fileName)
                .putFile(imageURI)
                .await()
            storage.reference.child("review/photo/$time").child(fileName).downloadUrl
                .await().toString()
            //imageView.setImageBitmap(BitmapFactory.decodeStream(URL(imageUrl).openStream()))
        }
    }

}