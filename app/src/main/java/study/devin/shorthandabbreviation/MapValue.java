package study.devin.shorthandabbreviation;

/**
 * Created by Devin on 2017-04-30.
 * Map을 구성할때 Value 값으로 추가적인 Position이 필요함.
 * 그러나 역으로 토글버튼을 구하는 동작에서는 Map의 구조가 불필요하기때문에 사용하지 않음.
 */

public class MapValue {
    private int position;
    private String content;

    public MapValue(int position, String content) {
        this.position = position;
        this.content = content;
    }
    public int getValuePosition() {return position;}
    public String getValueContent() {return content;}
}