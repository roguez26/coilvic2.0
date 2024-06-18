package unit.test.Initializer;

import log.Log;
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.CollaborativeProjectRequest;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.CollaborativeProjectRequestDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.course.Course;
import mx.fei.coilvicapp.logic.course.CourseDAO;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategory;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategoryDAO;
import mx.fei.coilvicapp.logic.hiringtype.HiringType;
import mx.fei.coilvicapp.logic.hiringtype.HiringTypeDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.language.Language;
import mx.fei.coilvicapp.logic.language.LanguageDAO;
import mx.fei.coilvicapp.logic.modality.Modality;
import mx.fei.coilvicapp.logic.modality.ModalityDAO;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorUV;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.student.StudentUV;
import mx.fei.coilvicapp.logic.student.StudentDAO;
import mx.fei.coilvicapp.logic.term.Term;
import mx.fei.coilvicapp.logic.term.TermDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.user.User;

public class TestHelper {

    private Language language;
    private Term term;
    private Modality modality;
    private Country countryOne;
    private Country countryTwo;
    private University universityOne;
    private University universityTwo;
    private Professor professorOne;
    private Professor professorTwo;
    private ProfessorUV professorUVOne;
    private ProfessorUV professorUVTwo;
    private Course courseOne;
    private Course courseTwo;
    private CollaborativeProjectRequest collaborativeProjectRequest;
    private CollaborativeProject collaborativeProject;
    private Student studentOne;
    private Student studentTwo;
    private StudentUV studentUVOne;
    private StudentUV studentUVTwo;
    private AcademicArea academicArea;
    private Region region;
    private HiringType hiringType;
    private HiringCategory hiringCategory;

    public TestHelper() {
        language = new Language();
        term = new Term();
        modality = new Modality();
        countryOne = new Country();
        countryTwo = new Country();
        universityOne = new University();
        universityTwo = new University();
        professorOne = new Professor();
        professorTwo = new Professor();
        professorUVOne = new ProfessorUV();
        professorUVTwo = new ProfessorUV();
        courseOne = new Course();
        courseTwo = new Course();
        collaborativeProjectRequest = new CollaborativeProjectRequest();
        collaborativeProject = new CollaborativeProject();
        studentUVOne = new StudentUV();
        studentUVTwo = new StudentUV();
        studentOne = new Student();
        studentTwo = new Student();
        academicArea = new AcademicArea();
        region = new Region();
        hiringType = new HiringType();
        hiringCategory = new HiringCategory();
    }
    
    public void initializeAcademicArea() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        academicArea.setName("Economico Administrativo");
        
        try {
            int idAcademicArea = academicAreaDAO.registerAcademicArea(academicArea);
            academicArea.setIdAreaAcademica(idAcademicArea);
        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }
    }
    
    public void initializeRegion() {
        RegionDAO regionDAO = new RegionDAO();
        region.setName("Xalapa");
        
        try {
            int idRegion = regionDAO.registerRegion(region);
            region.setIdRegion(idRegion);
        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }        
    }  
    
    public void initializeHiringType() {
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        hiringType.setName("Interino por Plaza");
        
        try {
            int idHiringType = hiringTypeDAO.registerHiringType(hiringType);
            hiringType.setIdHiringType(idHiringType);
        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }        
    } 

    public void initializeHiringCategory() {
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        hiringCategory = new HiringCategory();  
        hiringCategory.setName("Docente TC");

        try {
            int idHiringCategory = hiringCategoryDAO.registerHiringCategory(hiringCategory);
            hiringCategory.setIdHiringCategory(idHiringCategory);
        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }        
    }    

    public void initializeLanguage() {
        LanguageDAO languageDAO = new LanguageDAO();
        language.setName("Inglés");

        try {
            language.setIdLanguage(languageDAO.registerLanguage(language));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }

    public void initializeTerm() {
        TermDAO termDAO = new TermDAO();
        term.setName("Febrero2024-Junio2024");

        try {
            term.setIdTerm(termDAO.registerTerm(term));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }

    public void initializeModality() {
        ModalityDAO modalityDAO = new ModalityDAO();
        modality.setName("Clase espejo");

        try {
            modality.setIdModality(modalityDAO.registerModality(modality));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }

    public void initializeCountries() {
        CountryDAO countryDAO = new CountryDAO();
        countryOne = new Country();
        countryTwo = new Country();
        countryOne.setName("México");
        countryOne.setCountryCode("+52");
        countryTwo.setName("Venezuela");
        countryTwo.setCountryCode("+58");

        try {
            countryOne.setIdCountry(countryDAO.registerCountry(countryOne));
            countryTwo.setIdCountry(countryDAO.registerCountry(countryTwo));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }

    public void initializeUniversities() {
        initializeCountries();
        UniversityDAO universityDAO = new UniversityDAO();
        universityOne.setName("Universidad Veracruzana");
        universityOne.setAcronym("UV");
        universityOne.setJurisdiction("Veracruz");
        universityOne.setCity("Xalapa");
        universityOne.setCountry(countryOne);

        universityTwo.setName("Universidad Católica Andrés Bello");
        universityTwo.setAcronym("UCAB");
        universityTwo.setJurisdiction("Caracas");
        universityTwo.setCity("Guayana");
        universityTwo.setCountry(countryTwo);
        try {
            universityOne.setIdUniversity(universityDAO.registerUniversity(universityOne));
            universityTwo.setIdUniversity(universityDAO.registerUniversity(universityTwo));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }

    public void initializeProfessors() {
        initializeUniversities();
        ProfessorDAO professorDAO = new ProfessorDAO();
        User user = new User();
        
        user.setIdUser(0);
        user.setPassword(null);
        user.setType(null);        
        professorOne.setName("Maria");
        professorOne.setPaternalSurname("Arenas");
        professorOne.setMaternalSurname("Valdes");
        professorOne.setEmail("axlvaldez74@gmail.com");
        professorOne.setGender("Mujer");
        professorOne.setState("Pendiente");
        professorOne.setPhoneNumber("1234567890");
        professorOne.setUniversity(universityTwo);
        professorOne.setUser(user);

        professorTwo.setName("Jorge");
        professorTwo.setPaternalSurname("Gonzalez");
        professorTwo.setMaternalSurname("Hernandez");
        professorTwo.setEmail("joGo@gmail.com");
        professorTwo.setGender("Hombre");
        professorTwo.setState("Pendiente");
        professorTwo.setPhoneNumber("1234567890");
        professorTwo.setUniversity(universityTwo);
        professorTwo.setUser(user);
        try {
            professorOne.setIdProfessor(professorDAO.registerProfessor(professorOne));
            professorTwo.setIdProfessor(professorDAO.registerProfessor(professorTwo));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }
    
    public void initializeProfessorsUV() {
        initializeUniversities();
        initializeHiringCategory();
        initializeHiringType();
        initializeRegion();
        initializeAcademicArea();        
        ProfessorDAO professorDAO = new ProfessorDAO();
        User user = new User();
        
        user.setIdUser(0);
        user.setPassword(null);
        user.setType(null);        
        professorUVOne.setName("Maria");
        professorUVOne.setPaternalSurname("Arenas");
        professorUVOne.setMaternalSurname("Valdes");
        professorUVOne.setEmail("axlvaldez74@gmail.com");
        professorUVOne.setGender("Mujer");
        professorUVOne.setState("Pendiente");
        professorUVOne.setPhoneNumber("1234567890");
        professorUVOne.setUniversity(universityOne);
        professorUVOne.setUser(user);
        professorUVOne.setAcademicArea(academicArea);
        professorUVOne.setHiringCategory(hiringCategory);
        professorUVOne.setHiringType(hiringType);
        professorUVOne.setRegion(region);
        professorUVOne.setPersonalNumber(12345);

        professorUVTwo.setName("Jorge");
        professorUVTwo.setPaternalSurname("Gonzalez");
        professorUVTwo.setMaternalSurname("Hernandez");
        professorUVTwo.setEmail("joGo@gmail.com");
        professorUVTwo.setGender("Hombre");
        professorUVTwo.setState("Pendiente");
        professorUVTwo.setPhoneNumber("1234567890");
        professorUVTwo.setUniversity(universityOne);
        professorUVTwo.setUser(user);
        professorUVTwo.setAcademicArea(academicArea);
        professorUVTwo.setHiringCategory(hiringCategory);
        professorUVTwo.setHiringType(hiringType);
        professorUVTwo.setRegion(region);
        professorUVTwo.setPersonalNumber(54321);        
        try {
            professorUVOne.setIdProfessor(professorDAO.registerProfessorUV(professorUVOne));
            professorUVTwo.setIdProfessor(professorDAO.registerProfessorUV(professorUVTwo));

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
    }    

    public void intializeCourses() {
        initializeProfessors();
        initializeTerm();
        initializeLanguage();
        CourseDAO courseDAO = new CourseDAO();
        courseOne.setName("Programacion");
        courseOne.setGeneralObjective("Los alumnos comprendan la"
                + " programacion estructurada y orientada a objetos");
        courseOne.setTopicsInterest("Abstraccion, Herencia, Polimorfismo");
        courseOne.setNumberStudents(35);
        courseOne.setStudentsProfile("Ingenieria de software");
        courseOne.setTerm(term);
        courseOne.setLanguage(language);
        courseOne.setAdditionalInformation("Ademas de la programacion"
                + " orientada a objetos veremos el paradigma funcional");
        courseOne.setProfessor(professorOne);

        courseTwo.setName("Bases de Datos");
        courseTwo.setGeneralObjective("Los alumnos comprendan el diseño, "
                + "implementación y administración de bases de datos relacionales.");
        courseTwo.setTopicsInterest("Modelado de datos, SQL, Normalización");
        courseTwo.setNumberStudents(40);
        courseTwo.setStudentsProfile("Ingeniería en Sistemas Computacionales");
        courseTwo.setTerm(term);
        courseTwo.setLanguage(language);
        courseTwo.setAdditionalInformation("Además de bases de datos relacionales, "
                + "se introducirá a bases de datos NoSQL y técnicas avanzadas de optimización.");
        courseTwo.setProfessor(professorTwo);
        try {
            courseTwo.setIdCourse(courseDAO.registerCourse(courseTwo));
            courseOne.setIdCourse(courseDAO.registerCourse(courseOne));
            courseDAO.evaluateCourseProposal(courseTwo, "Aceptado");
            courseDAO.evaluateCourseProposal(courseOne, "Aceptado");

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
        courseTwo.setStatus("Aceptado");
        courseOne.setStatus("Aceptado");
    }

    public void initializeCollaborativeProjectRequest() {
        intializeCourses();
        CollaborativeProjectRequestDAO collaborativeProjectRequestDAO = new CollaborativeProjectRequestDAO();
        collaborativeProjectRequest.setRequestedCourse(courseOne);
        collaborativeProjectRequest.setRequesterCourse(courseTwo);

        try {
            collaborativeProjectRequest.setIdCollaborativeProjectRequest(
                    collaborativeProjectRequestDAO.registerCollaborativeProjectRequest(
                            collaborativeProjectRequest));
            collaborativeProjectRequestDAO.attendCollaborativeProjectRequest(collaborativeProjectRequest, "Aceptado");

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
        collaborativeProjectRequest.setStatus("Aceptado");
    }
    
    public void initializeStudent() {
        StudentDAO studentDAO = new StudentDAO();
        initializeUniversities();
        studentOne.setName("Edgar");
        studentOne.setPaternalSurname("Montiel");
        studentOne.setMaternalSurname("Acosta");
        studentOne.setEmail("dd@gmail.com");
        studentOne.setGender("Masculino");
        studentOne.setLineage("Mexicano");
        studentOne.setUniversity(universityTwo);
        
        studentTwo.setName("Ivan");
        studentTwo.setPaternalSurname("Rodriguez");
        studentTwo.setMaternalSurname("Franco");
        studentTwo.setEmail("roguez@gmail.com");
        studentTwo.setGender("Masculino");
        studentTwo.setLineage("Mexicano");
        studentTwo.setUniversity(universityTwo);
        
        try {
            studentOne.setIdStudent(studentDAO.registerStudent(studentOne));
            studentTwo.setIdStudent(studentDAO.registerStudent(studentTwo));
        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }
    }
    
    public void initializeStudentUV() {
        StudentDAO studentDAO = new StudentDAO();
        initializeUniversities();
        initializeRegion();
        initializeAcademicArea();
        studentUVOne.setName("Edgar");
        studentUVOne.setPaternalSurname("Montiel");
        studentUVOne.setMaternalSurname("Acosta");
        studentUVOne.setEmail("dd@gmail.com");
        studentUVOne.setGender("Masculino");
        studentUVOne.setLineage("Mexicano");
        studentUVOne.setEnrollment("S22013628");
        studentUVOne.setUniversity(universityOne);
        studentUVOne.setAcademicArea(academicArea);
        studentUVOne.setRegion(region);
        
        studentUVTwo.setName("Ivan");
        studentUVTwo.setPaternalSurname("Rodriguez");
        studentUVTwo.setMaternalSurname("Franco");
        studentUVTwo.setEmail("roguez@gmail.com");
        studentUVTwo.setGender("Masculino");
        studentUVTwo.setLineage("Mexicano");
        studentUVTwo.setEnrollment("S22013629");
        studentUVTwo.setUniversity(universityOne);
        studentUVTwo.setAcademicArea(academicArea);
        studentUVTwo.setRegion(region);        
        
        try {
            studentUVOne.setIdStudent(studentDAO.registerStudentUV(studentUVOne));
            studentUVTwo.setIdStudent(studentDAO.registerStudentUV(studentUVTwo));
        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }
    }    

    public void initializeCollaborativeProject() {
        initializeCollaborativeProjectRequest();
        initializeModality();
        initializeStudent();
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        collaborativeProject.setName("Programación y Bases de Datos");
        collaborativeProject.setStatus("Aceptado");
        collaborativeProject.setDescription("Este proyecto combina los conocimientos de programación orientada a objetos y bases de datos "
                + "para desarrollar una aplicación completa que gestione información de manera eficiente.");
        collaborativeProject.setGeneralObjective("Integrar conceptos de programación y bases de datos para desarrollar una solución software completa.");
        collaborativeProject.setModality(modality);
        collaborativeProject.setCode("ProgBasdat2024!");
        collaborativeProject.setSyllabusPath("/syllabus/proyecto_integrador.pdf");
        collaborativeProject.setRequestedCourse(courseOne);
        collaborativeProject.setRequesterCourse(courseTwo);
        try {
            collaborativeProject.setIdCollaborativeProject(collaborativeProjectDAO.registerCollaborativeProject(collaborativeProject, collaborativeProjectRequest));
            collaborativeProjectDAO.evaluateCollaborativeProjectProposal(collaborativeProject, "Aceptado");

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class
            ).error(exception.getMessage(), exception);
        }
        collaborativeProject.setStatus("Aceptado");
    }

    public void deleteAll() {
        try {
            new CollaborativeProjectDAO().deleteCollaborativeProjectByidCollaborativeProject(collaborativeProject.getIdCollaborativeProject());
            new CollaborativeProjectRequestDAO().deleteCollaborativeProjectRequestByidCollaborativeProjectRequest(collaborativeProjectRequest.getIdCollaborativeProjectRequest());
            new ModalityDAO().deleteModality(modality.getIdModality());
            new CourseDAO().deleteCourseByIdCourse(courseOne.getIdCourse());
            new CourseDAO().deleteCourseByIdCourse(courseTwo.getIdCourse());
            new LanguageDAO().deleteLanguage(language.getIdLanguage());
            new TermDAO().deleteTerm(term.getIdTerm());
            new StudentDAO().deleteStudentUVById(studentUVOne.getIdStudent());
            new StudentDAO().deleteStudentUVById(studentUVTwo.getIdStudent());
            new StudentDAO().deleteStudentById(studentOne.getIdStudent());
            new StudentDAO().deleteStudentById(studentTwo.getIdStudent());
            new ProfessorDAO().deleteProfessorUVByID(professorUVOne.getIdProfessor());
            new ProfessorDAO().deleteProfessorUVByID(professorUVTwo.getIdProfessor());
            new ProfessorDAO().deleteProfessorByID(professorUVOne.getIdProfessor());
            new ProfessorDAO().deleteProfessorByID(professorUVTwo.getIdProfessor());
            new ProfessorDAO().deleteProfessorByID(professorOne.getIdProfessor());
            new ProfessorDAO().deleteProfessorByID(professorTwo.getIdProfessor());
            new UniversityDAO().deleteUniversity(universityOne.getIdUniversity());
            new UniversityDAO().deleteUniversity(universityTwo.getIdUniversity());
            new CountryDAO().deleteCountry(countryOne.getIdCountry());
            new CountryDAO().deleteCountry(countryTwo.getIdCountry());
            new RegionDAO().deleteRegion(region.getIdRegion());
            new AcademicAreaDAO().deleteAcademicArea(academicArea.getIdAreaAcademica());
            new HiringTypeDAO().deleteHiringType(hiringType.getIdHiringType());
            new HiringCategoryDAO().deleteHiringCategory(hiringCategory.getIdHiringCategory());

        } catch (DAOException exception) {
            Log.getLogger(TestHelper.class).error(exception.getMessage(), exception);
        }
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public Country getCountryOne() {
        return countryOne;
    }

    public void setCountryOne(Country countryOne) {
        this.countryOne = countryOne;
    }

    public Country getCountryTwo() {
        return countryTwo;
    }

    public void setCountryTwo(Country countryTwo) {
        this.countryTwo = countryTwo;
    }

    public University getUniversityOne() {
        return universityOne;
    }

    public void setUniversityOne(University universityOne) {
        this.universityOne = universityOne;
    }

    public University getUniversityTwo() {
        return universityTwo;
    }

    public void setUniversityTwo(University universityTwo) {
        this.universityTwo = universityTwo;
    }

    public Professor getProfessorOne() {
        return professorOne;
    }

    public void setProfessorOne(Professor professorOne) {
        this.professorOne = professorOne;
    }

    public Professor getProfessorTwo() {
        return professorTwo;
    }

    public void setProfessorTwo(Professor professorTwo) {
        this.professorTwo = professorTwo;
    }
    
    public ProfessorUV getProfessorUVOne() {
        return professorUVOne;
    }

    public void setProfessorUVOne(ProfessorUV professorUV) {
        this.professorUVOne = professorUV;
    }

    public ProfessorUV getProfessorUVTwo() {
        return professorUVTwo;
    }

    public void setProfessorUVTwo(ProfessorUV professorUVTwo) {
        this.professorUVTwo = professorUVTwo;
    }    

    public Course getCourseOne() {
        return courseOne;
    }

    public void setCourseOne(Course courseOne) {
        this.courseOne = courseOne;
    }

    public Course getCourseTwo() {
        return courseTwo;
    }

    public void setCourseTwo(Course courseTwo) {
        this.courseTwo = courseTwo;
    }

    public CollaborativeProjectRequest getCollaborativeProjectRequest() {
        return collaborativeProjectRequest;
    }

    public void setCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) {
        this.collaborativeProjectRequest = collaborativeProjectRequest;
    }

    public CollaborativeProject getCollaborativeProject() {
        return collaborativeProject;
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
    }
    
    public void setStudentOne(Student student) {
        this.studentOne = student;
    }
    
    public Student getStudentOne() {
        return studentOne;
    }

    public void setStudentTwo(Student student) {
        this.studentTwo = student;
    }
    
    public Student getStudentTwo() {
        return studentTwo;
    } 
    
    public void setStudentUVOne(StudentUV studentUV) {
        this.studentUVOne = studentUV;
    }
    
    public StudentUV getStudentUVOne() {
        return studentUVOne;
    }

    public void setStudentUVTwo(StudentUV studentUV) {
        this.studentUVTwo = studentUV;
    }
    
    public StudentUV getStudentUVTwo() {
        return studentUVTwo;
    }     
    
    public void setAcademicArea(AcademicArea academicArea) {
        this.academicArea = academicArea;
    }
    
    public AcademicArea getAcademicArea() {
        return academicArea;
    }    
    
    public void setRegion(Region region) {
        this.region = region;
    }
    
    public Region getRegion() {
        return region;
    }    
    
    public HiringType getHiringType() {
        return hiringType;
    }

    public void setHiringType(HiringType hiringType) {
        this.hiringType = hiringType;
    }

    public HiringCategory getHiringCategory() {
        return hiringCategory;
    }

    public void setHiringCategory(HiringCategory hiringCategory) {
        this.hiringCategory = hiringCategory;
    }
    
}
