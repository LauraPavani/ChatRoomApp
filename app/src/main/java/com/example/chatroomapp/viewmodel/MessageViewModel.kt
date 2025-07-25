package com.example.chatroomapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroomapp.Injection
import com.example.chatroomapp.data.Message
import com.example.chatroomapp.data.MessageRepository
import com.example.chatroomapp.data.User
import com.example.chatroomapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageRepository: MessageRepository
    private val _roomId = MutableLiveData<String>()
    private val userRepository: UserRepository
    private val _messages = MutableLiveData<List<Message>>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser
    val messages: LiveData<List<Message>> get() = _messages
    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance())
        loadCurrentUser()

    }
    private fun loadCurrentUser(){
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return
        viewModelScope.launch{
            _currentUser.value = userRepository.getUserByEmail(email)
        }
    }
    fun loadMessage(roomId: String){
        viewModelScope.launch {
            messageRepository.getChatMessages(roomId).collect { messageList ->
                _messages.value = messageList
            }
        }
    }
    fun sendMessage(roomId: String, messageText: String){
        val user = _currentUser.value ?: return
        val message = Message(
            text = messageText,
            senderId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
            senderFirstName = user.firstName,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            messageRepository.sendMessage(roomId, message)
            loadMessage(roomId)
        }
    }
    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessage(roomId)
    }

}