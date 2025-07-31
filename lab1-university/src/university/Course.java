package university;

import java.util.ArrayList;
import java.util.List;

public class Course {

       private String title;
       private String teacher;
       private int id;
       public List<String> studentEnrolled = new ArrayList<>();
       public List<String> studentsGrade = new ArrayList<>();
       public int currentEnrolled; 

       public Course(String title,String teacher,int id){
              this.title = title;
              this.teacher = teacher;
              this.id = id;
       }

       public String getInfo(){
              return title + " " + teacher + id;
       }
       public  String getTitle(){
              return title;
       }
       public  String getTeacher(){
              return teacher;
       }
       public int getCurrentEnrolled() {
              return currentEnrolled;
       }
       public int getCode() {
              return id;
       }
}
