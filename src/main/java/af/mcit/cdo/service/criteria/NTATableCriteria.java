package af.mcit.cdo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.cdo.domain.NTATable} entity. This class is used
 * in {@link af.mcit.cdo.web.rest.NTATableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nta-tables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NTATableCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter fatherName;

    private StringFilter jobTitle;

    private StringFilter step;

    private StringFilter educationDegree;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private FloatFilter salary;

    private StringFilter signature;

    private Boolean distinct;

    public NTATableCriteria() {}

    public NTATableCriteria(NTATableCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.fatherName = other.optionalFatherName().map(StringFilter::copy).orElse(null);
        this.jobTitle = other.optionalJobTitle().map(StringFilter::copy).orElse(null);
        this.step = other.optionalStep().map(StringFilter::copy).orElse(null);
        this.educationDegree = other.optionalEducationDegree().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.salary = other.optionalSalary().map(FloatFilter::copy).orElse(null);
        this.signature = other.optionalSignature().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NTATableCriteria copy() {
        return new NTATableCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getFatherName() {
        return fatherName;
    }

    public Optional<StringFilter> optionalFatherName() {
        return Optional.ofNullable(fatherName);
    }

    public StringFilter fatherName() {
        if (fatherName == null) {
            setFatherName(new StringFilter());
        }
        return fatherName;
    }

    public void setFatherName(StringFilter fatherName) {
        this.fatherName = fatherName;
    }

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public Optional<StringFilter> optionalJobTitle() {
        return Optional.ofNullable(jobTitle);
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            setJobTitle(new StringFilter());
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public StringFilter getStep() {
        return step;
    }

    public Optional<StringFilter> optionalStep() {
        return Optional.ofNullable(step);
    }

    public StringFilter step() {
        if (step == null) {
            setStep(new StringFilter());
        }
        return step;
    }

    public void setStep(StringFilter step) {
        this.step = step;
    }

    public StringFilter getEducationDegree() {
        return educationDegree;
    }

    public Optional<StringFilter> optionalEducationDegree() {
        return Optional.ofNullable(educationDegree);
    }

    public StringFilter educationDegree() {
        if (educationDegree == null) {
            setEducationDegree(new StringFilter());
        }
        return educationDegree;
    }

    public void setEducationDegree(StringFilter educationDegree) {
        this.educationDegree = educationDegree;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public FloatFilter getSalary() {
        return salary;
    }

    public Optional<FloatFilter> optionalSalary() {
        return Optional.ofNullable(salary);
    }

    public FloatFilter salary() {
        if (salary == null) {
            setSalary(new FloatFilter());
        }
        return salary;
    }

    public void setSalary(FloatFilter salary) {
        this.salary = salary;
    }

    public StringFilter getSignature() {
        return signature;
    }

    public Optional<StringFilter> optionalSignature() {
        return Optional.ofNullable(signature);
    }

    public StringFilter signature() {
        if (signature == null) {
            setSignature(new StringFilter());
        }
        return signature;
    }

    public void setSignature(StringFilter signature) {
        this.signature = signature;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NTATableCriteria that = (NTATableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(fatherName, that.fatherName) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(step, that.step) &&
            Objects.equals(educationDegree, that.educationDegree) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(signature, that.signature) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, fatherName, jobTitle, step, educationDegree, startDate, endDate, salary, signature, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NTATableCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalFatherName().map(f -> "fatherName=" + f + ", ").orElse("") +
            optionalJobTitle().map(f -> "jobTitle=" + f + ", ").orElse("") +
            optionalStep().map(f -> "step=" + f + ", ").orElse("") +
            optionalEducationDegree().map(f -> "educationDegree=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalSalary().map(f -> "salary=" + f + ", ").orElse("") +
            optionalSignature().map(f -> "signature=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
