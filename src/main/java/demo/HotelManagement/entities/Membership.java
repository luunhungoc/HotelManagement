package demo.HotelManagement.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="memberships")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="description")
    private String description;

    @Column(name="level")
    private String level;

    @OneToMany(mappedBy = "membership",fetch = FetchType.EAGER)
    private List<Profile> profileList;
    public Membership(){}

    public Membership(Long id, String description, String level, List<Profile> profileList) {
        this.id = id;
        this.description = description;
        this.level = level;
        this.profileList = profileList;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
