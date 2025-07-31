package university;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */
public class University {
// R1
	/**
	 * Constructor
	 * @param name name of the university
	 */
	private String name;
	private String rector;
	private List<Student> students = new ArrayList<>();
	private List<Course> courses = new ArrayList<>();
	private int studentIdCounter = 0;
	private int courseCodeCounter = 0;
	

	public University(String name) {
		this.name = name;
	   }
      
	   public String getName() {
		return name;
	   }
      
	   public void setRector(String firstName, String lastName) {
		this.rector = firstName + " " + lastName;
	   }
      
	   public String getRector() {
		return rector;
	   }
// R2
	   public int enroll(String firstName, String lastName) {
		int id = 10000 + studentIdCounter;
		Student student = new Student(id, firstName, lastName);
		students.add(student);
		studentIdCounter++;
		logger.info("New student enrolled: " + id + ", " + firstName + " " + lastName);
		return id;
	   }
      
	   public String student(int id) {
		for (Student s : students) {
		    if (s.getId() == id) {
			 return s.getInfo();
		    }
		}
		return null;
	   }
// R3
	   public int activate(String courseTitle, String teacherName) {
		int code = 10 + courseCodeCounter;
		Course course = new Course(courseTitle, teacherName, code);
		courses.add(course);
		
		courseCodeCounter++;
		logger.info("New course activated: " + code + ", " + courseTitle + " " + teacherName);
		return code;
	   }
      
	   public String course(int code) {
		for (Course c : courses) {
		    if (c.getCode() == code) {
			 return c.getCode() + "," + c.getTitle() + "," + c.getTeacher();
		    }
		}
		return null;
	   }
	
// R4
	/**
	 * Register a student to attend a course
	 * @param studentID id of the student
	 * @param courseCode id of the course
	 */
	public void register(int studentID, int courseCode){
		int sIndex = studentID - 10000;
		int cIndex = courseCode - 10;
		if (sIndex < 0 || sIndex >= students.size()) {
			System.err.println("Error: Invalid student ID.");
			return;
		}
		if (cIndex < 0 || cIndex >= courses.size()) {
			System.err.println("Error: Invalid course code.");
			return;
		}
		Student s = students.get(sIndex);
		Course c = courses.get(cIndex);
		if (s.courseRegistred.size()<25){
			s.courseRegistred.add(c.getInfo());
		}
		else {
			System.err.println("error, more than 25 course for a student is not allowed");
		}
		if (c.studentEnrolled.size()<100){
			c.studentEnrolled.add(s.getInfo());
		}
		else {
			System.err.println("error, more than 100 student for a course is not allowed");
		}
		logger.info("Student " + studentID + " signed up for course " + courseCode);
		
	}
	
	/**
	 * Retrieve a list of attendees.
	 * 
	 * The students appear one per row (rows end with `'\n'`) 
	 * and each row is formatted as describe in in method {@link #student}
	 * 
	 * @param courseCode unique id of the course
	 * @return list of attendees separated by "\n"
	 */
	public String listAttendees(int courseCode){
		Course selectedCourse = null;
		for (Course course : courses) {
			if (course.getCode() == courseCode) {
				selectedCourse = course;
				break;
			}
		}
		if (selectedCourse == null) {
			return "Course not found.";
		}

		if (selectedCourse.studentEnrolled == null) {
			return "No students enrolled in this course.";
		   }
	      
		   StringBuilder result = new StringBuilder();
		   for (String enrolledStudent : selectedCourse.studentEnrolled) {
			result.append(enrolledStudent).append("\n");
		   }
	      
		   if (result.length() == 0) {
			return "No students enrolled in this course.";
		   }
		return result.toString();
	     
	}

	/**
	 * Retrieves the study plan for a student.
	 * 
	 * The study plan is reported as a string having
	 * one course per line (i.e. separated by '\n').
	 * The courses are formatted as describe in method {@link #course}
	 * 
	 * @param studentID id of the student
	 * 
	 * @return the list of courses the student is registered for
	 */
	public String studyPlan(int studentID){
		Student selectedStudent = null;
		for (Student student : students) {
			if (student.getId() == studentID) {
				selectedStudent = student;
				break;
			}
		}
		if (selectedStudent == null) {
			return "Student not found.";
		}

		StringBuilder result = new StringBuilder();
		
		for (String course : selectedStudent.courseRegistred) {
			result.append(course).append("\n");
		}
		return result.toString();
	}

// R5
	/**
	 * records the grade (integer 0-30) for an exam can 
	 * 
	 * @param studentId the ID of the student
	 * @param courseID	course code 
	 * @param grade		grade ( 0-30)
	 */
	public void exam(int studentId, int courseID, int grade) {
		Student selectedStudent = null;
		for (Student student : students) {
			if (student.getId() == studentId) {
				selectedStudent = student;
				break;
			}
		}
		StringBuilder result = new StringBuilder();
		result.append(courseID).append(" ").append(grade);
		selectedStudent.exams.add(result.toString());

		Course selectedCourse = null;
		for (Course course : courses) {
			if (course.getCode() == courseID) {
				selectedCourse = course;
				break;
			}
		}
		StringBuilder resultc = new StringBuilder();
		resultc.append(studentId).append(" ").append(grade);
		selectedCourse.studentsGrade.add(result.toString());
		logger.info("Student " + studentId + " took an exam in course " + courseID + " with grade " + grade);
	}

	/**
	 * Computes the average grade for a student and formats it as a string
	 * using the following format 
	 * 
	 * {@code "Student STUDENT_ID : AVG_GRADE"}. 
	 * 
	 * If the student has no exam recorded the method
	 * returns {@code "Student STUDENT_ID hasn't taken any exams"}.
	 * 
	 * @param studentId the ID of the student
	 * @return the average grade formatted as a string.
	 */
	public String studentAvg(int studentId) {
		Student selectedStudent = null;
		for (Student student : students) {
			if (student.getId() == studentId) {
				selectedStudent = student;
				break;
			}
		}
		if (selectedStudent.exams.size()<1){
			return "Student " + selectedStudent.getId() + " hasn't taken any exams";
		}
		int sum = 0;
		int countexams = 0;
		for (String exam : selectedStudent.exams){
			String [] parts = exam.split(" ");
			int score = Integer.parseInt(parts[1]);
			sum = sum + score;
			countexams++;
		}
		float avg = sum/countexams;
		return "Student" + " " + selectedStudent.getId() + " : " + avg;
	}
	
	/**
	 * Computes the average grades of all students that took the exam for a given course.
	 * 
	 * The format is the following: 
	 * {@code "The average for the course COURSE_TITLE is: COURSE_AVG"}.
	 * 
	 * If no student took the exam for that course it returns {@code "No student has taken the exam in COURSE_TITLE"}.
	 * 
	 * @param courseId	course code 
	 * @return the course average formatted as a string
	 */
	public String courseAvg(int courseId) {
		Course selectedCourse = null;
		for (Course course : courses) {
			if (course.getCode() == courseId) {
				selectedCourse = course;
				break;
			}
		}
		if (selectedCourse == null) {
			return "Course not found.";
		}
		if (selectedCourse.studentsGrade.size()<1){
			return "No student has taken the exam in" + selectedCourse.getTitle();
		}
		int sum = 0;
		int countexams = 0;
		for (String studentgrade : selectedCourse.studentsGrade){
			String [] parts = studentgrade.split(" ");
			int score = Integer.parseInt(parts[1]);
			sum = sum + score;
			countexams++;
		}
		float avg = sum/countexams;
		return "The average for the course" + selectedCourse.getTitle() + "is : " + avg;


		
	}
	

// R6
	/**
	 * Retrieve information for the best students to award a price.
	 * 
	 * The students' score is evaluated as the average grade of the exams they've taken. 
	 * To take into account the number of exams taken and not only the grades, 
	 * a special bonus is assigned on top of the average grade: 
	 * the number of taken exams divided by the number of courses the student is enrolled to, multiplied by 10.
	 * The bonus is added to the exam average to compute the student score.
	 * 
	 * The method returns a string with the information about the three students with the highest score. 
	 * The students appear one per row (rows are terminated by a new-line character {@code '\n'}) 
	 * and each one of them is formatted as: {@code "STUDENT_FIRSTNAME STUDENT_LASTNAME : SCORE"}.
	 * 
	 * @return info on the best three students.
	 */
	
	public String topThreeStudents() {
		// for (Student student : students) {
		// 	String show = studentAvg(student.getId());
		// 	String [] parts = show.split(" ");
		// 	int avg = Integer.parseInt(parts[2]);
		// 	int point = avg + student.exams.size() + 10;
		// 	student.setPoint(point);
		// }
		// students.sort(Comparator.comparing(Student::getPoint));
		// StringBuilder result = new StringBuilder();
		// for(int i=0 ; i<3; i++){
		// 	result.append(students.get(i).getFullName()).append(" : ").append(students.get(i).getPoint());
		// }
		List<Student> studentPoints = new ArrayList<>();
		for (Student student : students) {
			int sum = 0;
			int examCount = student.exams.size();
			
			// Calculate the sum of exam scores
			for (String exam : student.exams) {
			    String[] parts = exam.split(" ");
			    int score = Integer.parseInt(parts[1]);
			    sum += score;
			}
			// Calculate average; handle division by zero
			int avg = (examCount > 0) ? sum / examCount : 0;
			int bounos = (examCount/student.courseRegistred.size()) * 10;
			// Calculate total points
			int point = avg + bounos;
	      
			// Set the calculated point to the student
			student.setPoint(point);
			// Add student to the list
			if (examCount > 0){
				studentPoints.add(student);
			}
		}
	   
		// Sort students by points in descending order
		studentPoints.sort(Comparator.comparingDouble(Student::getPoint).reversed());
	   
		// Prepare the result string
		if (studentPoints.get(0).getPoint() == studentPoints.get(1).getPoint()){
			return "error" ; 
		}
		if (studentPoints.isEmpty()) {
			return "No students have taken exams.";
		   }

		StringBuilder result = new StringBuilder();
		if(studentPoints.size()>=3){
			int count = 3;
		}
		else{
			int count = studentPoints.size();
		}
		
		for (int i = 0; i < 3 && i<studentPoints.size(); i++) {
		    Student topStudent = studentPoints.get(i);
		    result.append(topStudent.getFullName()).append(" : ").append(topStudent.getPoint()).append("\n");
		}
	   
		return result.toString();
	//    }
	      
		// return result.toString();
	}

// R7
    /**
     * This field points to the logger for the class that can be used
     * throughout the methods to log the activities.
     */
    public static final Logger logger = Logger.getLogger("University");

}
