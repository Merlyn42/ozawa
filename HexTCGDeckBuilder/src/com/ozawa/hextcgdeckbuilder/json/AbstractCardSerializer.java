package com.ozawa.hextcgdeckbuilder.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by lreading on 19/12/13.
 */
public class AbstractCardSerializer implements JsonDeserializer<AbstractCard> {
    @Override
    public AbstractCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Card card = jsonDeserializationContext.deserialize(jsonElement, Card.class);
        if(Arrays.asList(card.cardType).contains(CardType.RESOURCE)){
            return jsonDeserializationContext.deserialize(jsonElement, ResourceCard.class);
        }else{
            return card;
        }
    }
}
