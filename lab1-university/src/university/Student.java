package university;

import java.util.ArrayList;
import java.util.List;

public class Student {
       private String fName, lName;
       private int id;
       public List<String> courseRegistred = new ArrayList<>();
       public List<String> exams = new ArrayList<>();
       private int point;
       

       public Student(int id, String firstName, String lastName) {
		this.id = id;
		this.fName = firstName;
		this.lName = lastName;
	}


       public String getInfo(){
              return id + " " + fName + " " + lName;
       }
       public String getFullName() {
              StringBuilder fullName = new StringBuilder();
              if (fName != null && !fName.isEmpty()) {
                  fullName.append(fName);
              }
              if (lName != null && !lName.isEmpty()) {
                  if (fullName.length() > 0) {
                      fullName.append(" ");
                  }
                  fullName.append(lName);
              }
              return fullName.toString();
          }
       
       public int getId(){
              return id;
       }
       public void setPoint(int point){
              this.point = point;
       }
       public float getPoint(){
              return (float)point;
       }

}
