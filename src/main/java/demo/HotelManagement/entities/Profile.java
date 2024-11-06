package demo.HotelManagement.entities;


import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
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

    @Column(name="nationalId")
    private String nationalId;

    @Column(name="notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column(name="national_id_photo",nullable = true, length = 64)
    private String nationalIdPhoto;

    @Column(name="birthday")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name="avatar",nullable = true, length = 64)
    private String avatar;

    @Column(name="position",nullable = true)
    private String position;

    @Column(name="department",nullable = true)
    private String department;

    // email
    @Column(name="login_name", nullable = true, length = 50)
    private String loginName;

    @Column(name="login_password",nullable = true, length = 10)
    private String loginPassword;

    private String role;

    private Boolean isEnable;

    private Boolean accountNonLocked;

    private Integer failedAttempt;

    private Date lockTime;

    private String resetToken;

    @OneToMany(mappedBy = "profile",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;
    public Profile(){}

    @OneToMany(mappedBy = "profile",fetch = FetchType.EAGER)
    private List<CentralReservation> centralReservations;

    @OneToMany(mappedBy = "profile",fetch = FetchType.EAGER)
    private List<Cart> carts;

    @ManyToOne
    @JoinColumn(name="membership_id", nullable = true)
    private Membership membership;



    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "profiles_roles",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )



    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<CentralReservation> getCentralReservations() {
        return centralReservations;
    }

    public void setCentralReservations(List<CentralReservation> centralReservations) {
        this.centralReservations = centralReservations;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Profile(Long id, String firstName, String lastName, String title, String billingName, String billingAddress, String billingCode, Gender gender, String address, String district, String city, String country, String phone, String nationalId, String notes, ProfileStatus status, String nationalIdPhoto, LocalDate birthday, String avatar, String position, String department, String loginName, String loginPassword, String role, List<Reservation> reservationList, List<CentralReservation> centralReservations, List<Cart> carts, Membership membership) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCode = billingCode;
        this.gender = gender;
        this.address = address;
        this.district = district;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.nationalId = nationalId;
        this.notes = notes;
        this.status = status;
        this.nationalIdPhoto = nationalIdPhoto;
        this.birthday = birthday;
        this.avatar = avatar;
        this.position = position;
        this.department = department;
        this.loginName = loginName;
        this.loginPassword = loginPassword;
        this.role = role;
        this.reservationList = reservationList;
        this.centralReservations = centralReservations;
        this.carts = carts;
        this.membership = membership;
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

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Integer getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(Integer failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}
