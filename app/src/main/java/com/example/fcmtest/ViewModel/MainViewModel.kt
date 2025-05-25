package com.example.fcmtest.ViewModel

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fcmtest.FallAPIClient
import com.example.fcmtest.Nav.ScreenRoute
import com.example.fcmtest.Util.FilterState
import com.example.fcmtest.Util.ServerIpManager
import com.example.fcmtest.customUI.LoadingState
import com.example.fcmtest.database.FallEvent
import com.example.fcmtest.database.FallEventRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FallEventRepository(application)

    //val fallEvents: LiveData<List<FallEvent>> get() = _fallEvents
    var fallEvents = repository.eventFlow
    var selectedPeriod = mutableStateOf("이번주")
    var stats = repository.getStats(selectedPeriod.value)
    val detailmodel = DetailViewModel(application)
    val analysismodel = AnalysisViewModel(application)
    private val _serverStatus = MutableStateFlow<Boolean>(false)
    val serverstatus: StateFlow<Boolean> = _serverStatus
    private val _isGranted = MutableStateFlow(checkPermission())
    val isGranted: StateFlow<Boolean> get() = _isGranted
    private val _showMap = mutableStateMapOf<ScreenRoute, Boolean>().apply {
        ScreenRoute.values().forEach { put(it, false) }
    }
    val showMap: Map<ScreenRoute, Boolean> get() = _showMap

    fun setActiveScreen(route: String) {
        ScreenRoute.values().forEach {
            _showMap[it] = (it.route == route)
        }
    }
    private fun startServerStatusMonitoring() {
        viewModelScope.launch {
            while (isActive) {
                checkStatus()
                Log.d("인터넷체크", "체크...${serverstatus.value}")
                delay(10_000) // 10초마다 체크
            }
        }
    }

    init {
        LoadingState.show()
        //지금 serverip가 중구난방 퍼져있음. 통합해야함
        //FallAPIClient.setServerUrl("10.0.2.2")
        FallAPIClient.setServerUrl(ServerIpManager.getServerIp(application.applicationContext))
        updateStatsResult(selectedPeriod.value)
        LoadingState.hide()
        viewModelScope.launch {
            /*repository.eventFlow.collect{
                _fallEvents.value = it
            }*/
            repository.example1()
            LoadingState.show()
            checkStatus()
            LoadingState.hide()

        }
        startServerStatusMonitoring()
    }
    fun selectCamera(index : Int, onSuccess: () -> Unit, onFailure:() -> Unit){
        viewModelScope.launch{
            LoadingState.show()
            val result = FallAPIClient.selectCamera(index)
            if(result){
                onSuccess()
            }
            else{
                onFailure()
            }
            LoadingState.hide()
        }
    }
    suspend fun checkStatus() {
        _serverStatus.value = FallAPIClient.checkStatus()
    }
    fun setServerIp(ip : String) : Boolean{
        val success = FallAPIClient.setServerUrl(ip)
        if(success) ServerIpManager.saveServerIp(getApplication(), FallAPIClient.GetServerUrl())
        return success
    }
    fun fetchAnalysisResult(
        event: FallEvent,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        analysismodel.clear()
        viewModelScope.launch {
            LoadingState.show()
            try {
                // 1. 분석 요청ly
                val result = analysismodel.requestAnalyze(event.Id)
                if (result.isEmpty() || result == "error") {
                    onFailure()
                    return@launch
                }
                if(result == "done"){
                    //결과 가져오기
                    Log.d("결과 출력","시작1")
                    analysismodel.setResult(FallAPIClient.getAnalysisResult(event.Id))
                    onSuccess()
                }
                else if(result == "processing" || result == "created"){
                    // 2. 진행률 확인 루프
                    while (true) {
                        val progress = analysismodel.checkProgress() // 진행률을 직접 반환하도록 수정 필요
                        if (progress == 100) {
                            //결과가져오기
                            Log.d("결과 출력","시작2")
                            analysismodel.setResult(FallAPIClient.getAnalysisResult(event.Id))
                            onSuccess()
                            break
                        } else if (progress == -1) {
                            onFailure()
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onFailure()
            } finally {
                LoadingState.hide()
            }
        }
    }
    fun getAnalysisResult(event : FallEvent, onFailure: () -> Unit){
        viewModelScope.launch {
            LoadingState.show()
            try{
                analysismodel.setResult(FallAPIClient.getAnalysisResult(event.Id))
            }
            catch (e: Exception){
                onFailure()
                e.printStackTrace()
            }
            finally {
                LoadingState.hide()
            }

        }

    }
    fun setDetailEvent(event: FallEvent) {
        detailmodel.setEvent(event)
    }

    fun getDetailEvent(): FallEvent {
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
        Log.d("권한체크", "권한체크")
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
    fun getFilteredEvents(
        filterState: FilterState
    ): Flow<List<FallEvent>> {
        return fallEvents.map { events ->
            Log.d("getFilteredEvents", "Filtering events: read=${filterState.selectedRead}, analysis=${filterState.selectedAnalysis}")
            events.filter { event ->
                val readMatches = when (filterState.selectedRead) {
                    1 -> event.status == "읽지 않음"
                    2 -> event.status == "읽음"
                    else -> true
                }

                val analysisMatches = when (filterState.selectedAnalysis) {
                    1 -> event.analysis == "미분석"
                    2 -> event.analysis == "분석"
                    else -> true
                }
                var timeMatches = true
                val timeRange = Pair(filterState.startDate,filterState.endDate)
                if(filterState.selectedDate == 1){
                    timeMatches = when {
                        timeRange.first != null && timeRange.second != null ->
                            event.timestamp in timeRange.first!!..timeRange.second!!
                        timeRange.first != null ->
                            event.timestamp >= timeRange.first!!
                        timeRange.second != null ->
                            event.timestamp <= timeRange.second!!
                        else -> true
                    }
                }
                readMatches && analysisMatches && timeMatches
            }
        }
    }

    fun getAnalysisEvents(Analysis: Int): Flow<List<FallEvent>> {
        return fallEvents.map { events ->
            when (Analysis) {
                1 -> events.filter { it.analysis == "미분석" }
                2 -> events.filter { it.analysis == "분석" }
                else -> events
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatus(fallEvent: FallEvent) {
        viewModelScope.launch {
            //val updatedList = _fallEvents.value?.toMutableList() ?: mutableListOf()
            //updatedList[index].status = "읽음"
            //repository.updateFallEventStatus(updatedList[index].Id, updatedList[index].status)
            //_fallEvents.value = updatedList  // UI 업데이트
            if (fallEvent.status == "읽지 않음") {
                repository.updateFallEventReadStatus(
                    fallEvent.Id.toString()
                )
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAnalysis(fallEvent: FallEvent) {
        viewModelScope.launch {
            if (fallEvent.analysis == "미분석") {
                repository.updateFallEventAnalysisStatus(
                    fallEvent.Id.toString()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteSelectedEvents(events: List<FallEvent>) {
        viewModelScope.launch {
            //val currentList = _fallEvents.value?.toMutableStateList()
            //currentList?.removeAll(events)
            //_fallEvents.value = currentList?:emptyList<FallEvent>()
            repository.deleteFallEvents(events) // DB에서 삭제
            //db삭제를 나중에 하니까 ui갱신이 되네...이유가 뭘까
            //_fallEvents.value = repository.getAllFallEvents()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatsResult(Period: String) {
        stats = repository.getStats(Period)

    }
}

