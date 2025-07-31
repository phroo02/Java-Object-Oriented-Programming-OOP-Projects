package social;
import java.util.Collection;
import java.util.HashSet;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "social_group")

public class Group {

@Id
  private String name;

 
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "person_group",
    joinColumns = @JoinColumn(name = "group_name"),
    inverseJoinColumns = @JoinColumn(name = "person_code")
)
private java.util.Set<Person> members = new HashSet<>();  

  public Group() {}

  public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void addMember(Person person) {
    members.add(person);
    } 
    public Collection<Person> getList(){
        return members;

    }
    public void setMembers(java.util.Set<Person> p){
        this.members=p;
    }

}
