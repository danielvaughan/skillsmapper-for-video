package org.skillsmapper.factservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class FactRepositoryTests {

    @Autowired
    private FactRepository facts;

    @Test
    public void testFindByType() {
        Fact fact = new Fact("Dan", "learning", "GCP");
        facts.save(fact);

        List<Fact> findByType = facts.findByLevel(fact.getLevel());
        assertThat(findByType).extracting(Fact::getLevel).containsOnly(fact.getSkill());
    }
}
