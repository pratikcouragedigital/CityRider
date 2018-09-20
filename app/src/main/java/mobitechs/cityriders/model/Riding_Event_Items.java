package mobitechs.cityriders.model;


public class Riding_Event_Items {

    public String RidingPlace, MeetUpPlace, RidingDate, Note;
    public double ridingLat,ridingLongi,meetUpLat,meetUpLongi;

    public Riding_Event_Items(){

    }

    public Riding_Event_Items(String RidingPlace, String MeetUpPlace, String RidingDate, String Note, double ridingLat, double ridingLongi, double meetUpLat, double meetUpLongi) {
        this.RidingPlace = RidingPlace;
        this.MeetUpPlace = MeetUpPlace;
        this.RidingDate = RidingDate;
        this.Note = Note;
        this.ridingLat = ridingLat;
        this.ridingLongi = ridingLongi;
        this.meetUpLat = meetUpLat;
        this.meetUpLongi = meetUpLongi;

    }
}
