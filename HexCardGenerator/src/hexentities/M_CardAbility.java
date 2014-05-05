/*******************************************************************************
 * Hex TCG Card Generator
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
