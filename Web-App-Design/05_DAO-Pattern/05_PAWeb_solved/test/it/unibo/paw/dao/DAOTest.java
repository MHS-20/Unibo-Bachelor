package it.unibo.paw.dao;


import java.util.Calendar;
import java.util.List;

public class DAOTest {
	
	public static final int DAO = DAOFactory.DB2;
	
	public static void main(String[] args) {
		
		// Student
		
		DAOFactory daoFactoryInstance = DAOFactory.getDAOFactory(DAO);
		
		StudentDAO studentDAO = daoFactoryInstance.getStudentDAO();
		studentDAO.dropTable();
		studentDAO.createTable();
		
		StudentDTO s = new StudentDTO();
		Calendar c = Calendar.getInstance();
		c.set(1984, 1, 24);
		s.setId(1);
		s.setFirstName("Luisa");
		s.setLastName("Verdi");
		s.setBirthDate(c.getTime());
		studentDAO.create(s);

		s = new StudentDTO();
		c.set(1985, 4, 2);
		s.setId(2);
		s.setFirstName("Anna");
		s.setLastName("Bruni");
		java.util.Date d = c.getTime(); 
		s.setBirthDate(d);
		studentDAO.create(s);
		
		// Courses
		
		CourseDAO courseDAO = daoFactoryInstance.getCourseDAO();
		courseDAO.dropTable();
		courseDAO.createTable();
		
		CourseDTO course = new CourseDTO();
		course.setId(1);
		course.setName("Progettazione di Applicazioni Web");
		courseDAO.create(course);

		course = new CourseDTO();
		course.setId(2);
		course.setName("Fondamenti di Informatica T1");
		courseDAO.create(course);
				
		
		// StudentCoursesMapping
		
		CourseStudentMappingDAO mappingDAO = daoFactoryInstance.getStudentCourseMappingDAO();
		mappingDAO.dropTable();
		mappingDAO.createTable();

		
		mappingDAO.create(1, 1);
		
		mappingDAO.create(1, 2);
		
		mappingDAO.create(2, 2);
		
		
		StudentDTO student = studentDAO.read(1);
		System.out.println(student.getFirstName()+" "+student.getLastName()+" frequenta i seguenti corsi:");
		List<CourseDTO> corsiDiStudent = student.getCourses();
		for(CourseDTO cds : corsiDiStudent)
		{
			System.out.println(""+cds.getId()+" "+cds.getName());
		}
		System.out.println();
		
		CourseDTO paw = courseDAO.read(1);
		System.out.println("Il corso "+paw.getName()+" è frequentato da:");
		List<StudentDTO> sdc = paw.getStudents();
		for(StudentDTO stu : sdc){
			
			System.out.println(""+stu.getFirstName()+" "+stu.getLastName()+" "+stu.getBirthDate());;
		}
		System.out.println();
		
		/*
		//TEST ON CASCADE
		//si può vedere che se vengono aggiunti i corsi attraverso java a uno studente e lo rendiamo persistente, vengono salvate anche le associazioni coi corsi
		System.out.println("TEST ON CASCADE");
		
		System.out.println("\nTest on student");
		student= new StudentDTO();
		student.setId(3);
		student.setFirstName("Mario");
		student.setLastName("Rossi");
		d = c.getTime(); 
		student.setBirthDate(d);
		
		course = courseDAO.read(1);
		student.addCourse(course);
		
		course = courseDAO.read(2);
		student.addCourse(course);
		
		studentDAO.create(student);
		
		student = studentDAO.read(3);
		System.out.println(student.getFirstName()+" "+student.getLastName()+" frequenta i seguenti corsi:");
		corsiDiStudent = student.getCourses();
		for(CourseDTO cds : corsiDiStudent)
		{
			System.out.println(""+cds.getId()+" "+cds.getName());
		}
		System.out.println();
		
		System.out.println("\nTest on course:");
		course = new CourseDTO();
		course.setId(3);
		course.setName("Amministrazione di Sistemi");
		
		course.addStudent(studentDAO.read(1));
		course.addStudent(studentDAO.read(2));
		
		courseDAO.create(course);
		
		course = courseDAO.read(3);
		System.out.println("Il corso "+course.getName()+" è frequentato da:");
		sdc = course.getStudents();
		for(StudentDTO stu : sdc){
			
			System.out.println(""+stu.getFirstName()+" "+stu.getLastName()+" "+stu.getBirthDate());;
		}
		System.out.println();*/
		
		System.out.println("Test di eliminazione tupla:");
		StudentDTO toDelete = studentDAO.read(1);
		if(studentDAO.delete(toDelete.getId()))
			System.out.println("eliminazione eseguita con successo");
		
		//FINE TEST
	}

}
