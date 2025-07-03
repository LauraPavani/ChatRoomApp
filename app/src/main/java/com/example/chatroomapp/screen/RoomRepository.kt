package com.example.chatroomapp.screen

import com.example.chatroomapp.data.Room
import kotlinx.coroutines.delay
import java.util.UUID

class RoomRepository {

    private val rooms = mutableListOf<Room>()

    suspend fun createRoom(name: String) {
        delay(500) // simulate network/database delay
        rooms.add(Room(id = UUID.randomUUID().toString(), name = name))
    }

    suspend fun getRooms(): List<Room> {
        delay(500)
        return rooms
    }
}