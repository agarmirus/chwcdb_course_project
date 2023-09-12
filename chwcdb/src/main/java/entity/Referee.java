package entity;

import java.util.Date;

public class Referee
{
    private Integer id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private Date birthDate;
    private String country;

    public Referee() {}
    public Referee(
        final Integer id,
        String firstName,
        String secondName,
        String thirdName,
        final Date birthDate,
        String country
    )
    {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.birthDate = birthDate;
        this.country = country;
    }

    public void setId(final Integer id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }
    public void setThirdName(String thirdName) { this.thirdName = thirdName; }
    public void setBirthDate(final Date birthDate) { this.birthDate = birthDate; }
    public void setCountry(String country) { this.country = country; }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }
    public String getThirdName() { return thirdName; }
    public Date getBirthDate() { return birthDate; }
    public String getCountry() { return country; }
}
