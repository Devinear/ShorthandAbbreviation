package study.devin.shorthandabbreviation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class AbbreviationAdapter extends BaseAdapter {

    // value
    private Context context;
    private ArrayList<AbbreviationItemByMap> listAbbreviation;

    // 리스트에서 역순으로 토글버튼을 구하는 동작으로 인해 Map의 필요성이 떨어짐..
    private HashMap<AbbreviationItemByMap, MapValue> mapAbbreviation;
    private AbbreviationItemByMap[] arrMapKey;

    private boolean isHighlight;
    private int highlightPosition;
    private boolean isUseList;

    private final int HIGHLIGHT_COLOR   = 0xFF7FFFD4; // Aquamarine
    private final int ORIGNAL_COLOR     = 0x00000000; // 투명

    public AbbreviationAdapter(Context context) {
        this.context = context;
        isHighlight = false;
        highlightPosition = -1;
        isUseList = false;

        listAbbreviation = new ArrayList<>();
        mapAbbreviation = new HashMap<>();
    }

    // LIST 추가
    public void AddItemFromList(ArrayList<AbbreviationItemByMap> list) {
        isUseList = true;
        listAbbreviation = list;
    }

    // MAP 추가 - 추후 사용...?!
    public void AddItemFromMap(HashMap<AbbreviationItemByMap, MapValue> map) {
        isUseList = false;
        mapAbbreviation = map;
        arrMapKey = mapAbbreviation.keySet().toArray(new AbbreviationItemByMap[mapAbbreviation.size()]);
    }

    public int getPosition(AbbreviationItemByMap itemByMap) {
        if(isUseList)
            return getPositionUseList(itemByMap);
        else
            return getPositionUseMap(itemByMap);
    }

    private int getPositionUseList(AbbreviationItemByMap itemByMap) {
        for(int i =0; i < listAbbreviation.size(); i++ ) {
            if(listAbbreviation.get(i).compare(itemByMap))
                return i;
        }
        return -1;
    }

    private int getPositionUseMap(AbbreviationItemByMap itemByMap) {
        MapValue value = mapAbbreviation.get(itemByMap);
        if(value != null)
            return value.getValuePosition();
        else
            return -1;
    }

    public int getPositionContent(int startPosition, String content) {
        if(isUseList)
            return getPositionContentUseList(startPosition, content);
        else
            return -1;
    }

    private int getPositionContentUseList(int startPosition, String content) {
        if(startPosition >= listAbbreviation.size())
            startPosition = 0;

        for(int i =startPosition; i < listAbbreviation.size(); i++ ) {
            if(listAbbreviation.get(i).containContent(content))
                return i;
        }
        return -1;
    }

    // 특정 Position Item 하이라이트 설정
    public void setItemHighlight(boolean isHighlight, int position) {
        this.isHighlight = isHighlight;
        this.highlightPosition = position;
    }

    @Override
    public int getCount() {
        if(isUseList)
            return listAbbreviation.size();
        else
            return mapAbbreviation.size();
    }

    @Override
    public AbbreviationItemByMap getItem(int position) {
        if(isUseList) {
            if(position >= listAbbreviation.size())
                return null;
            return listAbbreviation.get(position);
        }
        else {
            // 완전체로 내보내자.
            if(arrMapKey.length <= position)
                return null;
            AbbreviationItemByMap keyItem = arrMapKey[position];
            String content = mapAbbreviation.get(keyItem).getValueContent();
            AbbreviationItemByMap item = new AbbreviationItemByMap.Builder()
                    .setInitialConsonant(keyItem.getInitialConsonant())
                    .setMiddleConsonant(keyItem.getMiddleConsonant())
                    .setFinalConsonant(keyItem.getFinalConsonant())
                    .setNumber(keyItem.getNumber()).setContent(content).build();
            return item;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View가 화면 넘어에 위치하여 현재 보이지 않는 경우 convertView는 null로 전달됨
        if(convertView == null) {
            // view가 null인 경우 커스텀 레이아웃을 얻어옴
            // LayoutInflater :: XML에 정의된 Resource(자원)들을 View의 형태로 반환해 준다.
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.word_custom_row, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.tvRowNo        = (TextView) convertView.findViewById(R.id.table_row_no);
            holder.tvRowInitial   = (TextView) convertView.findViewById(R.id.table_row_initial);
            holder.tvRowMiddle    = (TextView) convertView.findViewById(R.id.table_row_middle);
            holder.tvRowFinal     = (TextView) convertView.findViewById(R.id.table_row_final);
            holder.tvRowNumber    = (TextView) convertView.findViewById(R.id.table_row_number);
            holder.tvRowContent   = (TextView) convertView.findViewById(R.id.table_row_contents);

            convertView.setTag(holder);
        }

        AbbreviationItemByMap item = null;
        if(isUseList)
            item = listAbbreviation.get(position);
        else if(arrMapKey.length > position)
            item = arrMapKey[position];

        final AbbreviationItemByMap finalItem = item;
        final int finalPosition = position;

        if(item != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            // 표기되는 숫자는 1부터 시작하자
            holder.tvRowNo.setText(String.format("%d", position +1));
            holder.tvRowInitial.setText(item.getInitialConsonant());
            holder.tvRowMiddle.setText(item.getMiddleConsonant());
            holder.tvRowFinal.setText(item.getFinalConsonant());
            holder.tvRowNumber.setText(item.getNumber());
            holder.tvRowContent.setText(item.getContent());
            if(isUseList)
                holder.tvRowContent.setText(item.getContent());
            else
                holder.tvRowContent.setText(mapAbbreviation.get(item).getValueContent());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리스트의 아이템을 선택한 경우에는 해당 아이템의 정보를 Main Activity 전달하여 해당 Toggle 버튼을 체크하자
                ((MainActivity)context).clickedListItem(finalItem, finalPosition);
            }
        });

        // 하이라이트 설정
        if(isHighlight && position == highlightPosition)
            convertView.setBackgroundColor(HIGHLIGHT_COLOR);
        else
            convertView.setBackgroundColor(ORIGNAL_COLOR);

        return convertView;
    }

    static class ViewHolder {
        TextView tvRowNo;
        TextView tvRowInitial;
        TextView tvRowMiddle;
        TextView tvRowFinal;
        TextView tvRowNumber;
        TextView tvRowContent;
    }
}
