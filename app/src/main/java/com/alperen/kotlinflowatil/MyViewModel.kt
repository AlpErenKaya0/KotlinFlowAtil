package com.alperen.kotlinflowatil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MyViewModel:ViewModel() {
    val countDownTimerFlow = flow<Int>{
        val countDownFrom = 10
        var counter = countDownFrom
        emit(countDownFrom)
        while (counter>0) {
            delay(1000)
            counter--
            emit(counter)
        }
    }
    init {
        collectInViewModel()
    }
    private fun collectInViewModel(){
        viewModelScope.launch {
            countDownTimerFlow
                .filter {
                    it %3 == 0
                }
                .collect {
                    println("counter : ${it}"   )
                }
           /*
            countDownTimerFlow.collectLatest {
                delay(2000)
                println("delay: ${it}")
            //2 saniyeden önce bir şey gelirse tam collect etmeden yeni veri geldiği için siler collect etmeden
            }
*/
           /*
            countDownTimerFlow.onEach {
                println(it)
            }.launchIn(viewModelScope)

            */
        }
    }
private val _liveData = MutableLiveData<String>("Bla bla, some cool text")
    val liveData: LiveData<String> = _liveData

    fun changeLiveDataValue(){
        _liveData.value="LiveData"
    }
    private val _stateFlow = MutableStateFlow("Kotlin State Flow")
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun changeStateFlowValue(){
        _stateFlow.value = "State Flow"
    }
    fun changeSharedFlowValue(){
        viewModelScope.launch {
            _sharedFlow.emit("Shared Flow")
        }
    }
}