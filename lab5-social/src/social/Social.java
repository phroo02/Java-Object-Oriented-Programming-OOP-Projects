package social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

/**
 * Facade class for the social network system.
 * 
 */
public class Social {

  private final PersonRepository personRepository = new PersonRepository();
  private final GroupRepository groupRepository = new GroupRepository();
  private final PostRepository postRepository = new PostRepository();
  
  /**
   * Creates a new account for a person
   * 
   * @param code    nickname of the account
   * @param name    first name
   * @param surname last name
   * @throws PersonExistsException in case of duplicate code
   */
  public void addPerson(String code, String name, String surname) throws PersonExistsException {
    if (personRepository.findById(code).isPresent()){    // check if db already contains the code
        throw new PersonExistsException();
    }
    Person person = new Person(code, name, surname);    // create the person as a POJO
    personRepository.save(person);                      // save it to db
  } 

  /**
   * Retrieves information about the person given their account code.
   * The info consists in name and surname of the person, in order, separated by
   * blanks.
   * 
   * @param code account code
   * @return the information of the person
   * @throws NoSuchCodeException if a person with that code does not exist
   */
  public String getPerson(String code) throws NoSuchCodeException {
    if(!personRepository.findById(code).isPresent()) {
      throw  new NoSuchCodeException();
    }
    Person  p=personRepository.findById(code).get();
    return code + " " + p.getName()+" "+p.getSurname();
  }

  /**
   * Define a friendship relationship between two persons given their codes.
   * <p>
   * Friendship is bidirectional: if person A is adding as friend person B, that means
   * that person B automatically adds as friend person A.
   * 
   * @param codePerson1 first person code
   * @param codePerson2 second person code
   * @throws NoSuchCodeException in case either code does not exist
   */
  public void addFriendship(String codePerson1, String codePerson2)
      throws NoSuchCodeException {
    Person p1 = personRepository.findById(codePerson1)
                  .orElseThrow(NoSuchCodeException::new);
    Person p2 = personRepository.findById(codePerson2)
                  .orElseThrow(NoSuchCodeException::new);

    p1.addFriend(p2);
    p2.addFriend(p1);

    personRepository.update(p1);
    personRepository.update(p2);
  }

  /**
   * Retrieve the collection of their friends given the code of a person.
   *
   * @param codePerson code of the person
   * @return the list of person codes
   * @throws NoSuchCodeException in case the code does not exist
   */
  public Collection<String> listOfFriends(String codePerson)
      throws NoSuchCodeException {
     Person p = personRepository.findById(codePerson)
                  .orElseThrow(NoSuchCodeException::new);

    Collection<String> friendCodes = new ArrayList<>();
    for (Person friend : p.getList()) {
        friendCodes.add(friend.getCode());
    }
    return friendCodes;

    
  }

  /**
   * Creates a new group with the given name
   * 
   * @param groupName name of the group
   * @throws GroupExistsException if a group with given name does not exist
   */
  public void addGroup(String groupName) throws GroupExistsException {
    if(groupRepository.findById(groupName).isPresent()){
      throw new GroupExistsException();
    }
    Group group=new Group(groupName);
    groupRepository.save(group);
  }

  /**
   * Deletes the group with the given name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if a group with given name does not exist
   */
  public void deleteGroup(String groupName) throws NoSuchCodeException {
    if(!groupRepository.findById(groupName).isPresent()){
      throw new NoSuchCodeException();
    }
    groupRepository.delete(groupRepository.findById(groupName).get());
  }

  /**
   * Modifies the group name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if the original group name does not exist
   * @throws GroupExistsException if the target group name already exist
   */
  public void updateGroupName(String groupName, String newName) throws NoSuchCodeException, GroupExistsException {
    // Check if the current group exists
    Optional<Group> currentGroupOpt = groupRepository.findById(groupName);
    if (!currentGroupOpt.isPresent()) {
        throw new NoSuchCodeException();
    }

    // Check if the new name already exists
    if (groupRepository.findById(newName).isPresent()) {
        throw new GroupExistsException();
    }

    Group currentGroup = currentGroupOpt.get();

    // Remove old group from DB
    groupRepository.delete(currentGroup);

    // Create new group with new name and copy members
    Group newGroup = new Group(newName);
    newGroup.setMembers(new HashSet<>(currentGroup.getList()));

    // Save new group
    groupRepository.save(newGroup);
  }

  /**
   * Retrieves the list of groups.
   * 
   * @return the collection of group names
   */
  public Collection<String> listOfGroups() {
     List<Group> groups = groupRepository.findAll();
    return groups.stream()
                 .map(Group::getName)
                 .collect(Collectors.toList());
  }

  /**
   * Add a person to a group
   * 
   * @param codePerson person code
   * @param groupName  name of the group
   * @throws NoSuchCodeException in case the code or group name do not exist
   */
  @Transactional
  public void addPersonToGroup(String codePerson, String groupName) throws NoSuchCodeException {
     Optional<Person> optPerson = personRepository.findById(codePerson);
    Optional<Group> optGroup = groupRepository.findById(groupName);

    if (optPerson.isEmpty() || optGroup.isEmpty()) {
        throw new NoSuchCodeException();
    }

    Person person = optPerson.get();
    Group group = optGroup.get();

    // Update both sides
    person.addGroup(group);
    group.addMember(person);

    // Persist the changes
    personRepository.update(person);
    groupRepository.update(group);
  }

  /**
   * Retrieves the list of people on a group
   * 
   * @param groupName name of the group
   * @return collection of person codes
   */
  @Transactional
  public Collection<String> listOfPeopleInGroup(String groupName) {
    Collection<String> codes=new ArrayList<>();
    if(!groupRepository.findById(groupName).isPresent()){
      return null;
    }
    else{
      for(Person p : groupRepository.findById(groupName).get().getList() ){
        codes.add(p.getCode());
      }
    }
    return codes;
  }

  /**
   * Retrieves the code of the person having the largest
   * group of friends
   * 
   * @return the code of the person
   */
  public String personWithLargestNumberOfFriends() {
    return personRepository.findAll().stream()
        .max((p1, p2) -> Integer.compare(p1.getList().size(), p2.getList().size()))
        .map(Person::getCode).orElse(null);
        
  }

  /**
   * Find the name of group with the largest number of members
   * 
   * @return the name of the group
   */
  public String largestGroup() {
    return groupRepository.findAll().stream()
        .max((p1, p2) -> Integer.compare(p1.getList().size(), p2.getList().size()))
        .map(Group::getName).orElse(null);
  }

  /**
   * Find the code of the person that is member of
   * the largest number of groups
   * 
   * @return the code of the person
   */
  public String personInLargestNumberOfGroups() {
   
    return personRepository.findAll().stream()
        .max(Comparator.comparingInt(p -> p.groups().size()))
        .map(Person::getCode)
        .orElse(null); // or throw exception if no people

  }

  // R5

  /**
   * add a new post by a given account
   * 
   * @param authorCode the id of the post author
   * @param text   the content of the post
   * @return a unique id of the post
   */
  public String post(String authorCode, String text) {
    Person author = personRepository.findById(authorCode).get();
        
    Post post = new Post(author, text);
    author.addPost(post);

    postRepository.save(post);
    personRepository.update(author); // Only necessary if posts are not cascaded

    return post.getId();
  }

  /**
   * retrieves the content of the given post
   * 
   * @param pid    the id of the post
   * @return the content of the post
   */
  public String getPostContent(String pid) {
   Post e=postRepository.findById(pid).get();
   return e.getText();
  }

  /**
   * retrieves the timestamp of the given post
   * 
   * @param pid    the id of the post
   * @return the timestamp of the post
   */
  public long getTimestamp(String pid) {
     Post f=postRepository.findById(pid).get();
     return f.getTimestamp();
  }

  /**
   * returns the list of post of a given author paginated
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts id
   */
  public List<String> getPaginatedUserPosts(String author, int pageNo, int pageLength) {
     Person person = personRepository.findById(author).get();
    return person.getPosts().stream()
        .sorted(Comparator.comparingLong(Post::getTimestamp).reversed())
        .skip((long)(pageNo - 1) * pageLength)
        .limit(pageLength)
        .map(Post::getId)
        .collect(Collectors.toList());
  }

  /**
   * returns the paginated list of post of friends.
   * The returned list contains the author and the id of a post separated by ":"
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts key elements
   */
  public List<String> getPaginatedFriendPosts(String author, int pageNo, int pageLength) {
    Person person = personRepository.findById(author).get();
    return person.getList().stream()
        .flatMap(friend -> friend.getPosts().stream())
        .sorted(Comparator.comparingLong(Post::getTimestamp).reversed())
        .skip((long)(pageNo - 1) * pageLength)
        .limit(pageLength)
        .map(p -> p.getAuthor().getCode() + ":" + p.getId())
        .collect(Collectors.toList());
  }

}