package com.qualize.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qualize.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettlementsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Settlements.class);
        Settlements settlements1 = new Settlements();
        settlements1.setId(1L);
        Settlements settlements2 = new Settlements();
        settlements2.setId(settlements1.getId());
        assertThat(settlements1).isEqualTo(settlements2);
        settlements2.setId(2L);
        assertThat(settlements1).isNotEqualTo(settlements2);
        settlements1.setId(null);
        assertThat(settlements1).isNotEqualTo(settlements2);
    }
}
