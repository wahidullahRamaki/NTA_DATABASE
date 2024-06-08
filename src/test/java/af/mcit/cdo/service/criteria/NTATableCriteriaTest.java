package af.mcit.cdo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NTATableCriteriaTest {

    @Test
    void newNTATableCriteriaHasAllFiltersNullTest() {
        var nTATableCriteria = new NTATableCriteria();
        assertThat(nTATableCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nTATableCriteriaFluentMethodsCreatesFiltersTest() {
        var nTATableCriteria = new NTATableCriteria();

        setAllFilters(nTATableCriteria);

        assertThat(nTATableCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nTATableCriteriaCopyCreatesNullFilterTest() {
        var nTATableCriteria = new NTATableCriteria();
        var copy = nTATableCriteria.copy();

        assertThat(nTATableCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nTATableCriteria)
        );
    }

    @Test
    void nTATableCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nTATableCriteria = new NTATableCriteria();
        setAllFilters(nTATableCriteria);

        var copy = nTATableCriteria.copy();

        assertThat(nTATableCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nTATableCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nTATableCriteria = new NTATableCriteria();

        assertThat(nTATableCriteria).hasToString("NTATableCriteria{}");
    }

    private static void setAllFilters(NTATableCriteria nTATableCriteria) {
        nTATableCriteria.id();
        nTATableCriteria.fullName();
        nTATableCriteria.fatherName();
        nTATableCriteria.jobTitle();
        nTATableCriteria.step();
        nTATableCriteria.educationDegree();
        nTATableCriteria.startDate();
        nTATableCriteria.endDate();
        nTATableCriteria.salary();
        nTATableCriteria.signature();
        nTATableCriteria.distinct();
    }

    private static Condition<NTATableCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getFatherName()) &&
                condition.apply(criteria.getJobTitle()) &&
                condition.apply(criteria.getStep()) &&
                condition.apply(criteria.getEducationDegree()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getSalary()) &&
                condition.apply(criteria.getSignature()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NTATableCriteria> copyFiltersAre(NTATableCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getFatherName(), copy.getFatherName()) &&
                condition.apply(criteria.getJobTitle(), copy.getJobTitle()) &&
                condition.apply(criteria.getStep(), copy.getStep()) &&
                condition.apply(criteria.getEducationDegree(), copy.getEducationDegree()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getSalary(), copy.getSalary()) &&
                condition.apply(criteria.getSignature(), copy.getSignature()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
