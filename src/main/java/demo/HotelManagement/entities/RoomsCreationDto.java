package demo.HotelManagement.entities;
import java.util.ArrayList;
import java.util.List;

public class RoomsCreationDto {

    private List<Room> rooms;

    public RoomsCreationDto() {
        this.rooms = new ArrayList<>();
    }

    public RoomsCreationDto(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> books) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
