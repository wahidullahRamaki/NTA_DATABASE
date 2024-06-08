package af.mcit.cdo.domain;

import static af.mcit.cdo.domain.NTATableTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import af.mcit.cdo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NTATableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NTATable.class);
        NTATable nTATable1 = getNTATableSample1();
        NTATable nTATable2 = new NTATable();
        assertThat(nTATable1).isNotEqualTo(nTATable2);

        nTATable2.setId(nTATable1.getId());
        assertThat(nTATable1).isEqualTo(nTATable2);

        nTATable2 = getNTATableSample2();
        assertThat(nTATable1).isNotEqualTo(nTATable2);
    }
}
