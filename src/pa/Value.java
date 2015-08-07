package pa;

/**
 * Created by devinmcgloin on 8/5/15.
 */
public final class Value {
    final String key;
    final String value;

    public Value(String key, String value) {

        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Value{" +
                "type='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public boolean ldataValueP(){
        if(LDATA.ldataP(key))
            return true;
        else
            return false;
    }

    public LDBN getLdataVal(){
        if(ldataValueP())
            return PA.getLDATA(value);
        return null;
    }

    /**
     * Unit could be in bracket form, or expression form, it parses to check if the unit is there.
     * @return
     */
    public boolean unitP(){

    }

    public String getUnit() {
        if(unitP())
            return value.split(" ")[1];
        return null;
    }

    public boolean referenceP(){

    }

    public Object getReference(){

    }

    public boolean valueP(){

    }

    public String getValue() {
        return value;
    }

}
