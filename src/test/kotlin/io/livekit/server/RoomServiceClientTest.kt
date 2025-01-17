package io.livekit.server

import livekit.LivekitModels
import kotlin.test.*


class RoomServiceClientTest {

    companion object {
        const val HOST = TestConstants.HOST
        const val KEY = TestConstants.KEY
        const val SECRET = TestConstants.SECRET

        const val ROOM_NAME = "room_name"
        const val METADATA = "metadata"
    }

    lateinit var client: RoomServiceClient

    @BeforeTest
    fun setup() {
        client = RoomServiceClient.create(HOST, KEY, SECRET, true)
    }

    @Test
    fun createRoom() {
        val response = client.createRoom(
            name = ROOM_NAME,
            metadata = METADATA,
        ).execute()
        val room = response.body()

        assertTrue(response.isSuccessful)
        assertEquals(ROOM_NAME, room?.name)
        assertEquals(METADATA, room?.metadata)
    }

    @Test
    fun listRooms() {
        client.createRoom(ROOM_NAME).execute()
        val response = client.listRooms(null).execute()
        val rooms = response.body()

        assertTrue(response.isSuccessful)
        assertNotNull(rooms)
        assertTrue(rooms.any { room -> room.name == ROOM_NAME })
    }

    @Test
    fun deleteRoom() {
        client.createRoom(ROOM_NAME).execute()
        client.deleteRoom(ROOM_NAME).execute()
        val response = client.listRooms(null).execute()
        val rooms = response.body()

        assertTrue(response.isSuccessful)
        assertNotNull(rooms)
        assertTrue(rooms.none { room -> room.name == ROOM_NAME })
    }

    @Test
    fun updateRoomMetadata() {
        val metadata = "NewMetadata"
        client.createRoom(ROOM_NAME).execute()
        val response = client.updateRoomMetadata(ROOM_NAME, metadata).execute()
        val room = response.body()

        assertTrue(response.isSuccessful)
        assertNotNull(room)
        assertEquals(metadata, room.metadata)
    }

    @Test
    fun listParticipants() {
        client.createRoom(ROOM_NAME).execute()
        val response = client.listParticipants(ROOM_NAME).execute()
        val participants = response.body()
        assertTrue(response.isSuccessful)
        assertNotNull(participants)
        assertEquals(0, participants.size)
    }

    @Test
    fun getParticipant() {
        val response = client.getParticipant(ROOM_NAME, "fdsa").execute()
    }

    @Test
    fun sendData() {
        client.createRoom(ROOM_NAME).execute()
        val response = client.sendData(ROOM_NAME, "data".toByteArray(), LivekitModels.DataPacket.Kind.RELIABLE).execute()
        assertTrue(response.isSuccessful)
    }
}