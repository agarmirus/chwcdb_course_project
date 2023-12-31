package entity;

import java.util.Date;

public class Player
{
    private Integer id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private Date birthDate;
    private String country;
    private Integer raiting;

    public Player() {}
    public Player(final Integer id) { this.id = id; }
    public Player(
        final Integer id,
        String firstName,
        String secondName,
        String thirdName,
        final Date birthDate,
        String country,
        final Integer raiting
    )
    {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.birthDate = birthDate;
        this.country = country;
        this.raiting = raiting;
    }

    public void setId(final Integer id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }
    public void setThirdName(String thirdName) { this.thirdName = thirdName; }
    public void setBirthDate(final Date birthDate) { this.birthDate = birthDate; }
    public void setCountry(String country) { this.country = country; }
    public void setRaiting(final Integer raiting) { this.raiting = raiting; }

    public Integer getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }
    public String getThirdName() { return thirdName; }
    public Date getBirthDate() { return birthDate; }
    public String getCountry() { return country; }
    public Integer getRaiting() { return raiting; }
}
