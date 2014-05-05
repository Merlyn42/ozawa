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
public class _v {

    @Expose
    private Integer CardTemplate;
    @Expose
    private Integer CardAbilityContainer;
    @Expose
    private Integer TemplateOverrideValues;
    @Expose
    private Integer CardTemplateLayout;

    public Integer getCardTemplate() {
        return CardTemplate;
    }

    public void setCardTemplate(Integer CardTemplate) {
        this.CardTemplate = CardTemplate;
    }

    public Integer getCardAbilityContainer() {
        return CardAbilityContainer;
    }

    public void setCardAbilityContainer(Integer CardAbilityContainer) {
        this.CardAbilityContainer = CardAbilityContainer;
    }

    public Integer getTemplateOverrideValues() {
        return TemplateOverrideValues;
    }

    public void setTemplateOverrideValues(Integer TemplateOverrideValues) {
        this.TemplateOverrideValues = TemplateOverrideValues;
    }

    public Integer getCardTemplateLayout() {
        return CardTemplateLayout;
    }

    public void setCardTemplateLayout(Integer CardTemplateLayout) {
        this.CardTemplateLayout = CardTemplateLayout;
    }

}
