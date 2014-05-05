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
public class M_DefaultLayout {

    @Expose
    private M_Id_ m_Id;
    @Expose
    private String m_Name;
    @Expose
    private Integer m_PortraitRotation;
    @Expose
    private Double m_PortraitLeft;
    @Expose
    private Double m_PortraitBottom;
    @Expose
    private Double m_PortraitRight;
    @Expose
    private Double m_PortraitTop;

    public M_Id_ getM_Id() {
        return m_Id;
    }

    public void setM_Id(M_Id_ m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public Integer getM_PortraitRotation() {
        return m_PortraitRotation;
    }

    public void setM_PortraitRotation(Integer m_PortraitRotation) {
        this.m_PortraitRotation = m_PortraitRotation;
    }

    public Double getM_PortraitLeft() {
        return m_PortraitLeft;
    }

    public void setM_PortraitLeft(Double m_PortraitLeft) {
        this.m_PortraitLeft = m_PortraitLeft;
    }

    public Double getM_PortraitBottom() {
        return m_PortraitBottom;
    }

    public void setM_PortraitBottom(Double m_PortraitBottom) {
        this.m_PortraitBottom = m_PortraitBottom;
    }

    public Double getM_PortraitRight() {
        return m_PortraitRight;
    }

    public void setM_PortraitRight(Double m_PortraitRight) {
        this.m_PortraitRight = m_PortraitRight;
    }

    public Double getM_PortraitTop() {
        return m_PortraitTop;
    }

    public void setM_PortraitTop(Double m_PortraitTop) {
        this.m_PortraitTop = m_PortraitTop;
    }

}
