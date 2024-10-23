package demo.HotelManagement.entities;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="profiles")
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="title")
    private String title;

    @Column(name="billing_name")
    private String billingName;

    @Column(name="billing_address")
    private String billingAddress;

    @Column(name="billing_code")
    private String billingCode;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="address")
    private String address;

    @Column(name="district")
    private String district;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column(name="national_id_photo",nullable = true, length = 64)
    private String nationalIdPhoto;

    @Column(name="avatar",nullable = true, length = 64)
    private String avatar;

    // email
    @Column(name="login_name", nullable = true, length = 20)
    private String loginName;

    @Column(name="login_password",nullable = true, length = 64)
    private String loginPassword;

    @OneToMany(mappedBy = "profile",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;
    public Profile(){}

    @OneToMany(mappedBy = "profile",fetch = FetchType.EAGER)
    private List<CentralReservation> centralReservations;

    @ManyToOne
    @JoinColumn(name="membership_id", nullable = true)
    private Membership membership;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "profiles_roles",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }


    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<CentralReservation> getCentralReservations() {
        return centralReservations;
    }

    public void setCentralReservations(List<CentralReservation> centralReservations) {
        this.centralReservations = centralReservations;
    }

    public Profile(Long id, String firstName, String lastName, String billingName, String billingAddress, String billingCode, Gender gender, String address, String district, String city, String country, String phone, String nationalIdPhoto, String loginName, String loginPassword, List<Reservation> reservationList, List<CentralReservation> centralReservations, Membership membership, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCode = billingCode;
        this.gender = gender;
        this.address = address;
        this.district = district;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.nationalIdPhoto = nationalIdPhoto;
        this.loginName = loginName;
        this.loginPassword = loginPassword;
        this.reservationList = reservationList;
        this.centralReservations = centralReservations;
        this.membership = membership;
        this.roles = roles;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCode() {
        return billingCode;
    }

    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalIdPhoto() {
        return nationalIdPhoto;
    }

    public void setNationalIdPhoto(String nationalIdPhoto) {
        this.nationalIdPhoto = nationalIdPhoto;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

}
