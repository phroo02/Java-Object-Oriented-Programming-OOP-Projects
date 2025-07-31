package social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
class Person {
  @Id
  private String code;
  private String name;
  private String surname;
  @ManyToMany(fetch = FetchType.EAGER)
  private List<Person> friends = new ArrayList<>();

  @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
  private java.util.Set<Group> groups = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
private List<Post> posts = new ArrayList<>();

  public Person() {
    // default constructor is needed by JPA
  }

  Person(String code, String name, String surname) {
    this.code = code;
    this.name = name;
    this.surname = surname;
  }

  String getCode() {
    return code;
  }

  String getName() {
    return name;
  }

  String getSurname() {
    return surname;
  }

 public void addFriend(Person p) {
    if (!friends.contains(p)) {
      friends.add(p);
    }
  }

  public Collection<Person> getList(){
    return friends;
  }
  public void addGroup(Group group) {
    groups.add(group);
}
public Collection<Group> groups(){
    return groups;
  }
  public void addPost(Post post) {
    posts.add(post);
}

public List<Post> getPosts() {
    return posts;
}
  

  //....
}
