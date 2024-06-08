package af.mcit.cdo.web.rest;

import static af.mcit.cdo.domain.NTATableAsserts.*;
import static af.mcit.cdo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.cdo.IntegrationTest;
import af.mcit.cdo.domain.NTATable;
import af.mcit.cdo.repository.NTATableRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NTATableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NTATableResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_STEP = "AAAAAAAAAA";
    private static final String UPDATED_STEP = "BBBBBBBBBB";

    private static final String DEFAULT_EDUCATION_DEGREE = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION_DEGREE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_SALARY = 1F;
    private static final Float UPDATED_SALARY = 2F;
    private static final Float SMALLER_SALARY = 1F - 1F;

    private static final String DEFAULT_SIGNATURE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nta-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NTATableRepository nTATableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNTATableMockMvc;

    private NTATable nTATable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NTATable createEntity(EntityManager em) {
        NTATable nTATable = new NTATable()
            .fullName(DEFAULT_FULL_NAME)
            .fatherName(DEFAULT_FATHER_NAME)
            .jobTitle(DEFAULT_JOB_TITLE)
            .step(DEFAULT_STEP)
            .educationDegree(DEFAULT_EDUCATION_DEGREE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .salary(DEFAULT_SALARY)
            .signature(DEFAULT_SIGNATURE);
        return nTATable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NTATable createUpdatedEntity(EntityManager em) {
        NTATable nTATable = new NTATable()
            .fullName(UPDATED_FULL_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .step(UPDATED_STEP)
            .educationDegree(UPDATED_EDUCATION_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .salary(UPDATED_SALARY)
            .signature(UPDATED_SIGNATURE);
        return nTATable;
    }

    @BeforeEach
    public void initTest() {
        nTATable = createEntity(em);
    }

    @Test
    @Transactional
    void createNTATable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NTATable
        var returnedNTATable = om.readValue(
            restNTATableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nTATable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NTATable.class
        );

        // Validate the NTATable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNTATableUpdatableFieldsEquals(returnedNTATable, getPersistedNTATable(returnedNTATable));
    }

    @Test
    @Transactional
    void createNTATableWithExistingId() throws Exception {
        // Create the NTATable with an existing ID
        nTATable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNTATableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nTATable)))
            .andExpect(status().isBadRequest());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nTATable.setFullName(null);

        // Create the NTATable, which fails.

        restNTATableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nTATable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFatherNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nTATable.setFatherName(null);

        // Create the NTATable, which fails.

        restNTATableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nTATable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNTATables() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList
        restNTATableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nTATable.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP)))
            .andExpect(jsonPath("$.[*].educationDegree").value(hasItem(DEFAULT_EDUCATION_DEGREE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(DEFAULT_SIGNATURE)));
    }

    @Test
    @Transactional
    void getNTATable() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get the nTATable
        restNTATableMockMvc
            .perform(get(ENTITY_API_URL_ID, nTATable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nTATable.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.step").value(DEFAULT_STEP))
            .andExpect(jsonPath("$.educationDegree").value(DEFAULT_EDUCATION_DEGREE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.signature").value(DEFAULT_SIGNATURE));
    }

    @Test
    @Transactional
    void getNTATablesByIdFiltering() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        Long id = nTATable.getId();

        defaultNTATableFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNTATableFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNTATableFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNTATablesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fullName equals to
        defaultNTATableFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fullName in
        defaultNTATableFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fullName is not null
        defaultNTATableFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fullName contains
        defaultNTATableFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fullName does not contain
        defaultNTATableFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByFatherNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fatherName equals to
        defaultNTATableFiltering("fatherName.equals=" + DEFAULT_FATHER_NAME, "fatherName.equals=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByFatherNameIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fatherName in
        defaultNTATableFiltering(
            "fatherName.in=" + DEFAULT_FATHER_NAME + "," + UPDATED_FATHER_NAME,
            "fatherName.in=" + UPDATED_FATHER_NAME
        );
    }

    @Test
    @Transactional
    void getAllNTATablesByFatherNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fatherName is not null
        defaultNTATableFiltering("fatherName.specified=true", "fatherName.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByFatherNameContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fatherName contains
        defaultNTATableFiltering("fatherName.contains=" + DEFAULT_FATHER_NAME, "fatherName.contains=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByFatherNameNotContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where fatherName does not contain
        defaultNTATableFiltering("fatherName.doesNotContain=" + UPDATED_FATHER_NAME, "fatherName.doesNotContain=" + DEFAULT_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllNTATablesByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where jobTitle equals to
        defaultNTATableFiltering("jobTitle.equals=" + DEFAULT_JOB_TITLE, "jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllNTATablesByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where jobTitle in
        defaultNTATableFiltering("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE, "jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllNTATablesByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where jobTitle is not null
        defaultNTATableFiltering("jobTitle.specified=true", "jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where jobTitle contains
        defaultNTATableFiltering("jobTitle.contains=" + DEFAULT_JOB_TITLE, "jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllNTATablesByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where jobTitle does not contain
        defaultNTATableFiltering("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE, "jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllNTATablesByStepIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where step equals to
        defaultNTATableFiltering("step.equals=" + DEFAULT_STEP, "step.equals=" + UPDATED_STEP);
    }

    @Test
    @Transactional
    void getAllNTATablesByStepIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where step in
        defaultNTATableFiltering("step.in=" + DEFAULT_STEP + "," + UPDATED_STEP, "step.in=" + UPDATED_STEP);
    }

    @Test
    @Transactional
    void getAllNTATablesByStepIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where step is not null
        defaultNTATableFiltering("step.specified=true", "step.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByStepContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where step contains
        defaultNTATableFiltering("step.contains=" + DEFAULT_STEP, "step.contains=" + UPDATED_STEP);
    }

    @Test
    @Transactional
    void getAllNTATablesByStepNotContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where step does not contain
        defaultNTATableFiltering("step.doesNotContain=" + UPDATED_STEP, "step.doesNotContain=" + DEFAULT_STEP);
    }

    @Test
    @Transactional
    void getAllNTATablesByEducationDegreeIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where educationDegree equals to
        defaultNTATableFiltering(
            "educationDegree.equals=" + DEFAULT_EDUCATION_DEGREE,
            "educationDegree.equals=" + UPDATED_EDUCATION_DEGREE
        );
    }

    @Test
    @Transactional
    void getAllNTATablesByEducationDegreeIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where educationDegree in
        defaultNTATableFiltering(
            "educationDegree.in=" + DEFAULT_EDUCATION_DEGREE + "," + UPDATED_EDUCATION_DEGREE,
            "educationDegree.in=" + UPDATED_EDUCATION_DEGREE
        );
    }

    @Test
    @Transactional
    void getAllNTATablesByEducationDegreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where educationDegree is not null
        defaultNTATableFiltering("educationDegree.specified=true", "educationDegree.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByEducationDegreeContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where educationDegree contains
        defaultNTATableFiltering(
            "educationDegree.contains=" + DEFAULT_EDUCATION_DEGREE,
            "educationDegree.contains=" + UPDATED_EDUCATION_DEGREE
        );
    }

    @Test
    @Transactional
    void getAllNTATablesByEducationDegreeNotContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where educationDegree does not contain
        defaultNTATableFiltering(
            "educationDegree.doesNotContain=" + UPDATED_EDUCATION_DEGREE,
            "educationDegree.doesNotContain=" + DEFAULT_EDUCATION_DEGREE
        );
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate equals to
        defaultNTATableFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate in
        defaultNTATableFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate is not null
        defaultNTATableFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate is greater than or equal to
        defaultNTATableFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate is less than or equal to
        defaultNTATableFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate is less than
        defaultNTATableFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where startDate is greater than
        defaultNTATableFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate equals to
        defaultNTATableFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate in
        defaultNTATableFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate is not null
        defaultNTATableFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate is greater than or equal to
        defaultNTATableFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate is less than or equal to
        defaultNTATableFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate is less than
        defaultNTATableFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where endDate is greater than
        defaultNTATableFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary equals to
        defaultNTATableFiltering("salary.equals=" + DEFAULT_SALARY, "salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary in
        defaultNTATableFiltering("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY, "salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary is not null
        defaultNTATableFiltering("salary.specified=true", "salary.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary is greater than or equal to
        defaultNTATableFiltering("salary.greaterThanOrEqual=" + DEFAULT_SALARY, "salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary is less than or equal to
        defaultNTATableFiltering("salary.lessThanOrEqual=" + DEFAULT_SALARY, "salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary is less than
        defaultNTATableFiltering("salary.lessThan=" + UPDATED_SALARY, "salary.lessThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllNTATablesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where salary is greater than
        defaultNTATableFiltering("salary.greaterThan=" + SMALLER_SALARY, "salary.greaterThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllNTATablesBySignatureIsEqualToSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where signature equals to
        defaultNTATableFiltering("signature.equals=" + DEFAULT_SIGNATURE, "signature.equals=" + UPDATED_SIGNATURE);
    }

    @Test
    @Transactional
    void getAllNTATablesBySignatureIsInShouldWork() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where signature in
        defaultNTATableFiltering("signature.in=" + DEFAULT_SIGNATURE + "," + UPDATED_SIGNATURE, "signature.in=" + UPDATED_SIGNATURE);
    }

    @Test
    @Transactional
    void getAllNTATablesBySignatureIsNullOrNotNull() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where signature is not null
        defaultNTATableFiltering("signature.specified=true", "signature.specified=false");
    }

    @Test
    @Transactional
    void getAllNTATablesBySignatureContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where signature contains
        defaultNTATableFiltering("signature.contains=" + DEFAULT_SIGNATURE, "signature.contains=" + UPDATED_SIGNATURE);
    }

    @Test
    @Transactional
    void getAllNTATablesBySignatureNotContainsSomething() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        // Get all the nTATableList where signature does not contain
        defaultNTATableFiltering("signature.doesNotContain=" + UPDATED_SIGNATURE, "signature.doesNotContain=" + DEFAULT_SIGNATURE);
    }

    private void defaultNTATableFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNTATableShouldBeFound(shouldBeFound);
        defaultNTATableShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNTATableShouldBeFound(String filter) throws Exception {
        restNTATableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nTATable.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP)))
            .andExpect(jsonPath("$.[*].educationDegree").value(hasItem(DEFAULT_EDUCATION_DEGREE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(DEFAULT_SIGNATURE)));

        // Check, that the count call also returns 1
        restNTATableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNTATableShouldNotBeFound(String filter) throws Exception {
        restNTATableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNTATableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNTATable() throws Exception {
        // Get the nTATable
        restNTATableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNTATable() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nTATable
        NTATable updatedNTATable = nTATableRepository.findById(nTATable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNTATable are not directly saved in db
        em.detach(updatedNTATable);
        updatedNTATable
            .fullName(UPDATED_FULL_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .step(UPDATED_STEP)
            .educationDegree(UPDATED_EDUCATION_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .salary(UPDATED_SALARY)
            .signature(UPDATED_SIGNATURE);

        restNTATableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNTATable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNTATable))
            )
            .andExpect(status().isOk());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNTATableToMatchAllProperties(updatedNTATable);
    }

    @Test
    @Transactional
    void putNonExistingNTATable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nTATable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNTATableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nTATable.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nTATable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNTATable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nTATable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNTATableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nTATable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNTATable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nTATable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNTATableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nTATable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNTATableWithPatch() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nTATable using partial update
        NTATable partialUpdatedNTATable = new NTATable();
        partialUpdatedNTATable.setId(nTATable.getId());

        partialUpdatedNTATable
            .step(UPDATED_STEP)
            .educationDegree(UPDATED_EDUCATION_DEGREE)
            .salary(UPDATED_SALARY)
            .signature(UPDATED_SIGNATURE);

        restNTATableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNTATable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNTATable))
            )
            .andExpect(status().isOk());

        // Validate the NTATable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNTATableUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNTATable, nTATable), getPersistedNTATable(nTATable));
    }

    @Test
    @Transactional
    void fullUpdateNTATableWithPatch() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nTATable using partial update
        NTATable partialUpdatedNTATable = new NTATable();
        partialUpdatedNTATable.setId(nTATable.getId());

        partialUpdatedNTATable
            .fullName(UPDATED_FULL_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .step(UPDATED_STEP)
            .educationDegree(UPDATED_EDUCATION_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .salary(UPDATED_SALARY)
            .signature(UPDATED_SIGNATURE);

        restNTATableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNTATable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNTATable))
            )
            .andExpect(status().isOk());

        // Validate the NTATable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNTATableUpdatableFieldsEquals(partialUpdatedNTATable, getPersistedNTATable(partialUpdatedNTATable));
    }

    @Test
    @Transactional
    void patchNonExistingNTATable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nTATable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNTATableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nTATable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nTATable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNTATable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nTATable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNTATableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nTATable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNTATable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nTATable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNTATableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nTATable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NTATable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNTATable() throws Exception {
        // Initialize the database
        nTATableRepository.saveAndFlush(nTATable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nTATable
        restNTATableMockMvc
            .perform(delete(ENTITY_API_URL_ID, nTATable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nTATableRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected NTATable getPersistedNTATable(NTATable nTATable) {
        return nTATableRepository.findById(nTATable.getId()).orElseThrow();
    }

    protected void assertPersistedNTATableToMatchAllProperties(NTATable expectedNTATable) {
        assertNTATableAllPropertiesEquals(expectedNTATable, getPersistedNTATable(expectedNTATable));
    }

    protected void assertPersistedNTATableToMatchUpdatableProperties(NTATable expectedNTATable) {
        assertNTATableAllUpdatablePropertiesEquals(expectedNTATable, getPersistedNTATable(expectedNTATable));
    }
}
