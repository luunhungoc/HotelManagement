package demo.HotelManagement.entities;

public class AvailableRoomInfo {

    private RoomType roomType;       // Thêm thuộc tính RoomType vào AvailableRoomInfo
    private int availableRooms;      // Số lượng phòng trống

    // Constructor đầy đủ
    public AvailableRoomInfo(RoomType roomType, int availableRooms) {
        this.roomType = roomType;
        this.availableRooms = availableRooms;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }
}
