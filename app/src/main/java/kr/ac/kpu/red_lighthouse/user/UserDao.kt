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

/**
 * 유저 데이터를 관리 및 제어하는 클래스.
 *
 * 최종 수정 일자 : 2023.6.8
 */
class UserDao {
    //파이어베이스 연동
    private var auth: FirebaseAuth = Firebase.auth
    private val firebase = Firebase.firestore
    val ref = firebase.collection("users")

    /** 회원가입 기능 (Firebase Auth)
     * @param email
     * @param password
     * @return Task<AuthResult>
     */
    fun register(email:String,password:String):Task<AuthResult>{
        return auth.createUserWithEmailAndPassword(email, password)
    }

    /** 로그인 기능 (Firebase Auth)
     * @param email
     * @param password
     * @return Task<AuthResult>
     */
    fun login(email:String,password:String):Task<AuthResult>{
        return auth.signInWithEmailAndPassword(email,password)
    }

    /** 회원정보를 가져오는 기능 (Firestore Database)
     * @param userId
     * @return User?
     */
    suspend fun getDataFromFirebase(userId:String): User? {
        return ref.document(userId).get().await().toObject(User::class.java)
    }

    /** 데이터 저장 기능 (Firestore Database)
     * @param user
     * @return Task<Void>
     */
    fun setDataToFirebase(user:User):Task<Void>{
        return ref.document(user.userId).set(user)
    }

    /** 이메일로 회원을 찾는 기능 (Firestore Database)
     * @param email
     * @return MutableList<User>
     */
    suspend fun checkExistsWithEmail(email:String): MutableList<User> {
        return ref.whereEqualTo("userEmail", email).get().await()
            .toObjects(User::class.java)
    }

    /** 닉네임으로 회원을 찾는 기능 (Firestore Database)
     * @param nickname
     * @return MutableList<User>
     */
    suspend fun checkExistsWithNickname(nickname:String): MutableList<User> {
        return ref.whereEqualTo("userNickname", nickname).get().await()
            .toObjects(User::class.java)
    }

    /** 비밀번호 초기화 메일을 전송하는 기능 (Firebase Auth)
     * @param email
     * @return Task<Void>
     */
    fun resetPassword(email:String): Task<Void> {
        return Firebase.auth.sendPasswordResetEmail(email)
    }

    /** 닉네임을 수정하는 기능 (Firebase Auth)
     * @param userId
     * @param nickname
     * @return Task<Void>
     */
    fun updateNickname(userId:String,nickname: String): Task<Void> {
        return ref.document(userId).update("userNickname",nickname)
    }

    /** 문서에서 회원을 삭제하는 기능 (Firebase Auth)
     * @param userId
     * @return Task<Void>
     */
    fun deleteUserWithFirestore(userId:String): Task<Void> {
        return ref.document(userId).delete()
    }

    /** 인증에서 회원을 삭제하는 기능 (Firestore Database)
     * @return Task<Void>
     */
    fun deleteUserWithFireAuth(): Task<Void> {
        val user = auth.currentUser!!
        return  user.delete()
    }
}