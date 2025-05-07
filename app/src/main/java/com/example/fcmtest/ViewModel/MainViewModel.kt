package com.example.fcmtest.ViewModel

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fcmtest.FallAPIClient
import com.example.fcmtest.Util.ServerIpManager
import com.example.fcmtest.analytics.StatsResult
import com.example.fcmtest.customUI.LoadingState
import com.example.fcmtest.database.FallEvent
import com.example.fcmtest.database.FallEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FallEventRepository(application)
    private val _fallEvents = MutableLiveData<List<FallEvent>>()
    //val fallEvents: LiveData<List<FallEvent>> get() = _fallEvents
    var fallEvents = mutableStateListOf<FallEvent>()
    var stat = mutableStateOf(StatsResult())
    var serverIp = ServerIpManager.getServerIp(application)
    var selectedPeriod = mutableStateOf("이번주")
    val detailmodel = DetailViewModel(application)
    private val _serverStatus = MutableStateFlow<Boolean>(false)
    val serverstatus: StateFlow<Boolean> = _serverStatus
    private val _isGranted = MutableStateFlow(checkPermission())
    val isGranted: StateFlow<Boolean> get() = _isGranted


    private fun startServerStatusMonitoring() {
        viewModelScope.launch {
            while (isActive) {
                checkStatus()
                Log.d("인터넷체크","체크...${serverstatus.value}")
                delay(10_000) // 10초마다 체크
            }
        }
    }
    init{
        viewModelScope.launch {
            checkStatus()
        }
        //startServerStatusMonitoring()
    }
    suspend fun checkStatus(){
        LoadingState.show()
        _serverStatus.value = FallAPIClient.checkStatus()
        LoadingState.hide()
    }
    suspend fun fetchAnalysisResult(event: FallEvent): Boolean {
        LoadingState.show()
        return try {
            // 예: 서버 요청
            delay(2000)
            true
        } catch (e: Exception) {
            false
        } finally {
            LoadingState.hide()
        }
    }

    fun setDetailEvent(event: FallEvent){
        detailmodel.setEvent(event)
    }
    fun getDetailEvent(): FallEvent{
        return detailmodel.getEvent()
    }
    fun refreshPermissionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            _isGranted.value = ContextCompat.checkSelfPermission(
                getApplication(), android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            _isGranted.value = true
        }
    }

    fun checkPermission(): Boolean {
        Log.d("권한체크","권한체크")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                getApplication(), android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
    fun updatePermissionResult(granted: Boolean) {
        _isGranted.value = granted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEvents() {
        viewModelScope.launch {
            LoadingState.show()
            async(Dispatchers.IO){
                repository.example1()
                fallEvents.addAll(repository.getAllFallEvents())
                getStatsResult("이번주")
            }.await()
            LoadingState.hide()
        }
    }

    fun getEvents(status: Int): List<FallEvent> {
        val filteredEvents = when (status) {
            1 -> return fallEvents.filter { it.status == "읽지 않음" }
            2 -> return fallEvents.filter { it.status == "읽음" }
            else -> return fallEvents
        }
    }

    fun getAnalysisEvents(Analysis: Int): List<FallEvent> {
        val filteredEvents = when (Analysis) {
            1 -> return fallEvents.filter { it.analysis == "미분석" }
            2 -> return fallEvents.filter { it.analysis == "분석" }
            else -> return fallEvents
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatus(index: Int) {
        viewModelScope.launch {
            //val updatedList = _fallEvents.value?.toMutableList() ?: mutableListOf()
            //updatedList[index].status = "읽음"
            //repository.updateFallEventStatus(updatedList[index].Id, updatedList[index].status)
            //_fallEvents.value = updatedList  // UI 업데이트
            var fallevent = _fallEvents.value?.toMutableList()?.get(index)

            if(fallevent?.status == "읽지 않음"){
                fallevent.status = "읽음"
                _fallEvents.value?.toMutableList()?.set(index, fallevent.copy())
                repository.updateFallEventStatus(fallevent.Id.toString(), fallevent.status.toString())
                getStatsResult(selectedPeriod.value)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteSelectedEvents(events: List<FallEvent>) {
        viewModelScope.launch {
            //val currentList = _fallEvents.value?.toMutableStateList()
            //currentList?.removeAll(events)
            //_fallEvents.value = currentList?:emptyList<FallEvent>()
            fallEvents.removeAll(events)
            repository.deleteFallEvents(events) // DB에서 삭제
            getStatsResult(selectedPeriod.value)
            //db삭제를 나중에 하니까 ui갱신이 되네...이유가 뭘까
            //_fallEvents.value = repository.getAllFallEvents()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStatsResult(Period : String){
        viewModelScope.launch {
            val new_stat = repository.getStats(Period)
            stat.value = new_stat
        }
    }
}
