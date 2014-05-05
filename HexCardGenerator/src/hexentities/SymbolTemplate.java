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
 ******************************************************************************/package hexentities;

import java.net.URL;
import java.util.Map;

import json.JsonReader;

public class SymbolTemplate {

	public URL templateId;
	public String cardText;
	public String imageName;
	public double sizeRatio;
	//private Bitmap image;
	public static String symbolJson = "json/data/symbols.json";
	
	private static Map<String,SymbolTemplate>	ALLTEMPLATES;
	
	public static Map<String, SymbolTemplate> getAllTemplates(String symbolJson) {
		if (ALLTEMPLATES == null) {
			JsonReader jsonReader = new JsonReader();
			ALLTEMPLATES = jsonReader.deserializeJSONInputStreamToSymbolTemplates(ClassLoader.getSystemResourceAsStream(symbolJson));
		}
		return ALLTEMPLATES;
	}
	
	public static SymbolTemplate findSymbolTemplate(String symbol, Map<String,SymbolTemplate> templates){
		return templates.get(symbol);
	}
}
