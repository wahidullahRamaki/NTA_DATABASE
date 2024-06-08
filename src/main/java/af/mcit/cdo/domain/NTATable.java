package af.mcit.cdo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NTATable.
 */
@Entity
@Table(name = "nta_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NTATable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    // @SequenceGenerator(name = "sequenceGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "father_name", nullable = false)
    private String fatherName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "step")
    private String step;

    @Column(name = "education_degree")
    private String educationDegree;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "salary")
    private Float salary;

    @Column(name = "signature")
    private String signature;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NTATable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public NTATable fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public NTATable fatherName(String fatherName) {
        this.setFatherName(fatherName);
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public NTATable jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getStep() {
        return this.step;
    }

    public NTATable step(String step) {
        this.setStep(step);
        return this;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getEducationDegree() {
        return this.educationDegree;
    }

    public NTATable educationDegree(String educationDegree) {
        this.setEducationDegree(educationDegree);
        return this;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public NTATable startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public NTATable endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getSalary() {
        return this.salary;
    }

    public NTATable salary(Float salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getSignature() {
        return this.signature;
    }

    public NTATable signature(String signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NTATable)) {
            return false;
        }
        return getId() != null && getId().equals(((NTATable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NTATable{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", step='" + getStep() + "'" +
            ", educationDegree='" + getEducationDegree() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", salary=" + getSalary() +
            ", signature='" + getSignature() + "'" +
            "}";
    }
}
