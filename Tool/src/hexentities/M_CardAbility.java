
package hexentities;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("com.googlecode.jsonschema2pojo")
public class M_CardAbility {

    @Expose
    private M_CardAbilityId m_CardAbilityId;
    @Expose
    private M_CardAbilityOverrides m_CardAbilityOverrides;

    public M_CardAbilityId getM_CardAbilityId() {
        return m_CardAbilityId;
    }

    public void setM_CardAbilityId(M_CardAbilityId m_CardAbilityId) {
        this.m_CardAbilityId = m_CardAbilityId;
    }

    public M_CardAbilityOverrides getM_CardAbilityOverrides() {
        return m_CardAbilityOverrides;
    }

    public void setM_CardAbilityOverrides(M_CardAbilityOverrides m_CardAbilityOverrides) {
        this.m_CardAbilityOverrides = m_CardAbilityOverrides;
    }

}
