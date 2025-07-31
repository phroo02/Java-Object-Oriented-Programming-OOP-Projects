package university;

public class BestStudents {
       private int point;
       private String name;
       private int studentId;
 

       public BestStudents(String name, int studentId, int point) {
        this.name = name;
        this.studentId = studentId;
        this.point = point;
    }

       public String getInfo(){
              return name + " " + point;
       }
       public int getPoint(){
              return point;
       }
}
