package com.qualize.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qualize.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CryptoTrackerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CryptoTracker.class);
        CryptoTracker cryptoTracker1 = new CryptoTracker();
        cryptoTracker1.setId(1L);
        CryptoTracker cryptoTracker2 = new CryptoTracker();
        cryptoTracker2.setId(cryptoTracker1.getId());
        assertThat(cryptoTracker1).isEqualTo(cryptoTracker2);
        cryptoTracker2.setId(2L);
        assertThat(cryptoTracker1).isNotEqualTo(cryptoTracker2);
        cryptoTracker1.setId(null);
        assertThat(cryptoTracker1).isNotEqualTo(cryptoTracker2);
    }
}
