package com.ozawa.hextcgdeckbuilder.json;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lreading on 18/12/13.
 */
public class JsonReader {

    Gson gson;

    public JsonReader(Context context){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
        gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
        gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
        gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
        gsonBuilder.registerTypeAdapter(AbstractCard.class, new CardSerializer());
        gsonBuilder.registerTypeAdapter(CardTemplate.class, new CardTemplateSerializer(context));
        gson = gsonBuilder.create();
    }

    public AbstractCard deserializeJSONInputStreamToCard(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        AbstractCard newCard = gson.fromJson(reader,AbstractCard.class);
        return newCard;
    }
    
    public CardTemplate[] deserializeJSONInputStreamToCardTemplates(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        CardTemplate[] templates = gson.fromJson(reader,CardTemplate[].class);
        return templates;
    }

    public List<AbstractCard> deserializeJSONInputStreamsToCard(ArrayList<InputStream> jsonStreams){
        AbstractCard[] newCards = new AbstractCard[jsonStreams.size()];
        for(int i = 0;i<jsonStreams.size();i++){
            newCards[i]=deserializeJSONInputStreamToCard(jsonStreams.get(i));
        }
        return Arrays.asList(newCards);
    }

}
