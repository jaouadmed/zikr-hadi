package com.hadi.zikr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hadi.zikr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZikrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zikr.class);
        Zikr zikr1 = new Zikr();
        zikr1.setId(1L);
        Zikr zikr2 = new Zikr();
        zikr2.setId(zikr1.getId());
        assertThat(zikr1).isEqualTo(zikr2);
        zikr2.setId(2L);
        assertThat(zikr1).isNotEqualTo(zikr2);
        zikr1.setId(null);
        assertThat(zikr1).isNotEqualTo(zikr2);
    }
}
