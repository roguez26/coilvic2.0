package unit.test.TermDAO;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.term.Term;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.term.TermDAO;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class TermTest {

    private static final Term TEST_TERM = new Term();
    private static final TermDAO TERM_DAO = new TermDAO();

    @Before
    public void setUp() throws DAOException {
        TEST_TERM.setName("Febrero2024-Junio2024");
        int idTestTerm = TERM_DAO.registerTerm(TEST_TERM);
        TEST_TERM.setIdTerm(idTestTerm);
    }

    @After
    public void tearDown() throws DAOException {
        TERM_DAO.deleteTerm(TEST_TERM.getIdTerm());
    }

    @Test
    public void testSuccessInsertTerm() throws DAOException {
        Term term = new Term();
        term.setName("Agosto2024-Enero2025");
        int idTestTerm = TERM_DAO.registerTerm(term);
        TERM_DAO.deleteTerm(idTestTerm);
        Assert.assertTrue(idTestTerm > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureInsertTermByNameAlreadyRegistered() throws DAOException {
        Term term = new Term();
        term.setName(TEST_TERM.getName());
        TERM_DAO.registerTerm(term);
    }

    @Test
    public void testSuccesUpdateTerm() throws DAOException {
        Term term = new Term();
        term.setName("Agosto2024-Enero2025");
        term.setIdTerm(TEST_TERM.getIdTerm());
        int result = TERM_DAO.updateTerm(term);
        Assert.assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureUpdateTermByAlreadyRegisteredName() throws DAOException {
        Term term = new Term();
        term.setName("Agosto2024-Enero2025");
        term.setIdTerm(TEST_TERM.getIdTerm());

        Term auxTerm = new Term();
        auxTerm.setName("Agosto2024-Enero2025");
        int idTestTerm = TERM_DAO.registerTerm(auxTerm);
        try {
            TERM_DAO.updateTerm(term);
        } finally {
            TERM_DAO.deleteTerm(idTestTerm);
        }
    }

    @Test
    public void testSuccessDeleteTerm() throws DAOException {
        int result = TERM_DAO.deleteTerm(TEST_TERM.getIdTerm());
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteTermByIdNotAvailable() throws DAOException {
        int result = TERM_DAO.deleteTerm(9999);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetTermByName() throws DAOException {
        Term term = TERM_DAO.getTermByName(TEST_TERM.getName());
        Assert.assertEquals(TEST_TERM, term);
    }

    @Test
    public void testFailureGetTermByNameNotAvailable() throws DAOException {
        Term term = TERM_DAO.getTermByName("Agosto2025-Enero2026");
        Assert.assertNotEquals(TEST_TERM, term);
    }

    @Test
    public void testSuccessGetTermById() throws DAOException {
        Term term = TERM_DAO.getTermByIdTerm(TEST_TERM.getIdTerm());
        Assert.assertEquals(TEST_TERM, term);
    }

    @Test
    public void testFailureGetTermByIdNotAvailable() throws DAOException {
        Term term = TERM_DAO.getTermByIdTerm(9999);
        Assert.assertNotEquals(TEST_TERM, term);
    }

    @Test
    public void testGetTerms() throws DAOException {
        ArrayList<Term> expectedTerms = initializeTermsArray();
        ArrayList<Term> actualTerms = TERM_DAO.getTerms();
        assertEquals(expectedTerms, actualTerms);
        tearDownTermsArray(expectedTerms);
    }

    public ArrayList<Term> initializeTermsArray() throws DAOException {
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(TEST_TERM);

        Term termAux1 = new Term();
        termAux1.setName("Agosto2024-Enero2025");

        Term termAux2 = new Term();
        termAux2.setName("Febrero2025-Junio2025");

        int idTerm1 = TERM_DAO.registerTerm(termAux1);
        termAux1.setIdTerm(idTerm1);
        terms.add(termAux1);

        int idTerm2 = TERM_DAO.registerTerm(termAux2);
        termAux2.setIdTerm(idTerm2);
        terms.add(termAux2);

        return terms;
    }

    private void tearDownTermsArray(ArrayList<Term> terms) throws DAOException {
        for (int i = 1; i < terms.size(); i++) {
            TERM_DAO.deleteTerm(terms.get(i).getIdTerm());
        }
    }
}
