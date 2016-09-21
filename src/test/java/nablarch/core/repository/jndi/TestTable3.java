package nablarch.core.repository.jndi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * テストバッチインプット
 *
 */
@Entity
@Table(name = "TEST_TABLE")
public class TestTable3 {

    public TestTable3() {
    };

    public TestTable3(String col1, Integer col2, String col3) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
    }

    @Id
    @Column(name = "COL1", length = 5)
    public String col1;

    @Column(name = "COL2", length = 10)
    public Integer col2;

    @Column(name = "COL3", length = 10)
    public String col3;
}
