package kr.ac.kpu.red_lighthouse.placeReview

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.user.User

class PlaceReviewDao {
    private val firebase = Firebase.firestore
    val ref = firebase.collection("placeReviews")
    fun setDataToFirebase(placeReview: PlaceReview): Task<Void> {
        return  ref.document().set(placeReview)
    }
    suspend fun getDataFromFirebase(placeReviewId:String): PlaceReview?{
        return ref.document(placeReviewId).get().await().toObject(PlaceReview::class.java)
    }
    suspend fun checkExistsWithAddress(address:String): MutableList<User> {
        return ref.whereEqualTo("placeAddress", address).get().await()
            .toObjects(User::class.java)
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
}