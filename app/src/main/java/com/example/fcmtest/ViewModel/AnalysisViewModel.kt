package com.example.fcmtest.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fcmtest.FallAPIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONException
import org.json.JSONObject

class AnalysisViewModel(application: Application) : ViewModel() {
    private var Id = ""
    var videomodel = VideoViewModel(application)
    private val _progress = MutableStateFlow<Int>(0)
    val progress = _progress.asStateFlow()
    private var result : String = ""
    suspend fun requestAnalyze(fall_id : String) : String{
        Id = fall_id
        return FallAPIClient.requestAnalyze(fall_id)
    }
    suspend fun checkProgress(): Int {
        val result = FallAPIClient.checkProgress(Id)
        _progress.value = result
        return result
    }
    fun setResult(response : String){
        result = response
    }
    fun getFallType(): String {
        if (result.isBlank()) {
            Log.w("getFallType", "result is null or blank")
            return ""
        }
        val directionMap = mapOf(
            "Fall Forward" to "정면 낙상",
            "Fall Backward" to "후면 낙상",
            "Fall Left" to "측면 낙상",
            "Fall Right" to "측면 낙상",
            "Fall Down" to "힘풀림",
            "Fall Slide" to "미끄러짐"
        )

        return try {
            val json = JSONObject(result)
            val type = json.optString("fall_direction", "")
            val direction = directionMap[type]?:""
            Log.d("낙상 방향", direction)
            direction
        } catch (e: JSONException) {
            Log.e("getFallType", "Invalid JSON: ${e.message}")
            ""
        }
    }

    fun getFallJoint(): String {
        if (result.isBlank()) {
            Log.w("getFallDescription", "result is null or blank")
            return ""
        }
        try{
            val json = JSONObject(result)
            val contactsArray = json.optJSONArray("contacts")
            val jointList = mutableListOf<String>()
            if (contactsArray != null) {
                for (i in 0 until contactsArray.length()) {
                    val contact = contactsArray.getJSONObject(i)
                    val joint = contact.optString("joint", "")
                    val jointMap = mapOf(
                        "Hip" to "골반",
                        "Right Hip" to "오른쪽 엉덩이",
                        "Right Knee" to "오른쪽 무릎",
                        "Right Ankle" to "오른쪽 발목",
                        "Left Hip" to "왼쪽 엉덩이",
                        "Left Knee" to "왼쪽 무릎",
                        "Left Ankle" to "왼쪽 발목",
                        "Spine" to "척추",
                        "Thorax" to "흉부",
                        "Neck/Nose" to "목 또는 코",
                        "Head" to "머리",
                        "Left Shoulder" to "왼쪽 어깨",
                        "Left Elbow" to "왼쪽 팔꿈치",
                        "Left Wrist" to "왼쪽 손목",
                        "Right Shoulder" to "오른쪽 어깨",
                        "Right Elbow" to "오른쪽 팔꿈치",
                        "Right Wrist" to "오른쪽 손목"
                    )
                    val joint_kor = jointMap[joint]?:""
                    if (joint_kor.isNotBlank()) {
                        jointList.add(joint_kor)
                    }
                }
            }
            Log.d("Joint", jointList.toString())
            return jointList.joinToString(",")
        } catch (e: JSONException) {
            Log.e("getFallJoint", "JSON parsing error: ${e.message}")
            return ""
        }
    }

    fun clear(){
        Id = ""
        _progress.value = 0
        result = ""
    }
}