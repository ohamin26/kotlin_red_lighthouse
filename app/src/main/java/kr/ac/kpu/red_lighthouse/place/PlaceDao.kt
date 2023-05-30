package kr.ac.kpu.red_lighthouse.place

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.user.User

class PlaceDao {
    private val firebase = Firebase.firestore
    val ref = firebase.collection("places")
    fun setDataToFirebase(place: Place):Task<Void>{
        return  ref.document().set(place)
    }
    suspend fun getDataFromFirebase(placeId:String): Place?{
        return ref.document(placeId).get().await().toObject(Place::class.java)
    }
    suspend fun checkExistsWithNameAndAddress(name:String,address:String): MutableList<User> {
        return ref.whereEqualTo("placeName", name).whereEqualTo("placeAddress", address).get().await()
            .toObjects(User::class.java)
    }
    fun updatePlaceName(placeId: String,name: String):Task<Void>{
        return ref.document(placeId).update("placeName", name)
    }
    fun updatePlaceAddress(placeId: String,address: String):Task<Void>{
        return ref.document(placeId).update("placeAddress", address)
    }
    fun updatePlaceOpening(placeId: String,opening: String):Task<Void>{
        return ref.document(placeId).update("placeOpening", opening)
    }
    fun updatePlaceCall(placeId: String,call: String):Task<Void>{
        return ref.document(placeId).update("placeCall", call)
    }
    fun deletePlaceWithFirestore(placeId:String): Task<Void> {
        return ref.document(placeId).delete()
    }
}