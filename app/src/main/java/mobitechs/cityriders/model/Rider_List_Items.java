package mobitechs.cityriders.model;


public class Rider_List_Items {

    public String RiderName, RiderContact, RiderBikeName,RiderPhoto,RiderCity;

    public String IsApproved;
    public Rider_List_Items(){

    }

    public Rider_List_Items(String RiderName, String RiderContact, String RiderBikeName, String RiderPhoto, String RiderCity, String IsApproved) {
        
        this.RiderName = RiderName;
        this.RiderContact = RiderContact;
        this.RiderBikeName = RiderBikeName;
        this.RiderPhoto = RiderPhoto;
        this.RiderCity = RiderCity;
        this.IsApproved = IsApproved;

    }

    public Rider_List_Items(String riderName, String riderContactNo, String riderBikeName,String RiderCity, String IsApproved) {

        this.RiderName = riderName;
        this.RiderContact = riderContactNo;
        this.RiderBikeName = riderBikeName;
        this.RiderCity = RiderCity;
        this.IsApproved = IsApproved;
    }
}
