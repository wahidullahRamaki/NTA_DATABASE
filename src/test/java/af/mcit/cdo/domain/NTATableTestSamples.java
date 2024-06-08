package af.mcit.cdo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NTATableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NTATable getNTATableSample1() {
        return new NTATable()
            .id(1L)
            .fullName("fullName1")
            .fatherName("fatherName1")
            .jobTitle("jobTitle1")
            .step("step1")
            .educationDegree("educationDegree1")
            .signature("signature1");
    }

    public static NTATable getNTATableSample2() {
        return new NTATable()
            .id(2L)
            .fullName("fullName2")
            .fatherName("fatherName2")
            .jobTitle("jobTitle2")
            .step("step2")
            .educationDegree("educationDegree2")
            .signature("signature2");
    }

    public static NTATable getNTATableRandomSampleGenerator() {
        return new NTATable()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .fatherName(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString())
            .step(UUID.randomUUID().toString())
            .educationDegree(UUID.randomUUID().toString())
            .signature(UUID.randomUUID().toString());
    }
}
