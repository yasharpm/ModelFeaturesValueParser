package com.yashoid.modelfeaturesvalueparser;

import com.yashoid.mmv.ModelFeatures;
import com.yashoid.yashson.datareader.DataReader;
import com.yashoid.yashson.datareader.ListDataReader;
import com.yashoid.yashson.datareader.ObjectDataReader;
import com.yashoid.yashson.valueparser.ValueParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelFeaturesValueParser extends ValueParser {

    public ModelFeaturesValueParser() {

    }

    @Override
    protected Object onParseNotNullValue(DataReader reader) throws IOException {
        return readModelFeatures(reader);
    }

    private ModelFeatures readModelFeatures(DataReader reader) throws IOException {
        ModelFeatures.Builder builder = new ModelFeatures.Builder();

        ObjectDataReader objectReader = reader.asObjectReader();

        while (objectReader.hasNextField()) {
            DataReader fieldReader = objectReader.nextField();

            String fieldName = fieldReader.getFieldName();

            int fieldType = fieldReader.getFieldType();

            switch (fieldType) {
                case DataReader.TYPE_NULL:
                    builder.add(fieldName, null);
                    break;
                case DataReader.TYPE_STRING:
                    builder.add(fieldName, fieldReader.readString());
                    break;
                case DataReader.TYPE_BOOLEAN:
                    builder.add(fieldName, fieldReader.readBoolean());
                    break;
                case DataReader.TYPE_OBJECT:
                    builder.add(fieldName, readModelFeatures(fieldReader));
                    break;
                case DataReader.TYPE_LIST:
                    builder.add(fieldName, readModelFeaturesList(fieldReader));
                    break;
                case DataReader.TYPE_NUMBER:
                    String sNumber = fieldReader.readString();

                    Number value = null;

                    try {
                        value = Integer.parseInt(sNumber);
                    } catch (NumberFormatException e) { }

                    if (value == null) {
                        try {
                            value = Double.parseDouble(sNumber);
                        } catch (NumberFormatException e) { }

                        if (value == null) {
                            throw new IllegalStateException("Field value type is a number but not integer or double.");
                        }
                    }

                    builder.add(fieldName, value);
                    break;
            }
        }

        reader.onReadingFinished();

        return builder.build();
    }

    private List<ModelFeatures> readModelFeaturesList(DataReader reader) throws IOException {
        List<ModelFeatures> list = new ArrayList<>();

        ListDataReader listReader = reader.asListReader();

        while (listReader.hasNext()) {
            list.add(readModelFeatures(listReader.next()));
        }

        reader.onReadingFinished();

        return list;
    }

}
