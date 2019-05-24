public class Contact {
    private String name;
    private String number;
    private String email;
    private String company;

    public Contact(String name, String number, String email, String company) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.company = company;
    }

    //getters and setters

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() { return this.number; }
    public void setNumber( String number ) { this.number = number; }

    public String getEmail() { return this.email; }
    public void setEmail( String email ) { this.email = email; }

    public String getCompany() { return this.company; }
    public void setCompany(String company) { this.company = company; }

    //check phone number length and basic email format
    public void inputCheck() {
        if (this.getNumber().length() > 10 || this.getNumber().length() < 7) {
            this.setNumber("Invalid Number");
        }
        int atPosition = this.getEmail().indexOf("@");
        if (atPosition == -1 || atPosition == 0 || atPosition == this.getEmail().length()-1) {
            this.setEmail("Invalid Email");
        }
    }
    //toString in JSON format
    @Override
    public String toString() {
        return "{" + "\"name\":\"" + name +
                '\"' + ", \"number\":\"" + number +
                '\"' + ", \"email\":\"" + email +
                '\"' + ", \"company\":\"" + company +
                '\"' + '}';
    }
}
