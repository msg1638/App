package com.example.fcmtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fcmtest.database.FallEvent
import com.example.fcmtest.database.FallEventRepository
import kotlinx.coroutines.launch

    class FallDetectionListModel(application: Application) : AndroidViewModel(application) {
        private val repository = FallEventRepository(application)
        private val _fallEvents = MutableLiveData<List<FallEvent>>()
        val fallEvents: LiveData<List<FallEvent>> get() = _fallEvents

        fun loadEvents() {
            viewModelScope.launch {
                //repository.example1()
                _fallEvents.value = repository.getAllFallEvents()
            }
    }

        fun updateStatus(index: Int) {
            viewModelScope.launch {
                val updatedList = _fallEvents.value?.toMutableList() ?: mutableListOf()
                updatedList[index].status = "읽음"
                repository.updateFallEventStatus(updatedList[index].Id, updatedList[index].status)
                _fallEvents.value = updatedList  // UI 업데이트
            }
        }
        fun deleteSelectedEvents(events: List<FallEvent>) {
            viewModelScope.launch {
                repository.deleteFallEvents(events) // DB에서 삭제
                val currentList = _fallEvents.value?.toMutableList()
                currentList?.removeAll(events)
                _fallEvents.value = currentList?:mut
                }
        }
    }