package kr.ac.kpu.red_lighthouse.user

import android.app.Activity
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.function.CheckUserId

class UserDao {
    private var auth: FirebaseAuth = Firebase.auth
    private val firebase = Firebase.firestore
    val ref = firebase.collection("users")



    fun register(email:String,password:String):Task<AuthResult>{
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email:String,password:String):Task<AuthResult>{
        return auth.signInWithEmailAndPassword(email,password)
    }

    suspend fun getDataFromFirebase(userId:String): User? {
        return ref.document(userId).get().await().toObject(User::class.java)
    }

    fun setDataToFirebase(user:User):Task<Void>{
        return ref.document(user.userId).set(user)
    }

    suspend fun checkExistsWithEmail(email:String): MutableList<User> {
        return ref.whereEqualTo("userEmail", email).get().await()
            .toObjects(User::class.java)
    }

    suspend fun checkExistsWithNickname(nickname:String): MutableList<User> {
        return ref.whereEqualTo("userNickname", nickname).get().await()
            .toObjects(User::class.java)
    }

    fun resetPassword(email:String): Task<Void> {
        return Firebase.auth.sendPasswordResetEmail(email)
    }

    fun updateNickname(userId:String,nickname: String): Task<Void> {
        return ref.document(userId).update("userNickname",nickname)
    }

    fun deleteUserWithFirestore(userId:String): Task<Void> {
        return ref.document(userId).delete()
    }

    fun deleteUserWithFireAuth(userId:String): Task<Void> {
        val user = auth.currentUser!!
        return  user.delete()
    }
}